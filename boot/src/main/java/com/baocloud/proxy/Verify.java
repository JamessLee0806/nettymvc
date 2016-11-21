package com.baocloud.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Verify {
	public static void main(String[] args) {
		ProxyInterface proxy=new ProxyImpl();
		InvocationHandler invo=new Invocation(proxy);
		ProxyInterface obj =(ProxyInterface) Proxy.newProxyInstance(Verify.class.getClassLoader(), new Class[] { ProxyInterface.class },invo);
		System.out.println(obj.method1());
		System.err.println(obj.method2());
	}

}
