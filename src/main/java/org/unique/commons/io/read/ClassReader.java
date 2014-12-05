package org.unique.commons.io.read;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ClassReader {

	public List<Class<?>> getClass(String packageName, boolean recursive);
	
	public List<Class<?>> getClass(String packageName, Class<?> parent, boolean recursive);
	
	public List<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation, boolean recursive);
	
	public List<Class<?>> getClassByAnnotation(String packageName, Class<?> parent, Class<? extends Annotation> annotation, boolean recursive);
	
}
