package org.unique.web.exception;

import org.unique.commons.utils.StringUtils;
import org.unique.web.render.Render;
import org.unique.web.render.RenderFactory;

/**
 * Action异常类
 * @author biezhi
 * @since 1.0
 */
public class RouteException extends RuntimeException{

	private static final long serialVersionUID = 1998063243843477017L;
	private int errorCode;
	private Render errorRender;
	
	public RouteException() {
		throw new IllegalArgumentException("The parameter errorRender can not be null.");
	}
	
	public RouteException(String msg) {
		throw new RuntimeException(msg);
	}
	
	public RouteException(int errorCode, Render errorRender) {
		if (errorRender == null)
			throw new IllegalArgumentException("The parameter errorRender can not be null.");
		this.errorCode = errorCode;
		this.errorRender = errorRender;
	}
	
	public RouteException(int errorCode, String errorView) {
		if (StringUtils.isBlank(errorView)){
			throw new IllegalArgumentException("The parameter errorView can not be blank.");
		}
		this.errorCode = errorCode;
		this.errorRender = RenderFactory.single().getErrorRender(errorCode, errorView);
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public Render getErrorRender() {
		return errorRender;
	}
}
