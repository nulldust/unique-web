package org.unique.support;

/**
 * 增强顶层接口
 * @author biezhi
 * @since 1.0
 */
public abstract class Support {
	
	/**
	 * 增强器状态（1：启用，0：未启用）
	 */
	protected int status = 0;
	
	/**
	 * 启动增强
	 */
	public abstract void startup();
	
	/**
	 * 停止增强
	 */
	public abstract void shutdown();
}
