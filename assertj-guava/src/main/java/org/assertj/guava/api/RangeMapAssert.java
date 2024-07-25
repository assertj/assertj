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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.guava.api;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.guava.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.guava.error.ShouldContainValues.shouldContainValues;
import static org.assertj.guava.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.MapEntry;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;

/**
 * Assertions for guava {@link com.google.common.collect.RangeMap}.
 * <p>
 * To create an instance of this class, invoke <code>{@link
 * org.assertj.guava.api.Assertions#assertThat(com.google.common.collect.RangeMap)}</code>
 * <p>
 *
 * @author Marcin Kwaczyński
 *
 * @param <K> the type of keys of the tested RangeMap value
 * @param <V> the type of values of the tested RangeMap value
 */
public class RangeMapAssert<K extends Comparable<K>, V> extends AbstractAssert<RangeMapAssert<K, V>, RangeMap<K, V>> {

  protected RangeMapAssert(final RangeMap<K, V> actual) {
    super(actual, RangeMapAssert.class);
  }

  // visible for test
  protected RangeMap<K, V> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.RangeMap} contains the given keys.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> RangeMap&lt;Integer, String&gt; spectralColors = TreeRangeMap.create();
   *
   * spectralColors.put(Range.closedOpen(380, 450), "violet");
   * spectralColors.put(Range.closedOpen(450, 495), "blue");
   * spectralColors.put(Range.closedOpen(495, 570), "green");
   * spectralColors.put(Range.closedOpen(570, 590), "yellow");
   * spectralColors.put(Range.closedOpen(590, 620), "orange");
   * spectralColors.put(Range.closedOpen(620, 750), "red");
   *
   * assertThat(spectralColors).containsKeys(380, 600, 700);</code></pre>
   *
   * If the <code>keys</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param keys the keys to look for in actual {@link com.google.common.collect.RangeMap}.
   * @return this {@link RangeMapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param keys have been set.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} does not contain the given keys.
   */
  public RangeMapAssert<K, V> containsKeys(@SuppressWarnings("unchecked") K... keys) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(keys == null, "The keys to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(keys.length == 0, "The keys to look for should not be empty");

    Set<K> keysNotFound = newLinkedHashSet();
    for (K key : keys) {
      if (actual.get(key) == null) {
        keysNotFound.add(key);
      }
    }
    if (!keysNotFound.isEmpty()) {
      throw assertionError(shouldContainKeys(actual, keys, keysNotFound));
    }

    return myself;
  }

  /**
   * <p>
   * Verifies that the actual {@link com.google.common.collect.RangeMap} contains the given entries.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> RangeMap&lt;Integer, String&gt; spectralColors = TreeRangeMap.create();
   *
   * spectralColors.put(Range.closedOpen(380, 450), "violet");
   * spectralColors.put(Range.closedOpen(450, 495), "blue");
   * spectralColors.put(Range.closedOpen(495, 570), "green");
   * spectralColors.put(Range.closedOpen(570, 590), "yellow");
   * spectralColors.put(Range.closedOpen(590, 620), "orange");
   * spectralColors.put(Range.closedOpen(620, 750), "red");
   *
   * // entry can be statically imported from {@link org.assertj.guava.data.MapEntry}
   * assertThat(spectralColors).contains(entry("400", "violet"), entry("650", "red"));</code></pre>
   *
   * If the <code>entries</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param entries the entries to look for in actual {@link com.google.common.collect.RangeMap}.
   * @return this {@link RangeMapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param entries have been set.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} does not contain the given entries.
   *
   * @deprecated use {@link #contains(MapEntry...)} instead (same method but using {@link MapEntry org.assertj.core.data.MapEntry} in place of {@link org.assertj.guava.data.MapEntry}.
   */
  @SafeVarargs
  @Deprecated
  public final RangeMapAssert<K, V> contains(org.assertj.guava.data.MapEntry<K, V>... entries) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(entries == null, "The entries to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(entries.length == 0, "The entries to look for should not be empty");

    List<org.assertj.guava.data.MapEntry<K, V>> entriesNotFound = newArrayList();
    for (org.assertj.guava.data.MapEntry<K, V> entry : entries) {
      final V value = actual.get(entry.key);
      if (value == null || !value.equals(entry.value)) {
        entriesNotFound.add(entry);
      }
    }
    if (!entriesNotFound.isEmpty()) {
      throw assertionError(shouldContain(actual, entries, entriesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.RangeMap} contains the given entries.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> RangeMap&lt;Integer, String&gt; spectralColors = TreeRangeMap.create();
   *
   * spectralColors.put(Range.closedOpen(380, 450), "violet");
   * spectralColors.put(Range.closedOpen(450, 495), "blue");
   * spectralColors.put(Range.closedOpen(495, 570), "green");
   * spectralColors.put(Range.closedOpen(570, 590), "yellow");
   * spectralColors.put(Range.closedOpen(590, 620), "orange");
   * spectralColors.put(Range.closedOpen(620, 750), "red");
   *
   * // entry can be statically imported from {@link org.assertj.core.data.MapEntry}
   * assertThat(spectralColors).contains(entry("400", "violet"), entry("650", "red"));</code></pre>
   *
   * If the <code>entries</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param entries the entries to look for in actual {@link com.google.common.collect.RangeMap}.
   * @return this {@link RangeMapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param entries have been set.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} does not contain the given entries.
   */
  @SafeVarargs
  public final RangeMapAssert<K, V> contains(MapEntry<K, V>... entries) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(entries == null, "The entries to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(entries.length == 0, "The entries to look for should not be empty");

    List<MapEntry<K, V>> entriesNotFound = newArrayList();
    for (MapEntry<K, V> entry : entries) {
      final V value = actual.get(entry.key);
      if (value == null || !value.equals(entry.value)) {
        entriesNotFound.add(entry);
      }
    }
    if (!entriesNotFound.isEmpty()) {
      throw assertionError(shouldContain(actual, entries, entriesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.RangeMap} contains the given values.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> RangeMap&lt;Integer, String&gt; spectralColors = TreeRangeMap.create();
   *
   * spectralColors.put(Range.closedOpen(380, 450), "violet");
   * spectralColors.put(Range.closedOpen(450, 495), "blue");
   * spectralColors.put(Range.closedOpen(495, 570), "green");
   * spectralColors.put(Range.closedOpen(570, 590), "yellow");
   * spectralColors.put(Range.closedOpen(590, 620), "orange");
   * spectralColors.put(Range.closedOpen(620, 750), "red");
   *
   * assertThat(actual).containsValues(&quot;violet&quot;, &quot;orange&quot;);</code></pre>
   *
   * If the <code>values</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param values the values to look for in actual {@link com.google.common.collect.RangeMap}.
   * @return this {@link RangeMapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param values have been set.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} does not contain the given values.
   */
  public RangeMapAssert<K, V> containsValues(@SuppressWarnings("unchecked") V... values) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(values == null, "The values to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(values.length == 0, "The values to look for should not be empty");

    final Map<Range<K>, V> mapOfRanges = actual.asMapOfRanges();
    Set<V> valuesNotFound = newLinkedHashSet();
    for (V value : values) {
      if (!mapOfRanges.containsValue(value)) {
        valuesNotFound.add(value);
      }
    }
    if (!valuesNotFound.isEmpty()) {
      throw assertionError(shouldContainValues(actual, values, valuesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.RangeMap} is empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> RangeMap&lt;Integer, String&gt; spectralColors = TreeRangeMap.create();
   *
   * assertThat(actual).isEmpty();</code></pre>
   *
   * @return this {@link RangeMapAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is not empty.
   */
  public RangeMapAssert<K, V> isEmpty() {
    isNotNull();
    if (!actual.asMapOfRanges().isEmpty()) {
      throw assertionError(shouldBeEmpty(actual));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.RangeMap} is not empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> RangeMap&lt;Integer, String&gt; spectralColors = TreeRangeMap.create();
   * spectralColors.put(Range.closedOpen(380, 450), "violet");
   * spectralColors.put(Range.closedOpen(450, 495), "blue");
   * spectralColors.put(Range.closedOpen(495, 570), "green");
   * spectralColors.put(Range.closedOpen(570, 590), "yellow");
   * spectralColors.put(Range.closedOpen(590, 620), "orange");
   * spectralColors.put(Range.closedOpen(620, 750), "red");
   *
   * assertThat(spectralColors).isNotEmpty();</code></pre>
   *
   * @return this {@link RangeMapAssert} for assertions chaining.
   *
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.RangeMap} is empty.
   */
  public RangeMapAssert<K, V> isNotEmpty() {
    isNotNull();
    if (actual.asMapOfRanges().isEmpty()) {
      throw assertionError(shouldNotBeEmpty());
    }
    return myself;
  }
}
