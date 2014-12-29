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
package org.unique.web.render.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;
import org.unique.web.render.Render;

/**
 * jsp渲染器
 * @author biezhi
 * @since 1.0
 */
public class JspRender implements Render {

	/**
	 * 视图文件位置
	 */
	private static final String prefix = Const.getConfig("unique.view.prefix");
	
	public JspRender() {
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
		try {
			String url = prefix + viewPath + suffix;
			if(viewPath.endsWith(suffix)){
				url = prefix + viewPath;
			}
			url = url.replaceAll("//", "/");
			response.setCharacterEncoding(Const.ENCODING);
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}