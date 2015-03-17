/**
 * Copyright (c) 2014-2015, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.unique.web.annotation.PathParam;
import org.unique.web.annotation.Route.HttpMethod;
import org.unique.web.render.Render;

/**
 * 路由映射器
 * @author biezhi
 * @since 1.0
 */
public final class RouteMapping {
	
	private static Logger logger = LoggerFactory.getLogger(RouteMapping.class);

	private final Map<String, Route> urlMapping;
	
	private final AbstractBeanFactory beanFactory;

	private RouteMapping() {
		urlMapping = CollectionUtil.newHashMap();
		beanFactory = new SingleBean();
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
		
		if(null != controllerList && controllerList.size() > 0){
			//遍历控制器
			for (Class<?> controller : controllerList) {
				Method[] methods = controller.getMethods();
				String nameSpace = controller.getAnnotation(Controller.class).value();
				nameSpace = nameSpace.endsWith("/") ? nameSpace : nameSpace + "/";
				
				//构建路由
				buildRoute(controller, nameSpace, methods);
				
			}
		}
		logger.info("route size ：" + urlMapping.size());
		return urlMapping;
	}
	
	/**
	 * 构建路由
	 * @param controller 	控制器class
	 * @param nameSpace 	控制器命名空间
	 * @param methods 		控制器方法
	 */
	private void buildRoute(Class<?> controller, String nameSpace, Method[] methods){
		for (Method method : methods) {
			
			org.unique.web.annotation.Route mapping = method.getAnnotation(org.unique.web.annotation.Route.class);
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
				
				//URL参数构建
				viewReg = buildParam(parameters, viewReg);
				
				// 构建一个路由
				Route action = new Route(controller, method, arguments, methodType, viewPath);
				
				if (null != urlMapping.get(viewReg)) {
					warnning(controller, method, "route \"" + viewPath + "\"重复");
				}
				urlMapping.put(viewReg, action);
				logger.info("route:" + viewPath);
			}
		}
	}
	
	/**
	 * 构建参数
	 * @param parameters	参数列表
	 * @param viewReg		url正则
	 * @return
	 */
	private String buildParam(Parameter[] parameters, String viewReg){
		if (parameters.length > 0 && viewReg.indexOf(":") != -1) {
			// 遍历方法内的参数
			for (int i = 0,len=parameters.length; i < len; i++) {
				// 如果有url path参数
				PathParam pathParam = parameters[i].getDeclaredAnnotation(PathParam.class);
				if (null != pathParam) {
					// 存在该参数
					if(parameters[i].getType().equals(Integer.class)){
						viewReg = viewReg.replaceFirst("/(:\\w+)", "/(\\\\d+)");
					}
					if(parameters[i].getType().equals(String.class)){
						viewReg = viewReg.replaceFirst("/(:[\u4e00-\u9fa5_a-zA-Z0-9]+)", "/([\u4e00-\u9fa5_a-zA-Z0-9]+)");
					}
				}
			}
		}
		//return "^" + viewReg + "$";
		return viewReg;
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
	 * @param action	方法注解的value 
	 * @return 			boolean
	 */
	private boolean isLegalAction(final String action){
		String regex = "^([\\w-_#&/:=.;%?])+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(action);
		return m.find();
	}

	/**
	 * 判断是否是一个合法的action请求
	 * @param method		方法
	 * @return				true：合法 false：不合法
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
	 * @param url		请求的url
	 * @return Action	对象
	 */
	public Route getRoute(String targetPath) {
		Route action = urlMapping.get(targetPath);
		if (null == action) {
			String indexTarget = targetPath.endsWith("/") ? targetPath + "index" : targetPath + "/index";
			action = urlMapping.get(indexTarget);
			if (null == action) {
				Set<String> mappings = urlMapping.keySet();
				for (String mapping : mappings) {
					Pattern p = Pattern.compile("^" + mapping + "$");
					Matcher m = p.matcher(targetPath);
					if (m.find()) {
						action = urlMapping.get(mapping);
						Object[] args = action.getParameters();
						for (int i = 0; i < args.length; i++) {
							String param = targetPath.replaceFirst(mapping, "$" + (i + 1));
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
	 * @param param		字符串参数
	 * @return Object	对象
	 */
	private Object parseObject(final String param) {
		if (StringUtils.isNumeric(param)) {
			return Integer.valueOf(param);
		}
		return param;
	}

}