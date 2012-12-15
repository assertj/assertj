package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;

import static org.fest.assertions.error.ShouldBeEmpty.shouldBeEmpty;
import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.assertions.error.ShouldContainKeys.shouldContainKeys;
import static org.fest.assertions.error.ShouldContainValues.shouldContainValues;
import static org.fest.assertions.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

import org.fest.assertions.data.MapEntry;
import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.Objects;
import org.fest.util.VisibleForTesting;

/**
 * 
 * Assertions for guava {@link Multimap}.
 * 
 * @author @marcelfalliere
 * @author @miralak
 * @author Joel Costigliola
 * 
 */
public class MultimapAssert<K, V> extends AbstractAssert<MultimapAssert<K, V>, Multimap<K, V>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected MultimapAssert(Multimap<K, V> actual) {
    super(actual, MultimapAssert.class);
  }

  /**
   * Verifies that the actual {@link Multimap} contains the given keys.<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * // add several values for each key.
   * actual.putAll(&quot;Lakers&quot;, newArrayList(&quot;Kobe Bryant&quot;, &quot;Magic Johnson&quot;, &quot;Kareem Abdul Jabbar&quot;));
   * actual.putAll(&quot;Spurs&quot;, newArrayList(&quot;Tony Parker&quot;, &quot;Tim Duncan&quot;, &quot;Manu Ginobili&quot;));
   * actual.putAll(&quot;Bulls&quot;, newArrayList(&quot;Michael Jordan&quot;, &quot;Scottie Pippen&quot;, &quot;Derrick Rose&quot;));
   * 
   * assertThat(actual).containsKeys(&quot;Lakers&quot;, &quot;Bulls&quot;);
   * </pre>
   * 
   * If the <code>keys</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param keys the keys to look for in actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * 
   * @throws IllegalArgumentException if no param keys have been set.
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} does not contain the given keys.
   */
  public MultimapAssert<K, V> containsKeys(K... keys) {
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
   * <pre>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * // add several values for each entrie.
   * actual.putAll(&quot;Lakers&quot;, newArrayList(&quot;Kobe Bryant&quot;, &quot;Magic Johnson&quot;, &quot;Kareem Abdul Jabbar&quot;));
   * actual.putAll(&quot;Spurs&quot;, newArrayList(&quot;Tony Parker&quot;, &quot;Tim Duncan&quot;, &quot;Manu Ginobili&quot;));
   * actual.putAll(&quot;Bulls&quot;, newArrayList(&quot;Michael Jordan&quot;, &quot;Scottie Pippen&quot;, &quot;Derrick Rose&quot;));
   * 
   * // entry -&gt; import static org.fest.assertions.api.Assertions.entry
   * assertThat(actual).contains(entry(&quot;Lakers&quot;, &quot;Kobe Bryant&quot;), entry(&quot;Spurs&quot;, &quot;Tim Duncan&quot;));
   * </pre>
   * 
   * If the <code>entries</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param entries the entries to look for in actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * 
   * @throws IllegalArgumentException if no param entries have been set.
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} does not contain the given entries.
   */
  public MultimapAssert<K, V> contains(MapEntry... entries) {
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
   * <pre>
   * Multimap&lt;String, String&gt; actual = ArrayListMultimap.create();
   * // add several values for each value.
   * actual.putAll(&quot;Lakers&quot;, newArrayList(&quot;Kobe Bryant&quot;, &quot;Magic Johnson&quot;, &quot;Kareem Abdul Jabbar&quot;));
   * actual.putAll(&quot;Spurs&quot;, newArrayList(&quot;Tony Parker&quot;, &quot;Tim Duncan&quot;, &quot;Manu Ginobili&quot;));
   * actual.putAll(&quot;Bulls&quot;, newArrayList(&quot;Michael Jordan&quot;, &quot;Scottie Pippen&quot;, &quot;Derrick Rose&quot;));
   * 
   * // note that given values are not linked to same key
   * assertThat(actual).containsValues(&quot;Kobe Bryant&quot;, &quot;Michael Jordan&quot;);
   * </pre>
   * 
   * If the <code>values</code> argument is null or empty, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param values the values to look for in actual {@link Multimap}.
   * @return this {@link MultimapAssert} for assertions chaining.
   * 
   * @throws IllegalArgumentException if no param values have been set.
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} does not contain the given values.
   */
  public MultimapAssert<K, V> containsValues(V... values) {
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
   * @throws AssertionError if the actual {@link Multimap} is {@code null}.
   * @throws AssertionError if the actual {@link Multimap} is not empty.
   */
  public void isEmpty() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isEmpty()) {
      throw failures.failure(info, shouldBeEmpty(actual));
    }
  }

}
