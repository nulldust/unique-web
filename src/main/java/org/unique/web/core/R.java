package org.unique.web.core;

import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unique.commons.utils.StringUtils;
import org.unique.web.annotation.Controller;
import org.unique.web.exception.RouteException;
import org.unique.web.render.Render;
import org.unique.web.render.RenderFactory;
import org.unique.web.rest.ResponseBody;

/**
 * 处理http请求的工具类
 * @author biezhi
 * @since 1.0
 */
public final class R {
	
	private static HttpServletRequest request;
	private static HttpServletResponse response;
	private static Set<String> headers = null;
	private static Render defaultRender;
	private static String viewPath;
	private static final RenderFactory renderFactory = RenderFactory.single();

	private R() {
	}
	
	public static void put(HttpServletRequest request_, HttpServletResponse response_){
		request = request_;
		response = response_;
	}
	
	/**
	 * 返回pathinfo
	 * @return path info Example return: "/example/foo"
	 */
	public static String pathInfo() {
		return request.getPathInfo();
	}

	/**
	 * 返回servletPath
	 * @return the servlet path
	 */
	public static String servletPath() {
		return request.getServletPath();
	}

	/**
	 * 返回contextPath
	 * @return the context path
	 */
	public static String contextPath() {
		return request.getContextPath();
	}

	/**
	 * 根据header获取请求头信息
	 * 
	 * @param header 请求头的键
	 * @return
	 */
	public static String headers(String header) {
		return request.getHeader(header);
	}

	/**
	 * 所有请求头
	 * 
	 * @return all headers
	 */
	public static Set<String> headers() {
		if (headers == null) {
			headers = new TreeSet<String>();
			Enumeration<String> enumeration = request.getHeaderNames();
			while (enumeration.hasMoreElements()) {
				headers.add(enumeration.nextElement());
			}
		}
		return headers;
	}

	public static String queryString() {
		return request.getQueryString();
	}

	/**
	 * 获取string类型请求参数
	 * 
	 * @param name 参数名
	 * @return 参数值
	 */
	public static String getPara(String name) {
		return request.getParameter(name);
	}

	/**
	 * 获取string请求参数，如果没有给一个默认值
	 * 
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数值
	 */
	public static String getPara(String name, String defaultValue) {
		String result = request.getParameter(name);
		return result != null && !"".equals(result) ? result : defaultValue;
	}
	
	public static Map<String, String[]> getParaMap() {
		return request.getParameterMap();
	}

	public static Enumeration<String> getParaNames() {
		return request.getParameterNames();
	}

	public static String[] getParaValues(String name) {
		return request.getParameterValues(name);
	}

	public static Integer[] getParaValuesToInt(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null)
			return null;
		Integer[] result = new Integer[values.length];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.parseInt(values[i]);
		return result;
	}

	public static Enumeration<String> getAttrNames() {
		return request.getAttributeNames();
	}

	public static void setAttr(String attribute, Object value) {
		request.setAttribute(attribute, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getAttr(String name) {
		Object attr = request.getAttribute(name);
		if (null == attr) {
			return null;
		}
		return (T) request.getAttribute(name);
	}

	public static String getAttrForStr(String name) {
		return (String) request.getAttribute(name);
	}

	public static Integer getAttrForInt(String name) {
		return (Integer) request.getAttribute(name);
	}

	private static Integer toInt(String value, Integer defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		if (value.startsWith("N") || value.startsWith("n"))
			return -Integer.parseInt(value.substring(1));
		return Integer.parseInt(value);
	}

	public static Integer getParaToInt(String name) {
		return toInt(request.getParameter(name), null);
	}

	public static Integer getParaToInt(String name, Integer defaultValue) {
		return toInt(request.getParameter(name), defaultValue);
	}

	private static Long toLong(String value, Long defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		if (value.startsWith("N") || value.startsWith("n"))
			return -Long.parseLong(value.substring(1));
		return Long.parseLong(value);
	}

	public static Long getParaToLong(String name) {
		return toLong(request.getParameter(name), null);
	}

	public static Long getParaToLong(String name, Long defaultValue) {
		return toLong(request.getParameter(name), defaultValue);
	}

	private static Boolean toBoolean(String value, Boolean defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		value = value.trim().toLowerCase();
		if ("1".equals(value) || "true".equals(value)) {
			return Boolean.TRUE;
		} else if ("0".equals(value) || "false".equals(value)) {
			return Boolean.FALSE;
		}
		throw new RuntimeException("Can not parse the parameter \"" + value + "\" to boolean value.");
	}

	public static Boolean getParaToBoolean(String name) {
		return toBoolean(request.getParameter(name), null);
	}

	public static Boolean getParaToBoolean(String name, Boolean defaultValue) {
		return toBoolean(request.getParameter(name), defaultValue);
	}

	private static Date toDate(String value, Date defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		try {
			return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date getParaToDate(String name) {
		return toDate(request.getParameter(name), null);
	}

	public static Date getParaToDate(String name, Date defaultValue) {
		return toDate(request.getParameter(name), defaultValue);
	}

	public static HttpServletRequest getRequest() {
		return request;
	}

	public static HttpServletResponse getResponse() {
		return response;
	}

	public static HttpSession getSession() {
		return request.getSession();
	}

	public static HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttr(String key) {
		HttpSession session = request.getSession(false);
		return session != null ? (T) session.getAttribute(key) : null;
	}

	public static void setSessionAttr(String key, Object value) {
		request.getSession().setAttribute(key, value);
	}

	public static void removeSessionAttr(String key) {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.removeAttribute(key);
	}

	public static String getCookie(String name, String defaultValue) {
		Cookie cookie = getCookieObject(name);
		return cookie != null ? cookie.getValue() : defaultValue;
	}

	public static String getCookie(String name) {
		return getCookie(name, null);
	}

	public static Integer getCookieToInt(String name) {
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : null;
	}

	public static Integer getCookieToInt(String name, Integer defaultValue) {
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : defaultValue;
	}

	public static Long getCookieToLong(String name) {
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : null;
	}

	public static Long getCookieToLong(String name, Long defaultValue) {
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : defaultValue;
	}

	public static Cookie getCookieObject(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}

	public static Cookie[] getCookieObjects() {
		Cookie[] result = request.getCookies();
		return result != null ? result : new Cookie[0];
	}

	public static void setCookie(Cookie cookie) {
		response.addCookie(cookie);
	}

	public static void setCookie(String name, String value, int maxAgeInSeconds, String path) {
		setCookie(name, value, maxAgeInSeconds, path, null);
	}

	public static void setCookie(String name, String value, int maxAgeInSeconds, String path, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (domain != null)
			cookie.setDomain(domain);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	public static void setCookie(String name, String value, int maxAgeInSeconds) {
		setCookie(name, value, maxAgeInSeconds, "/", null);
	}

	public static void removeCookie(String name) {
		setCookie(name, null, 0, "/", null);
	}

	public static void removeCookie(String name, String path) {
		setCookie(name, null, 0, path, null);
	}

	/**
	 * 移除cookie
	 * @param name cookie名称
	 * @param path cookie path
	 * @param domain cookie所在域
	 * @return R对象
	 */
	public static void removeCookie(String name, String path, String domain) {
		setCookie(name, null, 0, path, domain);
	}

	public static void render(final Render render) {
		render.render(request, response, viewPath);
	}
	
	public static void render(final String view) {
		renderFactory.getDefaultRender().render(request, response, view);
	}
	
	public static void render(Object object, final String view) {
		if(null == object.getClass().getAnnotation(Controller.class)){
			throw new RouteException("the class is not a controller.");
		}
		String nameSpace = object.getClass().getAnnotation(Controller.class).value();
		if(StringUtils.isNotBlank(nameSpace)){
			nameSpace = nameSpace.equals("/") ? nameSpace : nameSpace + "/";
		}
		renderFactory.getDefaultRender().render(request, response, nameSpace + view);
	}

	public static void renderText(final String text) {
		renderFactory.getTextRender(text).render(request, response, viewPath);
	}

	public static void renderText(final String text, final String contentType) {
		renderFactory.getTextRender(text, contentType).render(request, response, viewPath);
	}

	public static void renderJS(final String jsText) {
		renderFactory.getJavascriptRender(jsText).render(request, response, viewPath);
	}
	
	public static void renderJSON(final String jsonText) {
		renderFactory.getJsonRender(jsonText);
	}
	
	public static void renderJSON(final String key, final String value) {
		renderFactory.getJsonRender(key, value);
	}
	
	public static void renderJSON(final Object object) {
		renderFactory.getJsonRender(object);
	}
	
	public static void renderJSON(final String[] attrs) {
		renderFactory.getJsonRender(attrs);
	}
	
	public static void renderHtml(final String htmlText) {
		renderFactory.getHtmlRender(htmlText).render(request, response, viewPath);
	}

	public static void redirect(final String url) {
		renderFactory.getRedirectRender(url).render(request, response, viewPath);
	}

	public static void redirect(Object object, final String url) {
		if(null == object.getClass().getAnnotation(Controller.class)){
			throw new RouteException("the class is not a controller.");
		}
		String nameSpace = object.getClass().getAnnotation(Controller.class).value();
		if(StringUtils.isNotBlank(nameSpace)){
			nameSpace = nameSpace.equals("/") ? nameSpace : nameSpace + "/";
		}
		renderFactory.getRedirectRender(url).render(request, response, nameSpace);
	}
	
	public static Render getRender() {
		return defaultRender;
	}

	public static ResponseBody response(Object controller, Object data) {
		Controller controllerAnno = controller.getClass().getAnnotation(Controller.class);
		if(null != controllerAnno && null != data){
			
		}
		return null;
	}
	
}
