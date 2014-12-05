package org.unique.aop;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.unique.commons.tools.CollectionUtil;

/**
 * 默认代理获取类
 * @author biezhi
 * @since 1.0
 */
public class DefaultProxy implements MethodInterceptor {

	private Object target;
	private List<AbstractMethodInterceptor> interceptorChain = CollectionUtil.newArrayList();
	
	public DefaultProxy(Object target, List<AbstractMethodInterceptor> interceptorChain) {
		this.target = target;
		if (interceptorChain != null && interceptorChain.size() > 0) {
			this.interceptorChain.addAll(interceptorChain);
		}
	}
	
	public void addInterceptor(AbstractMethodInterceptor abstractMethodInterceptor){
		interceptorChain.add(abstractMethodInterceptor);
	}
	
	public Object getProxy(Object target) {
		this.target = target;
		// cglib 中加强器，用来创建动态代理
		Enhancer enhancer = new Enhancer();
		// 设置要创建动态代理的类
		enhancer.setSuperclass(target.getClass());
		// 设置回调，这里相当于是对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实行intercept()方法进行拦截
		enhancer.setCallback(this);
		Object proxy = enhancer.create();
		return proxy;
	}
	
	public Object getProxy() {
		// cglib 中加强器，用来创建动态代理
		Enhancer enhancer = new Enhancer();
		// 设置要创建动态代理的类
		enhancer.setSuperclass(target.getClass());
		// 设置回调，这里相当于是对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实行intercept()方法进行拦截
		enhancer.setCallback(this);
		Object proxy = enhancer.create();
		return proxy;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		DefaultMethodInvocation methodInvocation = new DefaultMethodInvocation(this, method, target, args, interceptorChain);
		return methodInvocation.proceed();
	}
	
	public Object getTarget() {
		return target;
	}
}