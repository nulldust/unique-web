package org.unique.ioc.impl;

import org.unique.ioc.AbstractBeanFactory;
import org.unique.ioc.Scope;

public class SingleBean extends AbstractBeanFactory {

	@Override
	public Object getBean(String className) {
		return container.getBean(className, Scope.SINGLE);
	}

	@Override
	public Object getBean(Class<?> clazz) {
		return container.getBean(clazz, Scope.SINGLE);
	}
	
}
