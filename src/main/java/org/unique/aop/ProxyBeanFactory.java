package org.unique.aop;

import java.util.Arrays;
import java.util.List;

/**
 * 单例的代理工厂
 * @author biezhi
 * @since 1.0
 */
public class ProxyBeanFactory {

	private ProxyBeanFactory() {
	}
	
	public static ProxyBeanFactory single() {
		return SingleHoder.single;
	}

	private static class SingleHoder {

		private static final ProxyBeanFactory single = new ProxyBeanFactory();
	}
	
	/**
	 * 
	 * @param targetClazz 创建指定类target实例
	 * @param chain 系统的拦截器链
	 * @return
	 */
	public Object newProxy(Class<?> targetClazz, List<AbstractMethodInterceptor> chain) {
		Object proxyBean = null;
		try {
			Object target = targetClazz.newInstance();
			proxyBean = new DefaultProxy(target, chain).getProxy();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return proxyBean;
	}
	
	public Object newProxy(Class<?> targetClazz, AbstractMethodInterceptor ... chain) {
		Object proxyBean = null;
		try {
			Object target = targetClazz.newInstance();
			List<AbstractMethodInterceptor> chainList = null;
			if(null != chain && chain.length > 0){
				chainList = Arrays.asList(chain);
			}
			proxyBean = new DefaultProxy(target, chainList).getProxy();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return proxyBean;
	}

	public Object newProxy(Object target,  AbstractMethodInterceptor ... chain) {
		List<AbstractMethodInterceptor> chainList = null;
		if(null != chain && chain.length > 0){
			chainList = Arrays.asList(chain);
		}
		Object proxyBean = new DefaultProxy(target, chainList).getProxy();
		return proxyBean;
	}
	
	public Object newProxy(Object target, List<AbstractMethodInterceptor> chain) {
		Object proxyBean = new DefaultProxy(target, chain).getProxy();
		return proxyBean;
	}
}