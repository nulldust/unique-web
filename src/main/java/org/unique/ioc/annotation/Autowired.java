package org.unique.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动注入接口
 * @author biezhi
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
	
	/**
	 * 要注入的类类型
	 * @return class
	 */
    Class<?> value() default Class.class;

}
