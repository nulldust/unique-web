package org.unique.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.Const;
import org.unique.web.annotation.Path.HttpMethod;
import org.unique.web.core.Route;
import org.unique.web.core.RouteInvocation;
import org.unique.web.core.RouteMapping;
import org.unique.web.exception.RouteException;
import org.unique.web.render.Render;
import org.unique.web.render.RenderFactory;

/**
 * 默认的Handler实现
 * @author biezhi
 * @since 1.0
 */
public class DefalutHandler implements Handler {

	protected final RouteMapping actionMapping = RouteMapping.single();

    protected static final RenderFactory renderFactory = RenderFactory.single();
    
    private Logger logger = LoggerFactory.getLogger(DefalutHandler.class);
    
    private static Render render;
    
    @Deprecated
    public DefalutHandler() {
    }
    
    public static DefalutHandler create(){
    	return new DefalutHandler();
    }

    public String filterStatic(String target){
    	// 伪静态
		if(target.endsWith(Const.ROUTE_SUFFIX)){
			target = target.substring(0, target.length() - Const.ROUTE_SUFFIX.length());
		} else{
			// 不处理静态资源
			if (target.indexOf(".") != -1) {
	            return null;
	        }
		}
		return target;
    } 
    
    private boolean exec(String target, HttpServletRequest request, HttpServletResponse response){
    	// 获取路由
    	Route route = actionMapping.getRoute(target);
        
        if (route == null) {
            String qs = request.getQueryString();
            logger.warn("404 Action Not Found: " + (qs == null ? target : target + "?" + qs));
            renderFactory.getErrorRender(404).render(request, response, null);
            return true;
        }
        try {
            // 验证是否
            if (!verifyMethod(route.getMethodType(), request.getMethod())) {
                logger.warn("404 Error request method");
                renderFactory.getErrorRender(404).render(request, response, null);
                return true;
            }
            String nameSpace = route.getControllerClass().getAnnotation(org.unique.web.annotation.Controller.class).value();
            Object result = new RouteInvocation(route, nameSpace).proceed();
            if(null != result && result instanceof String){
            	nameSpace = nameSpace.startsWith("/") ? nameSpace.substring(1) : nameSpace;
            	String viewPath = nameSpace + result;
            	if(null == render){
            		render = renderFactory.getDefaultRender();
            	}
            	render.render(request, response, viewPath);
            }
            return true;
        } catch (RouteException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 404) {
                String qs = request.getQueryString();
                logger.warn("404 Not Found: " + (qs == null ? target : target + "?" + qs));
            } else if (errorCode == 401) {
                String qs = request.getQueryString();
                logger.warn("401 Unauthorized: " + (qs == null ? target : target + "?" + qs));
            } else if (errorCode == 403) {
                String qs = request.getQueryString();
                logger.warn("403 Forbidden: " + (qs == null ? target : target + "?" + qs));
            }
            e.getErrorRender().render(request, response, null);
        } catch (Exception e) {
            logger.warn("Exception: " + e.getMessage());
            renderFactory.getErrorRender(500).render(request, response, null);
        }
        return false;
    }
    
    public boolean handle(String target, HttpServletRequest request, HttpServletResponse response) {
    	target = target.replaceAll("(//)+", "/");
    	
    	if(!target.equals("/")){
    		target = target.endsWith("/") ? target.substring(0, target.length() - 1) : target;
    	}
    	target = filterStatic(target);
    	if(null != target){
    		logger.info("reuqest:[" + target + "]");
            return exec(target, request, response);
		}
        return false;
    }

    /**
     * 验证请求方法
     * @param methodType 请求类型
     * @param method 方法
     * @return 验证成功/失败
     */
    protected boolean verifyMethod(HttpMethod methodType, String method) {
        if (null == methodType || methodType == HttpMethod.ALL) {
            return true;
        }
        if (methodType == HttpMethod.GET) {
            return method.trim().equals(HttpMethod.GET.toString());
        }
        if (methodType == HttpMethod.POST) {
            return method.trim().equals(HttpMethod.POST.toString());
        }
        if (methodType == HttpMethod.PUT) {
            return method.trim().equals(HttpMethod.PUT.toString());
        }
        if (methodType == HttpMethod.DELETE) {
            return method.trim().equals(HttpMethod.DELETE.toString());
        }
        return false;
    }

}
