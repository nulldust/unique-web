package org.unique.tools;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.unique.web.core.RouteContext;

/**
 * web工具类
 * @author biezhi
 * @since 1.0
 */
public final class WebUtil {

    private static String webRootPath;

    private static String rootClassPath;

    private static final String SLASH = "/";

    private WebUtil() {
    }
    
    /**
     * 获取class的物理路径
     * @param clazz class对象
     * @return string
     */
    public static String getPath(Class<?> clazz) {
        String path = clazz.getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    /**
     * 获取一个资源的物理路径
     * @param object 资源对象
     * @return string
     */
    public static String getPath(Object object) {
        String path = object.getClass().getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    /**
     * 获取class根路径
     * @return string
     */
    public static String getRootClassPath() {
        if (rootClassPath == null) {
            try {
                String path = WebUtil.class.getClassLoader().getResource("").toURI().getPath();
                rootClassPath = new File(path).getAbsolutePath();
            } catch (Exception e) {
                String path = WebUtil.class.getClassLoader().getResource("").getPath();
                rootClassPath = new File(path).getAbsolutePath();
            }
        }
        return rootClassPath;
    }

    /**
     * 设置web根路径
     * @param webRootPath webroot根
     */
    public static void setWebRootPath(String webRootPath) {
        if (webRootPath == null) return;
        if (webRootPath.endsWith(File.separator)) webRootPath = webRootPath.substring(0, webRootPath.length() - 1);
        WebUtil.webRootPath = webRootPath;
    }
    
    /**
     * 获取包所在路径
     * @param object 包名
     * @return string
     */
    public static String getPackagePath(Object object) {
        Package p = object.getClass().getPackage();
        return p != null ? p.getName().replaceAll("\\.", "/") : "";
    }
    
    public static File getFileFromJar(String file) {
        throw new RuntimeException("Not finish. Do not use this method.");
    }

    public static String getWebRootPath() {
        if (webRootPath == null) webRootPath = detectWebRootPath();
        ;
        return webRootPath;
    }
    
    private static String detectWebRootPath() {
        try {
            String path = WebUtil.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取相对路径
     * @param request 请求对象
     * @param filterPath 文件路径
     * @return string
     */
    public static String getRelativePath(HttpServletRequest request, String filterPath) {
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
    
    public static Object getApplicationAttr(final String name){
		ServletContext context = RouteContext.getServletContext();
		if(null != context){
			return context.getAttribute(name);
		}
		return null;
	}
	
	public static void setApplicationAttr(final String name, final Object object){
		ServletContext context = RouteContext.getServletContext();
		if(null != context){
			context.setAttribute(name, object);
		}
	}
	
	public static void setSession(final String name, final Object object){
		HttpSession session = RouteContext.getHttpServletRequest().getSession();
		if(null != session){
			session.setAttribute(name, object);
		}
	}
	
	public static Object getSession(final String name){
		HttpSession session = RouteContext.getHttpServletRequest().getSession();
		if(null != session){
			return session.getAttribute(name);
		}
		return null;
	}
	
	public static void setAttr(final String name, final Object object){
		HttpServletRequest request = RouteContext.getHttpServletRequest();
		if(null != request){
			request.setAttribute(name, object);
		}
	}
	
	public static Object getAttr(final String name){
		HttpServletRequest request = RouteContext.getHttpServletRequest();
		if(null != request){
			return request.getAttribute(name);
		}
		return null;
	}
	
	public static String getParam(final String name){
		HttpServletRequest request = RouteContext.getHttpServletRequest();
		if(null != request){
			return request.getParameter(name);
		}
		return null;
	}
}
