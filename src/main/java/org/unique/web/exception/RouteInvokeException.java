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
package org.unique.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ActionInvoke异常类
 * @author biezhi
 * @since 1.0
 */
public class RouteInvokeException extends RuntimeException {

	private static final long serialVersionUID = 1998063243843477017L;

	private Logger logger = LoggerFactory.getLogger(RouteInvokeException.class);
	
	public RouteInvokeException() {
		throw new IllegalArgumentException("URL参数类型不匹配！");
	}

	public RouteInvokeException(String msg, Exception e) {
		logger.error(msg);
		e.printStackTrace();
	}
	
	public RouteInvokeException(Exception e) {
		super(e);
		e.printStackTrace();
	}

}