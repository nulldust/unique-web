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
package org.unique.web.rest;

import org.unique.web.exception.HttpException;

public class ERROR {
	
	private HttpException exception;
	private String error;
	private Integer error_code;
	private String request;

	public enum ErrorCode {

		//1:开头的是系统错误
	    ERROR10000("配置信息错误！"),
		ERROR10001("系统错误！"), //		
		ERROR10016("参数不能为空！"), //	
		ERROR10017("参数类型不正确！"), //	
		ERROR10021("请求方式错误（post/get）!"), //
		ERROR10022("传递参数格式不正确!"), //
		ERROR50102("");
		
		private String code;
		ErrorCode(String code) {
			this.code = code;
		}

		public String toString() {
			return String.valueOf(code);
		}

	}
	
	public HttpException getException() {
		return exception;
	}

	public void setException(HttpException exception) {
		this.exception = exception;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
}