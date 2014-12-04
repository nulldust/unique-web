package org.unique.web.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;

/**
 * 顶层渲染器接口
 * @author biezhi
 * @since 1.0
 */
public interface Render {

	/**
	 * 文件后缀
	 */
	public static final String suffix = Const.getConfig("unique.view.suffix");
	
    /**
     * 渲染视图方法
     * @param request 请求对象
     * @param response 响应对象
     * @param viewPath 视图位置
     */
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath);
	
}