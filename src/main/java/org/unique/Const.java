package org.unique;

import java.util.Map;

/**
 * 常量类
 * 
 * @author biezhi
 * @since　1.0
 */
public final class Const{
	
	/**
	 * 系统默认配置文件名称
	 */
	public static final String DEFAULT_CONFIG = "unique-default.properties";
	
	/**
	 * 用户自定义默认配置文件名称
	 */
	public static String CUSTOM_CONFIG = "unique-config.properties";
	
	/**
	 * 增强类包名称
	 */
	public static final String SUPPORT_PACKAGE = "org.unique.support";
	
	/**
	 * 默认的web server端口
	 */
	public static final int DEFAULT_PORT = 8080;
	
	/**
	 * 系统配置常量map
	 */
	private static Map<String, String> configMap;

	/**
	 * 默认的渲染类型JSP
	 */
	public static String RENDER_TYPE = "jsp";
	
	/**
	 * 默认文件编码
	 */
	public static String ENCODING = "UTF-8";
	
	private Const() {
	}
	
	public static void putAllConst(final Map<String, String> configMap_){
		if(null != configMap_ && !configMap_.isEmpty()){
			configMap = configMap_;
		}
	}
	
	/**
	 * 获取配置map
	 * @return 	配置map
	 */
	public static Map<String, String> getConfigMap(){
		return configMap;
	}
	
	/**
	 * 根据key获取配置value
	 * @param key 	配置文件的key
	 * @return 		配置对应的value
	 */
	public static String getConfig(final String key){
		if(configMap.containsKey(key)){
			return configMap.get(key);
		}
		return "";
	}
}
