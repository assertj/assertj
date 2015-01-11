/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.guava.api;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.guava.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.guava.error.ShouldContainValues.shouldContainValues;
import static org.assertj.guava.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import com.google.common.collect.Multimap;

/**
 * Assertions for guava {@link Multimap}.
 *
 * @author @marcelfalliere
 * @author @miralak
 * @author Joel Costigliola
 */
public class MultimapAssert<K, V> extends AbstractAssert<MultimapAssert<K, V>, Multimap<K, V>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected MultimapAssert(Multimap<K, V> actual) {
    super(actual, MultimapAssert.class);
  }

  // visible for test
  protected Multimap<K, V> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link Multimap} contains the given keys.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * 
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * assertThat(actual).containsKeys(&quot;Lakers&quot;, &quot;Bulls&quot;);
   * </code></pre>
   *
   * If the <code>keys</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param keys the keys to look for in actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param keys have been set.
   * @throws AssertionError           if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError           if the actual {@link Multimap} does not contain the given keys.
   */
  public MultimapAssert<K, V> containsKeys(@SuppressWarnings("unchecked") K... keys) {
    Objects.instance().assertNotNull(info, actual);
    throwIllegalArgumentExceptionIfTrue(keys == null, "The keys to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(keys.length == 0, "The keys to look for should not be empty");

    Set<K> keysNotFound = newLinkedHashSet();
    for (K key : keys) {
      if (!actual.containsKey(key)) {
        keysNotFound.add(key);
      }
    }
    if (!keysNotFound.isEmpty()) {
      throw failures.failure(info, shouldContainKeys(actual, keys, keysNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Multimap} contains the given entries.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * // entry can be statically imported from org.assertj.guava.api.Assertions or org.assertj.guava.data.MapEntry
   * assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
   * </code></pre>
   *
   * If the <code>entries</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param entries the entries to look for in actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param entries have been set.
   * @throws AssertionError           if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError           if the actual {@link Multimap} does not contain the given entries.
   */
  @SafeVarargs
  public final MultimapAssert<K, V> contains(MapEntry... entries) {
    Objects.instance().assertNotNull(info, actual);
    throwIllegalArgumentExceptionIfTrue(entries == null, "The entries to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(entries.length == 0, "The entries to look for should not be empty");

    List<MapEntry> entriesNotFound = newArrayList();
    for (MapEntry entry : entries) {
      if (!actual.containsEntry(entry.key, entry.value)) {
        entriesNotFound.add(entry);
      }
    }
    if (!entriesNotFound.isEmpty()) {
      throw failures.failure(info, shouldContain(actual, entries, entriesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Multimap} contains the given values for any key.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * // note that given values are not linked to same key
   * assertThat(actual).containsValues(&quot;Kobe Bryant&quot;, &quot;Michael Jordan&quot;);
   * </code></pre>
   *
   * If the <code>values</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   *
   * @param values the values to look for in actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws IllegalArgumentException if no param values have been set.
   * @throws AssertionError           if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError           if the actual {@link Multimap} does not contain the given values.
   */
  public MultimapAssert<K, V> containsValues(@SuppressWarnings("unchecked") V... values) {
    Objects.instance().assertNotNull(info, actual);
    throwIllegalArgumentExceptionIfTrue(values == null, "The values to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(values.length == 0, "The values to look for should not be empty");

    Set<V> valuesNotFound = newLinkedHashSet();
    for (V value : values) {
      if (!actual.containsValue(value)) {
        valuesNotFound.add(value);
      }
    }
    if (!valuesNotFound.isEmpty()) {
      throw failures.failure(info, shouldContainValues(actual, values, valuesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Multimap} is empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * assertThat(actual).isEmpty();
   * </code></pre>
   *
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} is not empty.
   */
  public void isEmpty() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isEmpty()) {
      throw failures.failure(info, shouldBeEmpty(actual));
    }
  }

  /**
   * Verifies that the actual {@link Multimap} is not empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Multimap&lt;String, String&gt; nba = ArrayListMultimap.create();
   * nba.put("Bulls", "Derrick Rose");
   * nba.put("Bulls", "Joachim Noah");
   * 
   * assertThat(nba).isNotEmpty();
   * </code></pre>
   *
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} is empty.
   */
  public void isNotEmpty() {
    Objects.instance().assertNotNull(info, actual);
    if (actual.isEmpty()) {
      throw failures.failure(info, shouldNotBeEmpty());
    }
  }

  /**
   * Verifies that the number of values in the actual {@link Multimap} is equal to the given one.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * assertThat(actual).hasSize(9);
   * </code></pre>
   *
   * @param expectedSize the expected size of actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the number of values of the actual {@link Multimap} is not equal to the given one.
   */
  public MultimapAssert<K, V> hasSize(int expectedSize) {
    Objects.instance().assertNotNull(info, actual);
    int sizeOfActual = actual.size();
    if (sizeOfActual == expectedSize) {
      return this;
    }
    throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, expectedSize));
  }

}
