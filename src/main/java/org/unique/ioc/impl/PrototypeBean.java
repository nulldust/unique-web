package org.unique.ioc.impl;

import org.unique.ioc.AbstractBeanFactory;
import org.unique.ioc.Scope;

public class PrototypeBean extends AbstractBeanFactory {

	@Override
	public Object getBean(String className) {
		return container.getBean(className, Scope.PROTOTYPE);
	}

	@Override
	public Object getBean(Class<?> clazz) {
		return container.getBean(clazz, Scope.PROTOTYPE);
	}

}
