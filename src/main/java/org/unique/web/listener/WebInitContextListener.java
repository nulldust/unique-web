/**
 * Copyright (c) 2014-2015, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unique.web.listener;

import javax.servlet.ServletContext;


/**
 * webContext初始化监听器
 * @author biezhi
 * @since 1.0
 */
public interface WebInitContextListener extends Listener{
	
	/**
	 * context初始化方法
	 * @param servletContext ServletContext对象
	 */
    void contextInit(ServletContext servletContext);
    
}