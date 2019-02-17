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

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.ClasspathResourceSelector;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.DirectorySelector;
import org.junit.platform.engine.discovery.FileSelector;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.discovery.ModuleSelector;
import org.junit.platform.engine.discovery.PackageSelector;
import org.junit.platform.engine.discovery.UniqueIdSelector;
import org.junit.platform.engine.discovery.UriSelector;

/**
 * @since 1.5
 */
@API(status = EXPERIMENTAL, since = "1.5")
public interface SelectorResolver {

	default Resolution resolve(ClasspathResourceSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(ClasspathRootSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(ClassSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(DirectorySelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(FileSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(MethodSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(ModuleSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(PackageSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(UniqueIdSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(UriSelector selector, Context context) {
		return resolve((DiscoverySelector) selector, context);
	}

	default Resolution resolve(DiscoverySelector selector, Context context) {
		return Resolution.unresolved();
	}

	/**
	 * @since 1.5
	 */
	@API(status = EXPERIMENTAL, since = "1.5")
	interface Context {
		Optional<TestDescriptor> resolve(DiscoverySelector selector);

		<T extends TestDescriptor> Optional<T> addToParent(Function<TestDescriptor, Optional<T>> creator);

		<T extends TestDescriptor> Optional<T> addToParent(Supplier<DiscoverySelector> parentSelectorSupplier,
				Function<TestDescriptor, Optional<T>> creator);
	}

	/**
	 * @since 1.5
	 */
	@API(status = EXPERIMENTAL, since = "1.5")
	class Resolution {

		private static final Resolution UNRESOLVED = new Resolution(emptySet(), emptySet());

		private final Set<Match> matches;
		private final Set<? extends DiscoverySelector> additionalSelectors;

		public static Resolution unresolved() {
			return UNRESOLVED;
		}

		public static Resolution match(Match match) {
			return new Resolution(singleton(match), emptySet());
		}

		public static Resolution matches(Set<Match> matches) {
			Preconditions.containsNoNullElements(matches, "matches must not contain null elements");
			Preconditions.notEmpty(matches, "matches must not be empty");
			return new Resolution(matches, emptySet());
		}

		public static Resolution selectors(Set<? extends DiscoverySelector> selectors) {
			Preconditions.containsNoNullElements(selectors, "selectors must not contain null elements");
			Preconditions.notEmpty(selectors, "selectors must not be empty");
			return new Resolution(emptySet(), selectors);
		}

		private Resolution(Set<Match> matches, Set<? extends DiscoverySelector> additionalSelectors) {
			this.matches = matches;
			this.additionalSelectors = additionalSelectors;
		}

		public boolean isResolved() {
			return this != UNRESOLVED;
		}

		public Set<Match> getMatches() {
			return matches;
		}

		public Set<? extends DiscoverySelector> getAdditionalSelectors() {
			return additionalSelectors;
		}
	}

	/**
	 * @since 1.5
	 */
	@API(status = EXPERIMENTAL, since = "1.5")
	class Match {
		private final TestDescriptor testDescriptor;
		private final Supplier<Set<? extends DiscoverySelector>> childSelectorsSupplier;
		private final Type type;

		public static Match exact(TestDescriptor testDescriptor) {
			return exact(testDescriptor, Collections::emptySet);
		}

		public static Match exact(TestDescriptor testDescriptor,
				Supplier<Set<? extends DiscoverySelector>> childSelectorsSupplier) {
			return new Match(testDescriptor, childSelectorsSupplier, Type.EXACT);
		}

		public static Match partial(TestDescriptor testDescriptor) {
			return partial(testDescriptor, Collections::emptySet);
		}

		public static Match partial(TestDescriptor testDescriptor,
				Supplier<Set<? extends DiscoverySelector>> childSelectorsSupplier) {
			return new Match(testDescriptor, childSelectorsSupplier, Type.PARTIAL);
		}

		private Match(TestDescriptor testDescriptor, Supplier<Set<? extends DiscoverySelector>> childSelectorsSupplier,
				Type type) {
			this.testDescriptor = testDescriptor;
			this.childSelectorsSupplier = childSelectorsSupplier;
			this.type = type;
		}

		public boolean isExact() {
			return type == Type.EXACT;
		}

		public TestDescriptor getTestDescriptor() {
			return testDescriptor;
		}

		public Set<? extends DiscoverySelector> expand() {
			return childSelectorsSupplier.get();
		}

		private enum Type {
			EXACT, PARTIAL
		}
	}

}
