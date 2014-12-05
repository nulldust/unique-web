package org.unique.aop.intercept;

import java.util.List;

import org.unique.aop.AbstractMethodInterceptor;
import org.unique.commons.utils.CollectionUtil;
import org.unique.web.interceptor.AbstractRouteInterceptor;

/**
 * 拦截器方法工厂
 * @author biezhi
 * @since 1.0
 */
public final class AbstractMethodInterceptorFactory {
	
	/**
	 * 普通拦截器列表
	 */
	private static final List<AbstractMethodInterceptor> interceptorList = CollectionUtil.newArrayList();
	
	/**
	 * 路由拦截器列表
	 */
	private static final List<AbstractRouteInterceptor> routeInterceptorList = CollectionUtil.newArrayList();
	
	/**
	 * 获取所有普通拦截器
	 * @return
	 */
	public static List<AbstractMethodInterceptor> getAbstractInterceptors() {
		return interceptorList;
	}

	/**
	 * 添加一个拦截器
	 * @param routeInterceptors
	 */
	public static void addInterceptor(List<AbstractMethodInterceptor> routeInterceptors) {
		interceptorList.addAll(routeInterceptors);
	}
	
	/**
	 * 添加一组拦截器
	 * @param routeInterceptor
	 */
	public static void addInterceptor(AbstractMethodInterceptor routeInterceptor) {
		interceptorList.add(routeInterceptor);
	}
	
	/**
	 * 获取所有路由拦截器
	 * @return
	 */
	public static List<AbstractRouteInterceptor> getAbstractRouteInterceptors() {
		return routeInterceptorList;
	}

	/**
	 * 添加一个路由拦截器
	 * @param routeInterceptors
	 */
	public static void addRouteInterceptor(List<AbstractRouteInterceptor> routeInterceptors) {
		routeInterceptorList.addAll(routeInterceptors);
	}
	
	/**
	 * 添加一组路由拦截器
	 * @param routeInterceptor
	 */
	public static void addRouteInterceptor(AbstractRouteInterceptor routeInterceptor) {
		routeInterceptorList.add(routeInterceptor);
	}
	
}
