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
import org.unique.web.annotation.PathVariable;
import org.unique.web.annotation.Route.HttpMethod;
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
			for (Method method : methods) {
				
				org.unique.web.annotation.Route mapping = method.getAnnotation(org.unique.web.annotation.Route.class);
				
				//action方法
				if (isLegalMethod(method) && null != mapping) {
					
					// action路径
					String path = mapping.value().equals("default") ? method.getName() : mapping.value();
					
					HttpMethod methodType = mapping.method();
					
					String viewPath = (nameSpace +  path).replaceAll("//", "/");
					String viewReg = viewPath;
					
					if(!isLegalAction(viewReg)){
						warnning(controller, method, " 不合法的action！");
						continue;
					}
					
					Parameter[] parameters = method.getParameters();
					
					Object[] arguments = new Object[parameters.length];
					int count = 0;
					
					if (parameters.length > 0) {
						for (int i = 0; i < parameters.length; i++) {
							arguments[i] = parameters[i];
							if (!parameters[i].getType().equals(R.class)) {
								if (viewReg.indexOf("$") != -1 && null != parameters[i].getDeclaredAnnotation(PathVariable.class)) {
									if (parameters[i].getType().equals(Integer.class)) {
										viewReg = viewReg.replaceFirst("\\$\\d+", "(\\\\d+)");
									} else {
										viewReg = viewReg.replaceFirst("\\$\\d+", "(\\\\w+)");
									}
									count++;
								}
							}
						}
					} 
					if (StringUtils.getCharCount(viewPath, "$") != count) {
						warnning(controller, method, " 方法参数列表和访问路径的参数个数不匹配！");
						continue;
					}
					viewReg = "^" + viewReg + "$";
					Route action = new Route(controller, method, arguments, methodType, viewPath);
					if (null != urlMapping.get(viewReg)) {
						throw new RuntimeException(controller.getName() + ", action \"" + viewPath + "\"重复");
					}
					urlMapping.put(viewReg, action);
					logger.info("route ：" + viewPath);
				}
			}
		}
		logger.info("route size ：" + urlMapping.size());
		return urlMapping;
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
		String regex = "^([0-9a-zA-Z_#&/$=?])+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(action);
		return m.find();
	}

	/**
	 * 判断是否是一个合法的action请求
	 * @param method 方法
	 * @return true：合法 false：不合法
	 */
	private boolean isLegalMethod(Method method) {
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
		org.unique.web.annotation.Route mapping = method.getAnnotation(org.unique.web.annotation.Route.class);
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
	public Route getRoute(String url) {
		url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
		Route action = urlMapping.get("^" + url + "$");
		if (null == action) {
			action = urlMapping.get(("^" + url + "/index$").replaceAll("//", "/"));
			if (null == action) {
				Set<String> mappings = urlMapping.keySet();
				for (String mapping : mappings) {
					Pattern p = Pattern.compile(mapping);
					Matcher m = p.matcher(url);
					if (m.find()) {
						action = urlMapping.get(mapping);
						Object[] args = action.getParameters();
						for (int i = 0; null != args && i < args.length; i++) {
							if (!args[i].toString().contains(R.class.getName())) {
								args[i] = parseObject(m.group(i + 1));
							}
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
