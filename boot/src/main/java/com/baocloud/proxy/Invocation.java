package com.baocloud.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Invocation implements InvocationHandler {
	private Object target;

	public Invocation(Object tag) {
		target = tag;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("before execute " + method.getName());
		Object obj = method.invoke(target, args);
		System.out.println("after execute " + method.getName());
		return obj;
	}

}
