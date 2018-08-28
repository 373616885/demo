package com.jdk.proxy;

public class RealSubject implements Subject {

	@Override
	public void say() {
		System.out.println("RealSubject: say hello");
	}

}
