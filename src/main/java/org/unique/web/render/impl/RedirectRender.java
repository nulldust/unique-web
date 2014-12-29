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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unique.web.render.Render;

/**
 * RedirectRender with status: 302 Found.
 */
public class RedirectRender implements Render {

    private String url;

    private boolean withQueryString;

    public RedirectRender(String url) {
        this.url = url;
        this.withQueryString = false;
    }

    public RedirectRender(String url, boolean withQueryString) {
        this.url = url;
        this.withQueryString = withQueryString;
    }

    public void render(HttpServletRequest request, HttpServletResponse response, String viewPath) {
        if (request.getContextPath() != null && url.indexOf("://") == -1) url = request.getContextPath() + url;

        if (withQueryString) {
            String queryString = request.getQueryString();
            if (queryString != null) if (url.indexOf("?") == -1)
                url = url + "?" + queryString;
            else
                url = url + "&" + queryString;
        }
        try {
            response.sendRedirect(url); // always 302
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}