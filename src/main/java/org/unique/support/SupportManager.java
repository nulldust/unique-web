package org.unique.support;

import java.util.List;
import java.util.Map;

import org.unique.commons.tools.CollectionUtil;


/**
 * 增强管理器
 * @author biezhi
 * @since 1.0
 */
public final class SupportManager {
	
	private static final Map<String, Support> supportMap = CollectionUtil.newHashMap();
	
	/**
	 * 获取所有增强器
	 * @return 增强器map
	 */
	public static Map<String, Support> getSupportMap(){
		return supportMap;
	}
	
	/**
	 * 获取所有增强器
	 * @return 增强器列表
	 */
	public static List<Support> getSupportList(){
		return (List<Support>) supportMap.values();
	}
	
	/**
	 * 获取一个增强器
	 * @param key 增强器的名称（类完整名称）
	 * @return 增强器
	 */
	public static Support getSupport(final String key){
		return supportMap.get(key);
	}
	
	/**
	 * 添加一个增强器
	 * @param key 增强器全名称
	 * @param support 增强器实例
	 */
	public static void put(final String key, final Support support){
		supportMap.put(key, support);
	}
	
	/**
	 * 添加一个增强器
	 * @param key 增强器class
	 * @param support 增强器实例
	 */
	public static void put(final Class<? extends Support> key, final Support support){
		supportMap.put(key.getName(), support);
	}
	
	/**
	 * 移除一个增强
	 * @param key 要移除的增强器的名称
	 */
	public static void remove(final String key){
		Support support = getSupport(key);
		if(null != support){
			support.shutdown();
			supportMap.remove(key);
		}
	}
	
	/**
	 * 移除一个增强
	 * @param key 要移除的增强器的class
	 */
	public static void remove(final Class<? extends Support> key){
		Support support = getSupport(key.getName());
		if(null != support){
			support.shutdown();
			supportMap.remove(key.getName());
		}
	}
	
	/**
	 * 禁用增强器
	 * @param key
	 */
	public static void shutdown(final String key){
		Support support = getSupport(key);
		if(null != support){
			support.shutdown();
		}
	}
	
	/**
	 * 禁用增强器
	 * @param supportClass 增强器class对象
	 */
	public static void shutdown(final Class<? extends Support> supportClass){
		Support support = getSupport(supportClass.getName());
		if(null != support){
			support.shutdown();
		}
	}
	
	/**
	 * 启动增强器
	 * @param key
	 */
	public static void start(final String key){
		Support support = getSupport(key);
		if(null != support){
			if(support.status == 0){
				support.startup();
			}
		}
	}
	
	/**
	 * 启动增强器
	 * @param supportClass 增强器class对象
	 */
	public static void start(final Class<? extends Support> supportClass){
		Support support = getSupport(supportClass.getName());
		if(null != support){
			if(support.status == 0){
				support.startup();
			}
		}
	}
	
	/**
	 * 重新启用增强器
	 * @param key
	 */
	public static void restart(final String key){
		Support support = getSupport(key);
		if(null != support){
			if(support.status == 0){
				support.startup();
			}
			if(support.status == 1){
				support.shutdown();
				support.startup();
			}
		}
	}
	
	/**
	 * 重新启用增强器
	 * @param supportClass 增强器class对象
	 */
	public static void restart(final Class<? extends Support> supportClass){
		Support support = getSupport(supportClass.getName());
		if(null != support){
			if(support.status == 0){
				support.startup();
			}
			if(support.status == 1){
				support.shutdown();
				support.startup();
			}
		}
	}
}
