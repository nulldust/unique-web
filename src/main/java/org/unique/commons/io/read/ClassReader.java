package org.unique.commons.io.read;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClassReader {

	public Set<Class<?>> getClass(String packageName, boolean recursive);
	
	public Set<Class<?>> getClass(String packageName, Class<?> parent, boolean recursive);
	
	public Set<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation, boolean recursive);
	
	public Set<Class<?>> getClassByAnnotation(String packageName, Class<?> parent, Class<? extends Annotation> annotation, boolean recursive);
	
}
