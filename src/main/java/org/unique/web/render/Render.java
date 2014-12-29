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
package org.unique.web.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;

/**
 * 顶层渲染器接口
 * @author biezhi
 * @since 1.0
 */
public interface Render {

	/**
	 * 文件后缀
	 */
	public static final String suffix = Const.getConfig("unique.view.suffix");
	
    /**
     * 渲染视图方法
     * @param request 请求对象
     * @param response 响应对象
     * @param viewPath 视图位置
     * @throws Exception 
     */
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath);
	
}