package org.unique.aop;

import org.unique.aop.intercept.MethodInterceptor;
import org.unique.aop.intercept.MethodInvocation;

/**
 * 抽象方法拦截器
 * @author biezhi
 * @since 1.0
 */
public abstract class AbstractMethodInterceptor implements MethodInterceptor {
	
	/**
	 * 执行方法
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		return result;
	}

	/**
	 * 前置执行
	 */
	protected abstract void beforeAdvice();

	/**
	 * 后置执行
	 */
	protected abstract void afterAdvice();
	
}
