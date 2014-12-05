package org.unique.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ActionInvoke异常类
 * @author biezhi
 * @since 1.0
 */
public class RouteInvokeException extends RuntimeException {

	private static final long serialVersionUID = 1998063243843477017L;

	private Logger logger = LoggerFactory.getLogger(RouteInvokeException.class);
	
	public RouteInvokeException() {
		throw new IllegalArgumentException("URL参数类型不匹配！");
	}

	public RouteInvokeException(String msg, Exception e) {
		logger.error(msg);
		e.printStackTrace();
	}
	
	public RouteInvokeException(Exception e) {
		super(e);
		e.printStackTrace();
	}

}
