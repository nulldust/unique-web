package org.unique.web.render.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;
import org.unique.web.render.Render;

/**
 * jsp渲染器
 * @author biezhi
 * @since 1.0
 */
public class JspRender implements Render {

	/**
	 * 视图文件位置
	 */
	private static final String prefix = Const.getConfig("unique.view.prefix");
	
	public JspRender() {
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
		try {
			String url = prefix + viewPath + suffix;
			if(viewPath.endsWith(suffix)){
				url = prefix + viewPath;
			}
			url = url.replaceAll("//", "/");
			response.setCharacterEncoding(Const.ENCODING);
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}