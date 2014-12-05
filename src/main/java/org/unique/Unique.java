package org.unique;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.aop.AbstractMethodInterceptor;
import org.unique.aop.intercept.AbstractMethodInterceptorFactory;
import org.unique.commons.io.PropUtil;
import org.unique.commons.utils.ClassHelper;
import org.unique.commons.utils.CollectionUtil;
import org.unique.commons.utils.StringUtils;
import org.unique.ioc.AbstractBeanFactory;
import org.unique.ioc.Container;
import org.unique.ioc.annotation.Component;
import org.unique.ioc.impl.DefaultContainerImpl;
import org.unique.ioc.impl.SingleBean;
import org.unique.support.Support;
import org.unique.support.SupportManager;
import org.unique.web.annotation.Controller;
import org.unique.web.core.WebContext;
import org.unique.web.core.RouteMapping;
import org.unique.web.handler.DefalutHandler;
import org.unique.web.handler.Handler;
import org.unique.web.interceptor.AbstractRouteInterceptor;
import org.unique.web.listener.WebInitContextListener;

/**
 * 框架执行流程
 * @author biezhi
 * @since 1.0
 */
public final class Unique {

	private static final Logger LOGGER = LoggerFactory.getLogger(Unique.class);

	private Handler handler;

	/**
	 * web路由映射器
	 */
	private final RouteMapping routeMapping;
	
	/**
	 * IOC容器
	 */
	private final Container container;
	
	/**
	 * Bean工厂
	 */
	private final AbstractBeanFactory beanFactory;
	
	/**
	 * 方法拦截器
	 */
	private final List<AbstractMethodInterceptor> abstractMethodInterceptor;
	
	private Unique() {
		routeMapping = RouteMapping.single();
		container = DefaultContainerImpl.single();
		beanFactory = new SingleBean();
		abstractMethodInterceptor = CollectionUtil.newArrayList(1);
	}
	
	public static Unique single() {
		return SingleHoder.single;
	}

	private static class SingleHoder {
		private static final Unique single = new Unique();
	}
	
	/**
	 * 初始化方法
	 * @return true:初始化成功 false:初始化失败
	 */
	public boolean init(String config_path, String route_suffix) {
		
		if(StringUtils.isNotBlank(route_suffix)){
			Const.ROUTE_SUFFIX = route_suffix;
		}
		
		if(StringUtils.isNotBlank(config_path)){
			Const.CUSTOM_CONFIG = config_path;
		}
		
		// 初始化配置文件
		initConst();
		
		// 初始化IOC容器
		initIOC();
		
		// 初始化RouteMapping
		initRouteMapping();
		
		// 初始化第三方增强
		initSupport();
				
		// 初始化handler
		initHandler();
		
		// 初始化自定义上下文
		initContext();
		return true;
	}
	
	/**
	 * 初始化常量
	 */
	private void initConst(){
		// 加载默认配置文件
		Map<String, String> defaultCfg = PropUtil.getPropertyMap(Const.DEFAULT_CONFIG);
		defaultCfg.putAll(PropUtil.getPropertyMap(Const.CUSTOM_CONFIG));
		Const.putAllConst(defaultCfg);
		defaultCfg = null;
		if(Const.getConfig("unique.encoding").trim().length() > 0){
			Const.ENCODING = Const.getConfig("unique.encoding").trim();
		}
		if(Const.getConfig("unique.view.type").trim().length() > 0){
			Const.RENDER_TYPE = Const.getConfig("unique.view.type").trim();
		}
	}
	/**
	 * 初始化第三方增强
	 */
	private void initSupport() {
		List<Class<?>> supportList = ClassHelper.scanClasses("org.unique.support", Support.class, true);
		if(supportList.size() > 0){
			try {
				for (Class<?> clazz : supportList) {
					Support support = (Support) clazz.newInstance();
					support.startup();
					SupportManager.put(clazz.getName(), support);
				}
			} catch (InstantiationException e) {
				LOGGER.error("初始化增强失败: {}", e.getMessage());
			} catch (IllegalAccessException e) {
				LOGGER.error("初始化增强失败: {}", e.getMessage());
			}
		}
	}

	/**
	 * 初始化ioc容器
	 */
	private void initIOC() {
		//初始化系统类库
		List<Class<?>> sysClasses = ClassHelper.scanClassesByAnnotation("org.unique.sys", Component.class, false);
		container.registBean(sysClasses);
		//初始化自定义类
		String scanPackage = Const.getConfig("unique.scannpackage");
		if ((scanPackage == null) || (scanPackage.trim().length() == 0)) {
			throw new IllegalArgumentException("扫描的包不能为空!");
		}
		if (scanPackage.indexOf("org.unique") != -1) {
			throw new IllegalArgumentException("要扫描的包名称不能包含\"org.unique\"");
		}
		String[] packages = null;
		if (scanPackage.indexOf(',') == -1) {
			packages = new String[] { scanPackage };
		} else {
			packages = scanPackage.split(",");
		}
		for (String pack : packages) {
			scanPack(pack);
		}
		LOGGER.info("beans : {}", container.getBeanMap());
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		Object obj = beanFactory.getBean(Handler.class.getName());
		if (null != obj && obj instanceof Handler) {
			this.handler = (Handler) obj;
		} else{
			this.handler = DefalutHandler.create();
		}
	}
	
	/**
	 * 初始化actionMapping
	 */
	private void initRouteMapping() {
		routeMapping.buildRouteMapping();
	}
	
	/**
	 * 获取handler
	 * @return Handler	对象
	 */
	public Handler getHandler() {
		if(null != this.handler){
			return this.handler;
		}
		return null;
	}

	/**
	 * 初始化Context
	 */
	private void initContext() {
		Object obj = beanFactory.getBean(WebInitContextListener.class.getName());
		if (null != obj && obj instanceof WebInitContextListener) {
			WebInitContextListener initListener = (WebInitContextListener) obj;
			initListener.contextInit(WebContext.getServletContext());
		}
	}

	/**
	 * 扫描包
	 * @param pack 要扫描的包名
	 */
	private void scanPack(String pack) {
		if (pack.endsWith(".*")) {
			pack = pack.substring(0, pack.length() - 2);
		}
		Set<Class<?>> classes = ClassHelper.scanPackage(pack, true);
		for (Class<?> clazz : classes) {
			
			// 是一个控制器，为他添加系统的路由拦截器
			if(null != clazz.getAnnotation(Controller.class)){
				container.registBean(clazz);
				continue;
			}
			
			if(null != clazz.getSuperclass()){
				// 是一个路由拦截器
				if(clazz.getSuperclass().equals(AbstractRouteInterceptor.class)){
					addRouteInterceptor(clazz);
					continue;
				}
				// 是一个普通拦截器
				if(clazz.getSuperclass().equals(AbstractMethodInterceptor.class)){
					addInterceptor(clazz);
					continue;
				}
			}
			// 注册带有Component和Service注解的类
			if (container.isRegister(clazz.getAnnotations())) {
				container.registBean(clazz);
			}
		}
		// 初始化注入
		container.initWired();
	}
	
	/**
	 * 添加路由拦截器
	 * @param clazz
	 */
	private void addRouteInterceptor(Class<?> clazz) {
		Object object;
		try {
			object = clazz.newInstance();
			if(object instanceof AbstractRouteInterceptor){
				// 如果是路由拦截器 则添加到拦截器工厂中
				AbstractRouteInterceptor abstractRouteInterceptor = (AbstractRouteInterceptor) object;
				AbstractMethodInterceptorFactory.addRouteInterceptor(abstractRouteInterceptor);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加连接器到拦截器链
	 * @param clazz
	 */
	private void addInterceptor(Class<?> clazz){
		Object object;
		try {
			object = clazz.newInstance();
			if(object instanceof AbstractMethodInterceptor){
				// 将普通拦截器添加到拦截器工厂
				abstractMethodInterceptor.add( (AbstractMethodInterceptor) object);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
