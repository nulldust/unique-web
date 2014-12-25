package org.unique.web.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.Unique;
import org.unique.web.handler.Handler;

/**
 * mvc核心过滤器
 * @author biezhi
 * @since 1.0
 */
public class RouteFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(RouteFilter.class);

	private static final String SLASH = "/";
	
	private static Unique unique = Unique.single();

	private static boolean isInit = false;

	private static Handler handler;

	/**
	 * 初始化项目
	 * @param config 初始化参数对象
	 */
	public void init(FilterConfig config) {
		long start = System.currentTimeMillis();
		String config_path = config.getInitParameter("config_path");
		String route_suffix = config.getInitParameter("route_suffix");
		if (!isInit) {
			logger.info("Root WebApplicationContext: 初始化开始...");
			
			// config path
			WebContext.setActionContext(config.getServletContext());
			
			// init web
			isInit = unique.init(config_path, route_suffix);
			
			handler = unique.getHandler();
			long time = (System.currentTimeMillis() - start);
			logger.info("Root WebApplicationContext: 初始化耗费时长：" + time + " ms");
		}
	}

	/**
	 * 执行过滤器
	 * @param req 请求对象
	 * @param res 响应对象
	 * @param chain filterchain
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		// 相对于系统根目录的访问的路径
		String target = getRelativePath(request, "");

		// set reqest and response
		WebContext.setActionContext(request.getServletContext(), request, response);
		if (!handler.handle(target, request, response)) {
			chain.doFilter(request, response);
		}
	}
	
	/**
	 * 获取请求url的相对路径
	 * @param request
	 * @param filterPath
	 * @return
	 */
	public static final String getRelativePath(HttpServletRequest request, String filterPath) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        path = path.substring(contextPath.length());
        
        if (path.length() > 0) {
            path = path.substring(1);
        }
        
        if (!path.startsWith(filterPath) && filterPath.equals(path + SLASH)) {
            path += SLASH;
        }
        if (path.startsWith(filterPath)) {
            path = path.substring(filterPath.length());
        }

        if (!path.startsWith(SLASH)) {
            path = SLASH + path;
        }

        return path;
    }
	
	/**
	 * 销毁方法
	 */
	public void destroy() {
		if (null != unique) {
			unique = null;
		}
		if (null != handler) {
			handler = null;
		}
	}

}
