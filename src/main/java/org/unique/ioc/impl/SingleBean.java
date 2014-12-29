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