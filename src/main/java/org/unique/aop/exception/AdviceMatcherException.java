package org.unique.aop.exception;

/**
 * 切入点匹配异常
 * @author biezhi
 * @since 1.0
 */
public class AdviceMatcherException extends RuntimeException {

	private static final long serialVersionUID = -5143613620732802399L;

	public AdviceMatcherException() {
		super();
	}

	public AdviceMatcherException(Exception e) {
		super(e);
	}
	
	public AdviceMatcherException(String msg) {
		super(msg);
	}
	
}
