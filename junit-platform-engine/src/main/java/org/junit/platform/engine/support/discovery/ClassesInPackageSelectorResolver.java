/*
 * Copyright 2015-2019 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.engine.support.discovery;

import static org.junit.platform.commons.support.ReflectionSupport.findAllClassesInPackage;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasses;
import static org.junit.platform.engine.support.discovery.SelectorResolver.Resolution.selectors;

import java.util.function.Predicate;

import org.junit.platform.engine.discovery.PackageSelector;

/**
 * @since 1.4
 */
class ClassesInPackageSelectorResolver implements SelectorResolver {

	private final Predicate<String> classNameFilter;
	private final Predicate<Class<?>> classFilter;

	ClassesInPackageSelectorResolver(Predicate<String> classNameFilter, Predicate<Class<?>> classFilter) {
		this.classNameFilter = classNameFilter;
		this.classFilter = classFilter;
	}

	@Override
	public Resolution resolve(PackageSelector selector, Context context) {
		return selectors(
			selectClasses(findAllClassesInPackage(selector.getPackageName(), this.classFilter, this.classNameFilter)));
	}

}
