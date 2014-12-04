package org.unique.web.render;

/**
 * 渲染器异常
 * @author biezhi
 * @version 1.0
 */
public class RenderException extends RuntimeException {
	
	private static final long serialVersionUID = -71028494021202138L;

	public RenderException() {
		super();
	}

	public RenderException(String message) {
		super(message);
	}

	public RenderException(Throwable cause) {
		super(cause);
	}

	public RenderException(String message, Throwable cause) {
		super(message, cause);
	}

}
