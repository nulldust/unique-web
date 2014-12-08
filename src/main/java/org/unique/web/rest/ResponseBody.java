package org.unique.web.rest;

/**
 * 自定义返回
 * 
 * @author biezhi
 * @since 1.0
 */
public class ResponseBody {

	private String data;
	private String error;
	private String message;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
