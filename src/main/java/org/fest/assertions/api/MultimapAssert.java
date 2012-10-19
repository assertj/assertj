package org.fest.assertions.api;

import org.fest.assertions.api.internal.MultiMaps;

import com.google.common.collect.Multimap;

/**
 * 
 * @author @marcelfalliere
 * @author @miralak
 * 
 */
public class MultimapAssert<K, V> extends
		AbstractAssert<MultimapAssert<K, V>, Multimap<K, V>> {

	MultiMaps multimaps = MultiMaps.instance();

	protected MultimapAssert(Multimap<K, V> actual) {
		super(actual, MultimapAssert.class);
	}

	public MultimapAssert<K, V> containsKey(K key) {
		multimaps.assertContainsKey(info, actual, key);

		return myself;
	}

}
