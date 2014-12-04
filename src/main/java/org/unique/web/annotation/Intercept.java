package org.unique.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 拦截器注解
 * @author biezhi
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Intercept {
	
	/**
	 * 拦截器的加载顺序，从0开始
	 * @return int
	 */
	int loadOnStartup() default 0;
}

