package org.unique.aop.intercept;

/**
 * 执行器接口
 * @author biezhi
 * @since 1.0
 */
public interface Invocation extends Joinpoint {

	Object[] getArguments();
	
}
