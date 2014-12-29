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
package org.unique.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.unique.aop.annotation.After;
import org.unique.aop.annotation.Before;
import org.unique.aop.intercept.MethodInvocation;

/**
 * 默认的方法执行器实现
 * @author biezhi
 * @since 1.0
 */
public class DefaultMethodInvocation implements MethodInvocation {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMethodInvocation.class);
	
	List<AbstractMethodInterceptor> interceptors;
	private DefaultProxy proxy;
	private Method method;
	private Object target;
	private Object[] args;
	int index = 0;
	private boolean executed = false;

	public DefaultMethodInvocation(DefaultProxy proxy, Method method, Object target, Object[] args, List<AbstractMethodInterceptor> interceptorChain) {
		this.interceptors = interceptorChain;
		this.method = method;
		this.target = target;
		this.args = args;
		this.proxy = proxy;
	}

	public Object proceed() throws Exception {
		AbstractMethodInterceptor interceptor = null;
		Object result = null;
		if (interceptors.size() > 0 && index < interceptors.size()) {
			interceptor = interceptors.get(index++);
			if (new AdviceMatcher(interceptor, this).match(Before.class, "beforeAdvice")) {
				interceptor.beforeAdvice(); //     执行前置建议
			}
			proceed(); //    执行下一个拦截器
		}
		// 执行真正的方法调用
		if (!executed) {
			executed = true;
			try {
				result = method.invoke(target, args);
			} catch (RuntimeException e) {
				LOGGER.error(e.getMessage());
			}
		}
		if (index > 0) {
			interceptor = interceptors.get(--index);
			if (new AdviceMatcher(interceptor, this).match(After.class, "afterAdvice")) {
				interceptor.afterAdvice(); //     执行后置建议
			}
		}
		return result;
	}

	public Object getThis() {
		return target;
	}

	public AccessibleObject getStaticPart() {
		return null;
	}

	public Method getMethod() {
		return method;
	}

	public DefaultProxy getProxy() {
		return proxy;
	}

	public Object[] getArguments() {
		return args;
	}
}