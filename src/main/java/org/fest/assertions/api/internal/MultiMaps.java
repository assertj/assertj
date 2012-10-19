package org.fest.assertions.api.internal;

import static org.fest.assertions.error.ShouldContainKey.shouldContainKey;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Failures;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;

/**
 * 
 * @author @marcelfalliere
 * @author @miralak
 * 
 */
public class MultiMaps {

	private static MultiMaps INSTANCE = new MultiMaps();

	public static MultiMaps instance() {
		return INSTANCE;
	}

	@VisibleForTesting
	Failures failures = Failures.instance();

	@VisibleForTesting
	MultiMaps() {
	}

	public <T> void assertContainsKey(AssertionInfo info,
			Multimap<T, ?> actual, T key) {
		if (!actual.containsKey(key)) {
			throw failures.failure(info, shouldContainKey(actual, key));
		}
	}

}
