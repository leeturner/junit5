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

import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.junit.platform.engine.support.discovery.SelectorResolver.Resolution.selectors;
import static org.junit.platform.engine.support.discovery.SelectorResolver.Resolution.unresolved;

import java.util.Set;

import org.apiguardian.api.API;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.UniqueId;

/**
 * @since 1.4
 */
@API(status = EXPERIMENTAL, since = "1.4")
public abstract class ConvertingSelectorResolver implements SelectorResolver {

	@Override
	public Resolution resolveSelector(DiscoverySelector selector, Context context) {
		Set<? extends DiscoverySelector> selectors = convert(selector);
		if (selectors.isEmpty()) {
			return unresolved();
		}
		return selectors(selectors);
	}

	protected abstract Set<? extends DiscoverySelector> convert(DiscoverySelector selector);

	@Override
	public Resolution resolveUniqueId(UniqueId uniqueId, Context context) {
		return unresolved();
	}
}
