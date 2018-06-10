package com.github.henryco.injector.meta.resolver;

import java.util.List;

public interface IClassFinder {
	List<Class<?>> getClassesForPackage(String pckgname) throws ClassNotFoundException;
}
