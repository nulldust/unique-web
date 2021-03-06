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
package org.unique.ioc.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.aop.AbstractMethodInterceptor;
import org.unique.aop.ProxyBeanFactory;
import org.unique.aop.intercept.AbstractMethodInterceptorFactory;
import org.unique.commons.utils.CloneUtil;
import org.unique.commons.utils.CollectionUtil;
import org.unique.ioc.Container;
import org.unique.ioc.Scope;
import org.unique.ioc.annotation.Autowired;
import org.unique.ioc.annotation.Component;
import org.unique.ioc.annotation.Service;

/**
 * 默认的IOC容器实现
 * @author biezhi
 * @since 1.0
 */
public class DefaultContainerImpl implements Container {

    private static final Logger logger = LoggerFactory.getLogger(DefaultContainerImpl.class);

    /**
     * 保存所有bean对象
     */
    private static final Map<String, Object> beansMap = CollectionUtil.newConcurrentHashMap();
    
    /**
     * 保存所有注解的class
     */
    private static final Map<Class<? extends Annotation>, List<Object>> annotationMap = CollectionUtil.newConcurrentHashMap();
    
    private DefaultContainerImpl() {
    }
    
    public static DefaultContainerImpl single() {
        return DefaultContainerHoder.single;
    }
    
    private static class DefaultContainerHoder {
        private static final DefaultContainerImpl single = new DefaultContainerImpl();
    }

    public Map<String, Object> getBeanMap() {
        return beansMap;
    }
    
    @Override
    public Object getBean(String name, Scope scope) {
    	Object obj = beansMap.get(name);
    	if(null != scope && scope == Scope.PROTOTYPE){
    		try {
				return CloneUtil.deepClone(obj);
			} catch (Exception e) {
				logger.error("克隆对象失败," + e.getMessage());
			}
    	}
        return obj;
    }

    @Override
    public Object getBean(Class<?> type, Scope scope) {
        Iterator<Object> it = beansMap.values().iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (type.isAssignableFrom(obj.getClass())) {
            	if(null != scope && scope == Scope.PROTOTYPE){
            		try {
						return CloneUtil.deepClone(obj);
					} catch (Exception e) {
						logger.error("克隆对象失败," + e.getMessage());
					}
            	} else {
            		return obj;
				}
            }
        }
        return null;
    }

    @Override
    public Set<String> getBeanNames() {
        return beansMap.keySet();
    }
    
    @Override
    public Collection<Object> getBeans() {
        return beansMap.values();
    }

    @Override
    public boolean hasBean(Class<?> clz) {
        if (null != this.getBean(clz, Scope.SINGLE)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasBean(String name) {
        if (null != this.getBean(name, Scope.SINGLE)) {
            return true;
        }
        return false;
    }
    
    @Override
	public boolean removeBean(String name) {
    	Object object = beansMap.remove(name);
		return (null != object);
	}

	@Override
	public boolean removeBean(Class<?> clazz) {
		Object object = beansMap.remove(clazz.getName());
		return (null != object);
	}

    /**
     * 注册一个bean对象到容器里
     * @param clazz 要注册的class
     */
    @Override
    public Object registBean(Class<?> clazz) {
        String name = clazz.getCanonicalName();
        Object object = null;
		//非抽象类or接口
		if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
		    logger.debug("to load the class：" + name);
		    object = newInstance(clazz);
		    put(name, object);
		    //实现的接口对应存储
		    if(clazz.getInterfaces().length > 0){
		    	put(clazz.getInterfaces()[0].getCanonicalName(), object);
		    }
		    //带有annotation
		    if(null != clazz.getDeclaredAnnotations()){
		    	putAnnotationMap(clazz, object);
		    }
		}
		return object;
	}
    
    /**
     * bean容器存储
     * @param name
     * @param object
     */
    private void put(String name, Object object){
    	if(null == beansMap.get(name)){
    		beansMap.put(name, object);
    	}
    }
    
    /**
     * 给annotationMap添加元素
     * @param annotations
     * @param obj
     */
    private void putAnnotationMap(Class<?> clazz, Object object){
    	Annotation[] annotations = clazz.getAnnotations();
    	for(Annotation annotation : annotations){
    		if(null != annotation){
    			List<Object> listObject = annotationMap.get(annotation.annotationType());
    			if(CollectionUtil.isEmpty(listObject)){
    				listObject = CollectionUtil.newArrayList();
    			}
    			listObject.add(object);
    			put(annotation.annotationType(), listObject);
    		}
    	}
    }
    
    /**
     * annotationBean容器存储
     * @param clazz
     * @param listObject
     */
    private void put(Class<? extends Annotation> clazz, List<Object> listObject){
    	if(null == annotationMap.get(clazz)){
    		annotationMap.put(clazz, listObject);
    	}
    }
    
    /**
     * 创建一个实例对象
     * @param clazz class对象
     * @return
     */
    private Object newInstance(Class<?> clazz){
    	try {
    		Object obj = clazz.newInstance();
    		// 查询拦截器个数
    		List<AbstractMethodInterceptor> interceptorChain = AbstractMethodInterceptorFactory.getAbstractInterceptors();
    		if(interceptorChain.size() > 0){
    			obj = ProxyBeanFactory.single().newProxy(obj, interceptorChain);
    		}
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 初始化注入
     */
    @Override
    public void initWired() {
        Iterator<Object> it = beansMap.values().iterator();
        try {
            while (it.hasNext()) {
                Object obj = it.next();
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if (null != autowired) {
                        // 要注入的字段
                        Object wiredField = this.getBean(field.getType(), Scope.SINGLE);
                        // 指定装配的类
                        if (autowired.value() != Class.class) {
                            wiredField = this.getBean(autowired.value(), Scope.SINGLE);
                            // 容器有该类
                            if (null == wiredField) {
                                wiredField = this.registBean(autowired.value());
                            }
                        } else{
                        	// 容器有该类
                            if (null == wiredField) {
                                wiredField = this.registBean(autowired.value());
                            }
                        }
                        if (null == wiredField) {
                            throw new RuntimeException("Unable to load " + field.getType().getCanonicalName() + "！");
                        }
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        field.set(obj, wiredField);
                        field.setAccessible(accessible);
                    }
                }
            }
        } catch (SecurityException e) {
            logger.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 判断是否是可以注册的bean
     * @param 注解类型
     * @return true:可以注册 false:不可以注册
     */
    @Override
    public boolean isRegister(Annotation[] annotations) {
        if (null == annotations || annotations.length == 0) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if ((annotation instanceof Service) || 
                (annotation instanceof Component) ) {
                return true;
            }
        }
        return false;
    }

	@Override
	public List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
		List<Object> objectList = getBeansByAnnotation(annotation);
		if(!CollectionUtil.isEmpty(objectList)){
			List<Class<?>> classList = CollectionUtil.newArrayList(objectList.size());
			for(Object object : objectList){
				classList.add(object.getClass());
			}
			return classList;
		}
		return null;
	}

	@Override
	public List<Object> getBeansByAnnotation(Class<? extends Annotation> annotation) {
		return annotationMap.get(annotation);
	}
	
	@Override
	public void registBean(Set<Class<?>> classes) {
		if(!CollectionUtil.isEmpty(classes)){
			for(Class<?> clazz : classes){
				this.registBean(clazz);
			}
		}
	}

}