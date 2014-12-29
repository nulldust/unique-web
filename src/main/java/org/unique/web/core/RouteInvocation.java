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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.unique.aop.AbstractMethodInterceptor;
import org.unique.aop.intercept.AbstractMethodInterceptorFactory;
import org.unique.aop.intercept.MethodInvocation;
import org.unique.ioc.AbstractBeanFactory;
import org.unique.ioc.Container;
import org.unique.ioc.annotation.Autowired;
import org.unique.ioc.impl.DefaultContainerImpl;
import org.unique.ioc.impl.SingleBean;
import org.unique.web.exception.RouteIntercepterException;
import org.unique.web.exception.RouteInvokeException;
import org.unique.web.interceptor.AbstractRouteInterceptor;

/**
 * RouteInvocation
 * @author biezhi
 * @since 1.0
 */
public class RouteInvocation implements MethodInvocation {

	private final static AbstractBeanFactory beanFactory = new SingleBean();

	private String nameSpace;
	
	private Method method;
	
	private Class<?> controllerClass;
	
	private Route route;

	private Object[] arguments;

	private Object result = null;
	
	int index = 0;
	
	private boolean executed = false;
	
	private final static Container container = DefaultContainerImpl.single();

	private final List<AbstractRouteInterceptor> interceptorList = AbstractMethodInterceptorFactory.getAbstractRouteInterceptors();

	public RouteInvocation(Route route, String nameSpace) {
		this.route = route;
		this.nameSpace = nameSpace;
		this.arguments = route.getParameters();
		this.method = route.getMethod();
		this.controllerClass = route.getControllerClass();
		R.put(WebContext.getHttpServletRequest(), WebContext.getHttpServletResponse());
	}
	
	@Override
	public Object proceed() {
		AbstractMethodInterceptor interceptor = null;
		if (interceptorList.size() > 0 && index < interceptorList.size()) {
			interceptor = interceptorList.get(index++);
			try {
				result = interceptor.invoke(this);
			} catch (Throwable e) {
				throw new RouteIntercepterException(e);
			}
			this.proceed();
			//执行下一个拦截器
		}
		// 执行真正的方法调用
		if (!executed && null == result) {
			executed = true;
			Object controller = beanFactory.getBean(controllerClass);
			controller = newInstance(controller);
			try {
				result = method.invoke(controller, arguments);
			} catch (IllegalAccessException e) {
				throw new RouteInvokeException(e);
			} catch (IllegalArgumentException e) {
				throw new RouteInvokeException();
			} catch (InvocationTargetException e) {
				throw new RouteInvokeException("传递参数异常", e);
			}
		}
		return result;
	}

	public Object newInstance(Class<?> clazz){
	    Object obj = null;
		try {
			obj = clazz.newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				Autowired autowired = field.getAnnotation(Autowired.class);
				if (null != autowired) {
					// 要注入的字段
					Object wiredField = beanFactory.getBean(field.getType());
					// 指定装配的类
					if (autowired.value() != Class.class) {
						wiredField = beanFactory.getBean(autowired.value());
						// 容器有该类
						if (null == wiredField) {
							wiredField = container.registBean(autowired.value());
						}
					} else {
						// 容器有该类
						if (null == wiredField) {
							wiredField = container.registBean(autowired.value());
						}
					}
					if (null == wiredField) {
						throw new RuntimeException("Unable to load " + field.getType().getCanonicalName() + "！");
					}
					boolean accessible = field.isAccessible();
					field.setAccessible(true);
					field.set(obj, wiredField);
					field.setAccessible(accessible);
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 创建一个新的实例
	 * @param clazz 要被修改的class
	 * @return 修改后的实例
	 */
	private Object newInstance(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				Autowired autowired = field.getAnnotation(Autowired.class);
				if (null != autowired) {
					// 要注入的字段
					Object wiredField = beanFactory.getBean(field.getType());
					// 指定装配的类
					if (autowired.value() != Class.class) {
						wiredField = beanFactory.getBean(autowired.value());
						// 容器有该类
						if (null == wiredField) {
							wiredField = container.registBean(autowired.value());
						}
					} else {
						// 容器有该类
						if (null == wiredField) {
							wiredField = container.registBean(autowired.value());
						}
					}
					if (null == wiredField) {
						throw new RuntimeException("Unable to load " + field.getType().getCanonicalName() + "！");
					}
					boolean accessible = field.isAccessible();
					field.setAccessible(true);
					field.set(obj, wiredField);
					field.setAccessible(accessible);
				}
			}
			return obj;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Route getRoute() {
		return route;
	}
	
	public String getNameSpace(){
		return nameSpace;
	}
	
	public Object getResult() {
		return result;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public Method getMethod() {
		return method;
	}

}