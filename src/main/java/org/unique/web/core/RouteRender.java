package org.unique.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.web.render.Render;

/**
 * 请求渲染器
 * @author　biezhi
 * @since　1.0
 */
public final class RouteRender implements Render {
	
	private String actionUrl;
	
	public RouteRender(String actionUrl) {
		this.actionUrl = actionUrl.trim();
	}
	
	public String getActionUrl() {
		return actionUrl;
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
	}
	
}
