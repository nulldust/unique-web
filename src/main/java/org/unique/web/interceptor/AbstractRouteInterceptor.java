package org.unique.web.interceptor;

import org.unique.aop.AbstractMethodInterceptor;
import org.unique.aop.intercept.MethodInvocation;

/**
 * 抽象路由拦截器
 * @author biezhi
 * @since 1.0
 */
public abstract class AbstractRouteInterceptor extends AbstractMethodInterceptor {

	@Override
	public void beforeAdvice() {
	}

	@Override
	public void afterAdvice() {
	}

	@Override
	public abstract Object invoke(MethodInvocation invocation) throws Throwable;
}
