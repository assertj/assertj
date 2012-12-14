package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;

import static org.fest.assertions.error.ShouldContainKeys.shouldContainKeys;
import static org.fest.assertions.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;

import com.google.common.collect.Multimap;

import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.Objects;
import org.fest.util.VisibleForTesting;

/**
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
   * actual = ArrayListMultimap.create();
   * // add several values for each keys
   * actual.putAll(&quot;Lakers&quot;, newArrayList(&quot;Kobe Bryant&quot;, &quot;Magic Johnson&quot;, &quot;Kareem Abdul Jabbar&quot;));
   * actual.putAll(&quot;Spurs&quot;, newArrayList(&quot;Tony Parker&quot;, &quot;Tim Duncan&quot;, &quot;Manu Ginobili&quot;));
   * actual.putAll(&quot;Bulls&quot;, newArrayList(&quot;Michael Jordan&quot;, &quot;Scottie Pippen&quot;, &quot;Derrick Rose&quot;));
   * 
   * assertThat(actual).containsKeys(&quot;Lakers&quot;, &quot;Bulls&quot;);
   * </pre>
   * 
   * If the arguments keys is null or empty an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param keys the keys to look for in actual {@link Multimap}.
   * @throws IllegalArgumentException if no param keys have been set.
   * @throws AssertionError if the actual {@link MultimapAssert} is {@code null}.
   * @throws AssertionError if the actual {@link MultimapAssert} does not contain the given keys.
   */
  public MultimapAssert<K, V> containsKeys(K... keys) {
    Objects.instance().assertNotNull(info, actual);
    throwIllegalArgumentExceptionIfTrue(keys == null, "The keys to look for should not be null");
    throwIllegalArgumentExceptionIfTrue(keys.length == 0, "The keys to look for should not be empty");

    List<K> keysNotFound = newArrayList();
    for (K key : keys) {
      if (!actual.containsKey(key)) {
        keysNotFound.add(key);
      }
    }
    if (!keysNotFound.isEmpty()) {
      throw failures.failure(info, shouldContainKeys(actual, keysNotFound.toArray()));
    }
    return myself;
  }

}
