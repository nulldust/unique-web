package org.unique.ioc;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.unique.ioc.impl.DefaultContainerImpl;

public abstract class AbstractBeanFactory {
	
	protected Container container = DefaultContainerImpl.single();
	
	public abstract Object getBean(String className);
	
	public abstract Object getBean(Class<?> clazz);
	
	public boolean setBean(Class<?> clazz, Object object){
		if(null != clazz.getInterfaces()){
			for(Class<?> interface_ : clazz.getInterfaces()){
				this.setBean(interface_, object);
			}
		}
		container.removeBean(clazz);
		container.getBeanMap().put(clazz.getName(), object);
		return true;
	}
	
	public Set<String> getBeanNames(){
		return container.getBeanNames();
	}
	
	public Collection<Object> getBeans(){
		return container.getBeans();
	}
	
	public List<Object> getBeansByAnnotation(Class<? extends Annotation> annotation){
		return container.getBeansByAnnotation(annotation);
	}
	
	public List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation){
		return container.getClassesByAnnotation(annotation);
	}
	
}
