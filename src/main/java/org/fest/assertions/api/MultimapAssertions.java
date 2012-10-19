package org.fest.assertions.api;

import com.google.common.collect.Multimap;

/**
 * 
 * @author @marcelfalliere
 * @author @miralak
 * 
 */
public class MultimapAssertions extends Assertions {

	public static <K, V> MultimapAssert<K, V> assertThat(Multimap<K, V> actual) {
		return new MultimapAssert<K, V>(actual);
	}

}
