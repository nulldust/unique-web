package org.unique.aop.intercept;

/**
 * 方法拦截器
 * @author biezhi
 * @since 1.0
 */
public interface MethodInterceptor extends Interceptor{

	/**
	 * 执行方法
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable;
	
}
