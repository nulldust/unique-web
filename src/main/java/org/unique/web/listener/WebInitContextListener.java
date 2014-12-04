package org.unique.web.listener;

import javax.servlet.ServletContext;


/**
 * webContext初始化监听器
 * @author biezhi
 * @since 1.0
 */
public interface WebInitContextListener extends UniqueListener{
	
	/**
	 * context初始化方法
	 * @param servletContext ServletContext对象
	 */
    void contextInit(ServletContext servletContext);
    
}