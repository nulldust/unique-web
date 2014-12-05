package org.unique.ioc;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 容器顶层接口
 * @author biezhi
 * @since 1.0
 */
public interface Container {

    Object getBean(String name, Scope scope);

    Object getBean(Class<?> type, Scope scope);

    Set<String> getBeanNames();
    
    Collection<Object> getBeans();
    
    List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation);
    
    List<Object> getBeansByAnnotation(Class<? extends Annotation> annotation);
    
    boolean hasBean(Class<?> clazz);

    boolean hasBean(String name);
    
    boolean removeBean(String name);
    
    boolean removeBean(Class<?> clazz);
    
    boolean isRegister(Annotation[] annotations);

    Object registBean(Class<?> clazz);
    
    void registBean(Set<Class<?>> classes);

    void initWired();

    Map<String, Object> getBeanMap();
    
}
