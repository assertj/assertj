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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.guava.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.guava.error.ShouldContainValues.shouldContainValues;
import static org.assertj.guava.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.MapEntry;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;

/**
 * Assertions for guava {@link Multimap}.
 *
 * @author marcelfalliere
 * @author miralak
 * @author Joel Costigliola
 */
public class MultimapAssert<K, V> extends AbstractAssert<MultimapAssert<K, V>, Multimap<K, V>> {

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
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * assertThat(actual).containsKeys(&quot;Lakers&quot;, &quot;Bulls&quot;);</code></pre>
   * <p>
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
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(keys == null, "The keys to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(keys.length == 0, "The keys to look for should not be empty");

    Set<K> keysNotFound = newLinkedHashSet();
    for (K key : keys) {
      if (!actual.containsKey(key)) {
        keysNotFound.add(key);
      }
    }
    if (!keysNotFound.isEmpty()) {
      throw assertionError(shouldContainKeys(actual, keys, keysNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Multimap} contains the given entries.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * // entry can be statically imported from org.assertj.guava.api.Assertions or org.assertj.guava.data.MapEntry
   * assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));</code></pre>
   * <p>
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
  public final MultimapAssert<K, V> contains(MapEntry<K, V>... entries) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(entries == null, "The entries to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(entries.length == 0, "The entries to look for should not be empty");

    List<MapEntry<K, V>> entriesNotFound = newArrayList();
    for (MapEntry<K, V> entry : entries) {
      if (!actual.containsEntry(entry.key, entry.value)) {
        entriesNotFound.add(entry);
      }
    }
    if (!entriesNotFound.isEmpty()) {
      throw assertionError(shouldContain(actual, entries, entriesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Multimap} contains the given values for any key.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * // note that given values are not linked to same key
   * assertThat(actual).containsValues(&quot;Kobe Bryant&quot;, &quot;Michael Jordan&quot;);</code></pre>
   * <p>
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
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(values == null, "The values to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(values.length == 0, "The values to look for should not be empty");

    Set<V> valuesNotFound = newLinkedHashSet();
    for (V value : values) {
      if (!actual.containsValue(value)) {
        valuesNotFound.add(value);
      }
    }
    if (!valuesNotFound.isEmpty()) {
      throw assertionError(shouldContainValues(actual, values, valuesNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Multimap} is empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * assertThat(actual).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} is not empty.
   */
  public void isEmpty() {
    isNotNull();
    if (!actual.isEmpty()) {
      throw assertionError(shouldBeEmpty(actual));
    }
  }

  /**
   * Verifies that the actual {@link Multimap} is not empty.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * nba.put("Bulls", "Derrick Rose");
   * nba.put("Bulls", "Joachim Noah");
   *
   * assertThat(nba).isNotEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} is empty.
   */
  public void isNotEmpty() {
    isNotNull();
    if (actual.isEmpty()) {
      throw assertionError(shouldNotBeEmpty());
    }
  }

  /**
   * Verifies that the number of values in the actual {@link Multimap} is equal to the given one.
   *
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   *
   * actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * assertThat(actual).hasSize(9);</code></pre>
   *
   * @param expectedSize the expected size of actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the number of values of the actual {@link Multimap} is not equal to the given one.
   */
  public MultimapAssert<K, V> hasSize(int expectedSize) {
    isNotNull();
    int sizeOfActual = actual.size();
    if (sizeOfActual == expectedSize) {
      return this;
    }
    throw assertionError(shouldHaveSize(actual, sizeOfActual, expectedSize));
  }

  /**
   * Verifies that the actual {@link Multimap} has the same entries as the given one.<br>
   * It allows to compare two multimaps having the same content but who are not equal because being of different types
   * like {@link SetMultimap} and {@link ListMultimap}.
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * listMultimap.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * listMultimap.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * Multimap&lt;String, String&gt; setMultimap = TreeMultimap.create();
   * setMultimap.putAll("Spurs", newHashSet("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * setMultimap.putAll("Bulls", newHashSet("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * // assertion will pass as listMultimap and setMultimap have the same content
   * assertThat(listMultimap).hasSameEntriesAs(setMultimap);
   *
   * // this assertion FAILS even though both multimaps have the same content
   * assertThat(listMultimap).isEqualTo(setMultimap);</code></pre>
   *
   * @param other {@link Multimap} to compare actual's entries with.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws AssertionError           if the actual {@link Multimap} is {@code null}.
   * @throws IllegalArgumentException if the other {@link Multimap} is {@code null}.
   * @throws AssertionError           if actual {@link Multimap} does not have the same entries as the other {@link Multimap}.
   */
  public final MultimapAssert<K, V> hasSameEntriesAs(Multimap<? extends K, ? extends V> other) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(other == null, "The multimap to compare actual with should not be null");

    Set<?> entriesNotExpectedInActual = difference(newLinkedHashSet(actual.entries()), newLinkedHashSet(other.entries()));
    Set<?> entriesNotFoundInActual = difference(newLinkedHashSet(other.entries()), newLinkedHashSet(actual.entries()));
    if (entriesNotFoundInActual.isEmpty() && entriesNotExpectedInActual.isEmpty()) return myself;
    throw assertionError(shouldContainOnly(actual, other, entriesNotFoundInActual, entriesNotExpectedInActual));
  }

  /**
   * Verifies that the actual {@link Multimap} contains all entries of the given one (it might contain more entries).
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
   * actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
   *
   * Multimap&lt;String, String&gt; other = TreeMultimap.create();
   * other.putAll("Spurs", newHashSet("Tony Parker", "Tim Duncan"));
   * other.putAll("Bulls", newHashSet("Michael Jordan", "Scottie Pippen"));
   *
   * // assertion will pass as other is a subset of actual.
   * assertThat(actual).containsAllEntriesOf(other);
   *
   * // this assertion FAILS as other does not contain "Spurs -&gt; "Manu Ginobili" and "Bulls" -&gt; "Derrick Rose"
   * assertThat(other).containsAllEntriesOf(actual);</code></pre>
   *
   * @param other {@link Multimap} to compare actual's entries with.
   * @return this {@link MultimapAssert} for assertions chaining.
   * @throws AssertionError           if the actual {@link Multimap} is {@code null}.
   * @throws IllegalArgumentException if the other {@link Multimap} is {@code null}.
   * @throws AssertionError           if actual {@link Multimap} does not have contain all the given {@link Multimap} entries.
   */
  public final MultimapAssert<K, V> containsAllEntriesOf(Multimap<? extends K, ? extends V> other) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(other == null, "The multimap to compare actual with should not be null");

    Set<?> entriesNotFoundInActual = difference(newLinkedHashSet(other.entries()), newLinkedHashSet(actual.entries()));
    if (entriesNotFoundInActual.isEmpty()) return myself;
    throw assertionError(shouldContain(actual, other, entriesNotFoundInActual));
  }

}
