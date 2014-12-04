package org.unique.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 后置执行注解
 * @author biezhi
 * @since 1.0
 */
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface After{
	
    String expression() default "";
}