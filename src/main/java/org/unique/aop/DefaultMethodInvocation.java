package org.unique.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import org.unique.aop.annotation.After;
import org.unique.aop.annotation.Before;
import org.unique.aop.intercept.MethodInvocation;

/**
 * 默认的方法执行器实现
 * @author biezhi
 * @since 1.0
 */
public class DefaultMethodInvocation implements MethodInvocation {
	
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
				System.out.println(e.getMessage());
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