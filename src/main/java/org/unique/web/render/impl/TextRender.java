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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.Const;
import org.unique.commons.io.IOUtil;
import org.unique.web.render.Render;

/**
 * TextRender.
 */
public class TextRender implements Render {
	
	private static final String defaultContentType = "text/plain;charset=" + Const.ENCODING;
	private String text;
	
	public TextRender(String text) {
		this.text = text;
	}
	
	private String contentType;
	public TextRender(String text, String contentType) {
		this.text = text;
		this.contentType = contentType;
	}
	
	public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);
	        
	        if (contentType == null) {
	        	response.setContentType(defaultContentType);
	        }
	        else {
	        	response.setContentType(contentType);
				response.setCharacterEncoding(Const.ENCODING);
	        }
	        
	        writer = response.getWriter();
	        writer.write(text);
	        writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			IOUtil.closeQuietly(writer);
		}
	}
}