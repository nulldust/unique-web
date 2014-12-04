package org.unique.aop.intercept;

/**
 * 切入点
 * @author biezhi
 * @since 1.0
 */
public interface Joinpoint {

	Object proceed() throws Throwable;
	
}
