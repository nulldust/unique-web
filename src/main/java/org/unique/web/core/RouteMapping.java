package org.unique.web.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.commons.utils.CollectionUtil;
import org.unique.commons.utils.StringUtils;
import org.unique.ioc.AbstractBeanFactory;
import org.unique.ioc.impl.SingleBean;
import org.unique.web.annotation.Controller;
import org.unique.web.annotation.Path;
import org.unique.web.annotation.Path.HttpMethod;
import org.unique.web.annotation.PathParam;
import org.unique.web.render.Render;

/**
 * actionMapping
 * @author biezhi
 * @since 1.0
 */
public final class RouteMapping {

	private static Logger logger = LoggerFactory.getLogger(RouteMapping.class);

	// route mapping
	private Map<String, Route> urlMapping = CollectionUtil.newHashMap();
	
	private AbstractBeanFactory beanFactory = new SingleBean();

	private RouteMapping() {
	}

	public static RouteMapping single() {
		return SingleHoder.single;
	}

	private static class SingleHoder {

		private static final RouteMapping single = new RouteMapping();
	}

	/**
	 * 构建actionMapping映射
	 * @return action映射的map
	 */
	public Map<String, Route> buildRouteMapping() {
		urlMapping.clear();
		//所有控制器
		List<Class<?>> controllerList = beanFactory.getClassesByAnnotation(Controller.class);
		
		//遍历控制器
		for (Class<?> controller : controllerList) {
			Method[] methods = controller.getMethods();
			String nameSpace = controller.getAnnotation(Controller.class).value();
			nameSpace = nameSpace.endsWith("/") ? nameSpace : nameSpace + "/";
			
			buildRoute(controller, nameSpace, methods);
			
		}
		logger.info("route size ：" + urlMapping.size());
		return urlMapping;
	}
	
	private void buildRoute(Class<?> controller, String nameSpace, Method[] methods){
		for (Method method : methods) {
			
			Path mapping = method.getAnnotation(Path.class);
			//route方法
			if (isLegalRoute(method) && null != mapping) {
				
				// action路径
				String path = mapping.value().equals("default") ? method.getName() : mapping.value();
				
				HttpMethod methodType = mapping.method();
				
				String viewPath = (nameSpace +  path).replaceAll("(//)+", "/");
				String viewReg = viewPath;
				
				if(!isLegalAction(viewReg)){
					warnning(controller, method, " 不合法的action！");
					continue;
				}
				
				Parameter[] parameters = method.getParameters();
				Object[] arguments = new Object[parameters.length];
				
				viewReg = buildParam(parameters, arguments, viewReg);
				
				// 构建一个路由
				Route action = new Route(controller, method, arguments, methodType, viewPath);
				
				if (null != urlMapping.get(viewReg)) {
					warnning(controller, method, "route \"" + viewPath + "\"重复");
				}
				urlMapping.put(viewReg, action);
				logger.info("route ：" + viewPath);
			}
		}
	}
	
	private String buildParam(Parameter[] parameters, Object[] arguments, String viewReg){
		if (parameters.length > 0 && viewReg.indexOf(":") != -1) {
			// 遍历方法内的参数
			for (int i = 0,len=parameters.length; i < len; i++) {
				arguments[i] = parameters[i];
				// 如果有url path参数
				PathParam pathParam = parameters[i].getDeclaredAnnotation(PathParam.class);
				if (null != pathParam) {
					// 存在该参数
					if(parameters[i].getType().equals(Integer.class)){
						viewReg = viewReg.replaceFirst("/(:\\w+)", "/(\\\\d+)");
					}
					if(parameters[i].getType().equals(String.class)){
						viewReg = viewReg.replaceFirst("/(:\\w+)", "/(\\\\w+)");
					}
				}
			}
		}
		return "^" + viewReg + "$";
	}
	
	
	/**
	 * 取得访问修饰符为public的方法集合
	 * @return 要过滤的方法
	 */
	private Set<String> buildExcludedMethodName() {
		Set<String> publicMethodNameSet = CollectionUtil.newHashSet();
		Method[] methods = Object.class.getMethods();
		for (Method method : methods) {
			publicMethodNameSet.add(method.getName());
		}
		// jrebel产生的代理方法__rebel_clinit,需要将此方法排除
		publicMethodNameSet.add("__rebel_clinit");
		return publicMethodNameSet;
	}
	
	/**
	 * 判断action上的映射是否是一个合法的
	 * @param action 方法注解的value 
	 * @return boolean
	 */
	private boolean isLegalAction(final String action){
		String regex = "^([\\w-_#&/:=.;%?])+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(action);
		return m.find();
	}

	/**
	 * 判断是否是一个合法的action请求
	 * @param method 方法
	 * @return true：合法 false：不合法
	 */
	private boolean isLegalRoute(Method method) {
		Set<String> excludedMethod = this.buildExcludedMethodName();
		if (excludedMethod.contains(method.getName())) {
			return false;
		}
		if (Modifier.isStatic(method.getModifiers())) {
			return false;
		}
		if (Modifier.isPrivate(method.getModifiers())) {
			return false;
		}
		org.unique.web.annotation.Path mapping = method.getAnnotation(org.unique.web.annotation.Path.class);
		if (null != mapping && mapping.value().length() == 0) {
			return false;
		}
		Class<?> returnType = method.getReturnType();
		// 支持三种返回值
		if (!returnType.equals(void.class) && !returnType.equals(String.class) && !returnType.equals(Render.class)) {
			return false;
		}
		return true;
	}
	
	private final void warnning(Class<?> clazz, Method method, String msg) {
		logger.warn(clazz.getName() + "|" + method.getName() + "," + msg);
	}
	
	/**
	 * 根据url获取Action
	 * @param url 请求的url
	 * @return Action对象
	 */
	public Route getRoute(String targetPath) {
		Route action = urlMapping.get(targetPath);
		if (null == action) {
			action = urlMapping.get((targetPath + "/index"));
			if (null == action) {
				Set<String> mappings = urlMapping.keySet();
				for (String mapping : mappings) {
					Pattern p = Pattern.compile(mapping);
					Matcher m = p.matcher(targetPath);
					if (m.find()) {
						action = urlMapping.get(mapping);
						Object[] args = action.getParameters();
						for (int i = 0; i < args.length; i++) {
							String param = targetPath.replaceFirst(mapping, "$" + (i+1));
							args[i] = parseObject(param);
						}
						break;
					}
				}
			}
		}
		return action;
	}
	
	/**
	 * 字符串转对象
	 * @param param 字符串参数
	 * @return Object对象
	 */
	private Object parseObject(final String param) {
		if (StringUtils.isNumeric(param)) {
			return Integer.valueOf(param);
		}
		return param;
	}

}
