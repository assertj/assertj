/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.api;

import org.assertj.core.data.MapEntry;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Table;
import com.google.common.io.ByteSource;

/**
 * The entry point for all Guava assertions.
 *
 * @author marcelfalliere
 * @author miralak
 * @author Kornel
 * @author Jan Gorman
 * @author Joel Costigliola
 * @author Marcin Kwaczyński
 * @author Max Daniline
 * @author Ilya Koshaleu
 */
public class Assertions implements InstanceOfAssertFactories {

  public static ByteSourceAssert assertThat(final ByteSource actual) {
    return new ByteSourceAssert(actual);
  }

  public static <K, V> MultimapAssert<K, V> assertThat(final Multimap<K, V> actual) {
    return new MultimapAssert<>(actual);
  }

  public static <T> OptionalAssert<T> assertThat(final Optional<T> actual) {
    return new OptionalAssert<>(actual);
  }

  public static <T extends Comparable<T>> RangeAssert<T> assertThat(final Range<T> actual) {
    return new RangeAssert<>(actual);
  }

  public static <K extends Comparable<K>, V> RangeMapAssert<K, V> assertThat(final RangeMap<K, V> actual) {
    return new RangeMapAssert<>(actual);
  }

  public static <T extends Comparable<T>> RangeSetAssert<T> assertThat(final RangeSet<T> actual) {
    return new RangeSetAssert<>(actual);
  }

  public static <R, C, V> TableAssert<R, C, V> assertThat(Table<R, C, V> actual) {
    return new TableAssert<>(actual);
  }

  public static <T> MultisetAssert<T> assertThat(final Multiset<T> actual) {
    return new MultisetAssert<>(actual);
  }

  // ------------------------------------------------------------------------------------------------------
  // Data utility methods : not assertions but here to have a single entry point to all AssertJ Guava features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link MapEntry#entry(Object, Object)} so that Assertions offers a fully featured entry point to all
   * AssertJ Guava features (but you can use {@link MapEntry} if you prefer).
   * <p>
   * Typical usage is to call <code>entry</code> in MultimapAssert <code>contains</code> assertion as shown below :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * actual.putAll(&quot;Lakers&quot;, newArrayList(&quot;Kobe Bryant&quot;, &quot;Magic Johnson&quot;, &quot;Kareem Abdul Jabbar&quot;));
   * actual.putAll(&quot;Spurs&quot;, newArrayList(&quot;Tony Parker&quot;, &quot;Tim Duncan&quot;, &quot;Manu Ginobili&quot;));
   *
   * assertThat(actual).contains(entry(&quot;Lakers&quot;, &quot;Kobe Bryant&quot;), entry(&quot;Spurs&quot;, &quot;Tim Duncan&quot;)); </code></pre>
   *
   * @param <K> the type of the key of this entry.
   * @param <V> the type of the value of this entry.
   * @param key the key of the entry to create.
   * @param value the value of the entry to create.
   *
   * @return the built entry
   */
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return MapEntry.entry(key, value);
  }

  /**
   * protected to avoid direct instantiation but allowing subclassing.
   */
  protected Assertions() {
    // empty
  }
}
