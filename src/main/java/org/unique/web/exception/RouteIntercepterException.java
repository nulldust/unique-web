package org.unique.web.exception;


/**
 * 路由拦截器异常
 * @author biezhi
 * @since 1.0
 */
public class RouteIntercepterException extends RuntimeException{

	private static final long serialVersionUID = 1998063243843477017L;
	
	public RouteIntercepterException() {
		super();
	}
	
	public RouteIntercepterException(String msg) {
		super(msg);
	}
	
	public RouteIntercepterException(Exception e) {
		super(e);
	}
	
	public RouteIntercepterException(Throwable e) {
		super(e);
	}
	
	public RouteIntercepterException(String msg, Exception e) {
		super(msg, e);
	}
	
}
