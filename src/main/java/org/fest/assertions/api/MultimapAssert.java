package org.fest.assertions.api;

import static org.fest.assertions.error.ShouldContainKey.shouldContainKey;

import com.google.common.collect.Multimap;

import org.fest.assertions.internal.Failures;

/**
 * 
 * @author @marcelfalliere
 * @author @miralak
 * @author Joel Costigliola
 * 
 */
public class MultimapAssert<K, V> extends AbstractAssert<MultimapAssert<K, V>, Multimap<K, V>> {

  Failures failures = Failures.instance();

  protected MultimapAssert(Multimap<K, V> actual) {
    super(actual, MultimapAssert.class);
  }

  public MultimapAssert<K, V> containsKey(K key) {
    if (!actual.containsKey(key)) {
      throw failures.failure(info, shouldContainKey(actual, key));
    }

    return myself;
  }

}
