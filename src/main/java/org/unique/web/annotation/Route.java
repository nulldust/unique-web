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
package org.unique.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法上定义的路由注解
 * 

Restful routes:
<pre>
==========================================================================================
verb    path                   action          used for
==========================================================================================
GET     /books                 index 	       display a list of all books
GET     /books/new_form        new_form        return an HTML form for creating a new book
POST    /books                 create 	       create a new book
GET     /books/id              show            display a specific book
GET     /books/id/edit_form    edit_form       return an HTML form for editing a books
PUT     /books/id              update          update a specific book
DELETE 	/books/id              destroy         delete a specific book
</pre>

 * @author biezhi
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
	
	public enum HttpMethod{ 
	    ALL, GET, POST, PUT, DELETE
	};
	
	public enum ResponseType{ 
	    XML, JSON, HTML
	};
	
	/**
	 * 请求url
	 * @return 请求url
	 */
	String value() default "default";
	
	/**
	 * 请求类型 HttpMethod
	 * @return 请求类型
	 */
	HttpMethod method() default HttpMethod.ALL;
	
	ResponseType response() default ResponseType.HTML;
	
}