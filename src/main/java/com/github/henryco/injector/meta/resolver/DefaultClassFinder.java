package com.github.henryco.injector.meta.resolver;

import java.util.ArrayList;

public class DefaultClassFinder implements IClassFinder {

	@Override
	public ArrayList<Class<?>> getClassesForPackage(String pckgname) throws ClassNotFoundException {
		return StaticClassFinder.getClassesForPackage(pckgname);
	}
}