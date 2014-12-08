package org.unique.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法上定义的路由注解
 * @author biezhi
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Path {
	
	public enum HttpMethod{ 
	    ALL, GET, POST, PUT, PATCH, DELETE, HEAD, TRACE, CONNECT, OPTIONS, BEFORE, AFTER
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
	
}
