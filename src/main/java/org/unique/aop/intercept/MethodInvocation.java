package org.unique.aop.intercept;

import java.lang.reflect.Method;

/**
 * 方法执行器
 * @author biezhi
 * @since 1.0
 */
public interface MethodInvocation extends Invocation {

	/**
	 * 获取方法
	 * @return
	 */
	Method getMethod();
	
}