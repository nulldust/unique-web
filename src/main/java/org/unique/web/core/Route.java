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

import org.unique.web.annotation.Route.HttpMethod;

/**
 * 代表请求的Route类
 * @author biezhi
 * @since 1.0
 */
public final class Route {

	private final Class<?> controllerClass;
	private final Object[] parameters;
	private final Method method;
	private final HttpMethod methodType;
	private final String viewPath;
	
	/**
	 * 初始化Route
	 * @param controllerClass 控制器class
	 * @param method 要执行的方法
	 * @param parameters 执行方法中的参数列表
	 * @param methodType 请求类型
	 * @param viewPath 返回的视图位置
	 */
	public Route(Class<?> controllerClass, Method method, Object[] parameters, HttpMethod methodType,
			String viewPath) {
		this.controllerClass = controllerClass;
		this.parameters = parameters;
		this.method = method;
		this.methodType = methodType;
		this.viewPath = viewPath;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public Method getMethod() {
		return method;
	}

	public HttpMethod getMethodType() {
		return methodType;
	}

	public String getViewPath() {
		return viewPath;
	}

	public Object[] getParameters() {
		return parameters;
	}

}