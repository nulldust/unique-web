package org.unique.commons.io.read;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class AbstractClassReader implements ClassReader {

	@Override
	public List<Class<?>> getClass(String packageName, boolean recursive) {
		return null;
	}

	@Override
	public List<Class<?>> getClass(String packageName, Class<?> parent, boolean recursive) {
		
		return null;
	}

	@Override
	public List<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation, boolean recursive) {
		return null;
	}

	@Override
	public List<Class<?>> getClassByAnnotation(String packageName, Class<?> parent, Class<? extends Annotation> annotation,
			boolean recursive) {
		return null;
	}

}
