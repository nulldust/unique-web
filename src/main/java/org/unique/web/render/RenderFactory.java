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

import org.unique.Const;
import org.unique.web.render.impl.ErrorRender;
import org.unique.web.render.impl.HtmlRender;
import org.unique.web.render.impl.JavascriptRender;
import org.unique.web.render.impl.JsonRender;
import org.unique.web.render.impl.JspRender;
import org.unique.web.render.impl.RedirectRender;
import org.unique.web.render.impl.TextRender;

/**
 * RenderFactory.
 */
public class RenderFactory {

	private Render render;
	
    private RenderFactory() {
    }
    
    public void setRender(Render render){
    	this.render = render;
    }
    
    public static RenderFactory single() {
        return RenderFactoryHoder.instance;
    }

    private static class RenderFactoryHoder {
        private static final RenderFactory instance = new RenderFactory();
    }
    
    public Render getTextRender(String text) {
        return new TextRender(text);
    }

    public Render getTextRender(String text, String contentType) {
        return new TextRender(text, contentType);
    }

    public Render getDefaultRender() {
    	if(null == this.render){
    		this.render = new JspRender();
    	}
        return this.render;
    }
    
    public Render getErrorRender(int errorCode, String view) {
        return new ErrorRender(errorCode, view);
    }

    public Render getErrorRender(int errorCode) {
        return new ErrorRender(errorCode, Const.getConfig("unique.error." + errorCode));
    }

    public Render getRedirectRender(String url) {
        return new RedirectRender(url);
    }

    public Render getRedirectRender(String url, boolean withQueryString) {
        return new RedirectRender(url, withQueryString);
    }

    public Render getJavascriptRender(String jsText) {
        return new JavascriptRender(jsText);
    }
    
    public Render getJsonRender(String jsonText) {
        return new JsonRender(jsonText);
    }

    public Render getJsonRender(String key, String value) {
        return new JsonRender(key, value);
    }
    
    public Render getJsonRender(Object object) {
        return new JsonRender(object);
    }
    
    public Render getJsonRender(String[] attrs) {
        return new JsonRender(attrs);
    }
    
    public Render getHtmlRender(String htmlText) {
        return new HtmlRender(htmlText);
    }

}