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