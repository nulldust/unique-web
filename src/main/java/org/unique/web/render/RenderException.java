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

/**
 * 渲染器异常
 * @author biezhi
 * @version 1.0
 */
public class RenderException extends RuntimeException {
	
	private static final long serialVersionUID = -71028494021202138L;

	public RenderException() {
		super();
	}

	public RenderException(String message) {
		super(message);
	}

	public RenderException(Throwable cause) {
		super(cause);
	}

	public RenderException(String message, Throwable cause) {
		super(message, cause);
	}

}