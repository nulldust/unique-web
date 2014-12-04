package org.unique.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler
 * @author biezhi
 * @since 1.0
 */
public interface Handler {

	public boolean handle(String target, HttpServletRequest request, HttpServletResponse response);
}
