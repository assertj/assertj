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
package org.assertj.core.api;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Assertions methods applicable to groups of objects (e.g. arrays or collections.)
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Florent Biville
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ELEMENT> the type of elements of the "actual" value.
 */
public interface ObjectEnumerableAssert<SELF extends ObjectEnumerableAssert<SELF, ELEMENT>, ELEMENT>
    extends EnumerableAssert<SELF, ELEMENT> {

  /**
   * Verifies that the actual group contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).contains("b", "a");
   * assertThat(abc).contains("b", "a", "b");
   *
   * // assertion will fail
   * assertThat(abc).contains("d");</code></pre>
   * <p>
   * If you want to specify the elements to check with an {@link Iterable}, use {@link #containsAll(Iterable) containsAll(Iterable)} instead.
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values.
   */
  @SuppressWarnings("unchecked")
  SELF contains(ELEMENT... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, in any order and ignoring duplicates (i.e. once a value is found, its duplicates are also considered found).
   * <p>
   * If you need to check exactly the elements and their duplicates use:
   * <ul>
   * <li>{@link #containsExactly(Object...) containsExactly(Object...)} if the order does matter</li>
   * <li>{@link #containsExactlyInAnyOrder(Object...) containsExactlyInAnyOrder(Object...)} if the order does not matter</li>
   * </ul>
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass as order does not matter
   * assertThat(abc).containsOnly("c", "b", "a");
   * // duplicates are ignored
   * assertThat(abc).containsOnly("a", "a", "b", "c", "c");
   * // ... on both actual and expected values
   * assertThat(asList("a", "a", "b")).containsOnly("a", "b")
   *                                  .containsOnly("a", "a", "b", "b");
   *
   * // assertion will fail because "c" is missing in the given values
   * assertThat(abc).containsOnly("a", "b");
   * // assertion will fail because "d" is missing in abc (use isSubsetOf if you want this assertion to pass)
   * assertThat(abc).containsOnly("a", "b", "c", "d");</code></pre>
   * <p>
   * If you need to check that actual is a subset of the given values, use {@link #isSubsetOf(Object...)}.
   * <p>
   * If you want to specify the elements to check with an {@link Iterable}, use {@link #containsOnlyElementsOf(Iterable) containsOnlyElementsOf(Iterable)} instead.
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more values than the given ones.
   */
  @SuppressWarnings("unchecked")
  SELF containsOnly(ELEMENT... values);

  /**
   * Verifies that the actual group contains only null elements and nothing else.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * Iterable&lt;String&gt; items = Arrays.asList(null, null, null);
   * assertThat(items).containsOnlyNulls();
   *
   * // assertion will fail because items2 contains a not null element
   * Iterable&lt;String&gt; items2 = Arrays.asList(null, null, "notNull");
   * assertThat(items2).containsOnlyNulls();
   *
   * // assertion will fail since an empty iterable does not contain any elements and therefore no null ones.
   * Iterable&lt;String&gt; empty = new ArrayList&lt;&gt;();
   * assertThat(empty).containsOnlyNulls();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group is empty or contains non null elements.
   * @since 2.9.0 / 3.9.0
   */
  SELF containsOnlyNulls();

  /**
   * Verifies that the actual group contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // lists are used in the examples but it would also work with arrays
   *
   * // assertions will pass
   * assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;winter&quot;);
   * assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;coming&quot;, &quot;winter&quot;);
   *
   * // assertions will fail
   * assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;Lannister&quot;);
   * assertThat(newArrayList(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;);
   * assertThat(newArrayList(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;, &quot;Lannister&quot;, &quot;Arya&quot;);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  @SuppressWarnings("unchecked")
  SELF containsOnlyOnce(ELEMENT... values);

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with groups that have a consistent iteration order (i.e. don't use it with
   * {@link HashSet}, prefer {@link #containsOnly(Object...)} in that case).
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactly(vilya, nenya, narya);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(elvesRings).containsExactly(nenya, vilya, narya);</code></pre>
   * <p>
   * If you want to specify the elements to check with an {@link Iterable}, use {@link #containsExactlyElementsOf(Iterable) containsExactlyElementsOf(Iterable)} instead.
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  @SuppressWarnings("unchecked")
  SELF containsExactly(ELEMENT... values);

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   *
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya, vilya);
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyInAnyOrder(vilya, vilya, nenya, narya);
   *
   * // assertion will fail as vilya is contained twice in elvesRings.
   * assertThat(elvesRings).containsExactlyInAnyOrder(nenya, vilya, narya);</code></pre>
   * <p>
   * If you want to specify the elements to check with an {@link Iterable}, use {@link #containsExactlyInAnyOrderElementsOf(Iterable) containsExactlyInAnyOrderElementsOf(Iterable)} instead.
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   */
  @SuppressWarnings("unchecked")
  SELF containsExactlyInAnyOrder(ELEMENT... values);

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya, vilya);
   * Iterable&lt;Ring&gt; elvesRingsSomeMissing = newArrayList(vilya, nenya, narya);
   * Iterable&lt;Ring&gt; elvesRingsDifferentOrder = newArrayList(nenya, narya, vilya, vilya);
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyInAnyOrderElementsOf(elvesRingsDifferentOrder);
   *
   * // assertion will fail as vilya is contained twice in elvesRings.
   * assertThat(elvesRings).containsExactlyInAnyOrderElementsOf(elvesRingsSomeMissing);</code></pre>
   * <p>
   * If you want to directly specify the elements to check, use {@link #containsExactlyInAnyOrder(Object...) containsExactlyInAnyOrder(Object...)} instead.
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.9.0 / 3.9.0
   */
  SELF containsExactlyInAnyOrderElementsOf(Iterable<? extends ELEMENT> values);

  /**
   * Verifies that the actual group contains the given sequence in the correct order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #containsSubsequence(Object...)} to allow values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSequence(vilya, nenya)
   *                       .containsSequence(nenya, narya);
   *
   * // assertions will fail, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(vilya, narya);
   * assertThat(elvesRings).containsSequence(nenya, vilya);</code></pre>
   * <p>
   * If you want to specify the sequence to check with an {@link Iterable}, use {@link #containsSequence(Iterable) containsSequence(Iterable)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given sequence.
   */
  @SuppressWarnings("unchecked")
  SELF containsSequence(ELEMENT... sequence);

  /**
   * Verifies that the actual group contains the given sequence in the correct order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #containsSubsequence(Iterable)} to allow values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, nenya))
   *                       .containsSequence(newArrayList(nenya, narya));
   *
   * // assertions will fail, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, narya));
   * assertThat(elvesRings).containsSequence(newArrayList(nenya, vilya));</code></pre>
   * <p>
   * If you want to directly specify the elements of the sequence to check, use {@link #containsSequence(Object...) containsSequence(Object...)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given sequence.
   */
  SELF containsSequence(Iterable<? extends ELEMENT> sequence);

  /**
   * Verifies that the actual group does not contain the given sequence,
   * a sequence is defined by an ordered group of values <b>without extra values between them</b>.
   * <p>
   * Use {@link #doesNotContainSubsequence(Object...)} to also ensure the sequence does not exist with values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).doesNotContainSequence(vilya, narya)
   *                       .doesNotContainSequence(nenya, vilya);
   *
   * // assertions will fail
   * assertThat(elvesRings).doesNotContainSequence(vilya, nenya);
   * assertThat(elvesRings).doesNotContainSequence(nenya, narya);</code></pre>
   * <p>
   * If you want to specify the sequence not to find with an {@link Iterable}, use {@link #doesNotContainSequence(Iterable) doesNotContainSequence(Iterable)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group contains the given sequence.
   */
  @SuppressWarnings("unchecked")
  SELF doesNotContainSequence(ELEMENT... sequence);

  /**
   * Verifies that the actual group does not contain the given sequence,
   * a sequence is defined by an ordered group of values <b>without extra values between them</b>.
   * <p>
   * Use {@link #doesNotContainSubsequence(Iterable)} to also ensure the sequence does not exist with values between the sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).doesNotContainSequence(newArrayList(vilya, narya))
   *                       .doesNotContainSequence(newArrayList(nenya, vilya));
   *
   * // assertions will fail
   * assertThat(elvesRings).doesNotContainSequence(newArrayList(vilya, nenya));
   * assertThat(elvesRings).doesNotContainSequence(newArrayList(nenya, narya));</code></pre>
   * <p>
   * If you want to directly specify the elements of the sequence not to find, use {@link #doesNotContainSequence(Object...) doesNotContainSequence(Object...)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group contains the given sequence.
   */
  SELF doesNotContainSequence(Iterable<? extends ELEMENT> sequence);

  /**
   * Verifies that the actual group contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(vilya, nenya)
   *                       .containsSubsequence(vilya, narya);
   *
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(nenya, vilya);</code></pre>
   * <p>
   * If you want to specify the elements of the subsequence to check with an {@link Iterable}, use {@link #containsSubsequence(Iterable) containsSubsequence(Iterable)} instead.
  
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given subsequence.
   */
  @SuppressWarnings("unchecked")
  SELF containsSubsequence(ELEMENT... sequence);

  /**
   * Verifies that the actual group contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(newArrayList(vilya, nenya))
   *                       .containsSubsequence(newArrayList(vilya, narya));
   *
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(newArrayList(nenya, vilya));</code></pre>
   * <p>
   * If you want to directly specify the subsequence to check, use {@link #containsSubsequence(Object...) containsSubsequence(Object...)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given subsequence.
   */
  SELF containsSubsequence(Iterable<? extends ELEMENT> sequence);

  /**
   * Verifies that the actual group does not contain the given subsequence,
   * a subsequence is defined by an ordered group of values <b>with possibly extra values between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).doesNotContainSubsequence(nenya, vilya)
   *                       .doesNotContainSubsequence(narya, vilya);
   *
   * // assertion will fail
   * assertThat(elvesRings).doesNotContainSubsequence(vilya, nenya);
   * assertThat(elvesRings).doesNotContainSubsequence(vilya, narya);</code></pre>
   * <p>
   * If you want to specify the subsequence not to find with an {@link Iterable}, use {@link #doesNotContainSubsequence(Iterable) doesNotContainSubsequence(Iterable)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group contains the given subsequence.
   */
  @SuppressWarnings("unchecked")
  SELF doesNotContainSubsequence(ELEMENT... sequence);

  /**
   * Verifies that the actual group does not contain the given subsequence,
   * a subsequence is defined by an ordered group of values <b>with possibly extra values between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass
   * assertThat(elvesRings).doesNotContainSubsequence(newArrayList(nenya, vilya));
   *                       .doesNotContainSubsequence(newArrayList(narya, vilya));
   *
   * // assertion will fail
   * assertThat(elvesRings).doesNotContainSubsequence(newArrayList(vilya, nenya));
   * assertThat(elvesRings).doesNotContainSubsequence(newArrayList(vilya, narya));</code></pre>
   * <p>
   * If you want to directly specify the elements of the subsequence not to find, use {@link #doesNotContainSubsequence(Object...) doesNotContainSubsequence(Object...)} instead.
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group contains the given subsequence.
   */
  SELF doesNotContainSubsequence(Iterable<? extends ELEMENT> sequence);

  /**
   * Verifies that the actual group does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).doesNotContain("d")
   *                .doesNotContain("d", "e");
   *
   * // assertions will fail
   * assertThat(abc).doesNotContain("a");
   * assertThat(abc).doesNotContain("a", "b");
   * assertThat(abc).doesNotContain("c", "d");</code></pre>
   * <p>
   * If you want to specify the elements not to find with an {@link Iterable}, use {@link #doesNotContainAnyElementsOf(Iterable) doesNotContainAnyElementsOf(Iterable)} instead.
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains any of the given values.
   */
  @SuppressWarnings("unchecked")
  SELF doesNotContain(ELEMENT... values);

  /**
   * Verifies that the actual group does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; lotsOfAs = newArrayList("a", "a", "a");
   *
   * // assertion will pass
   * assertThat(abc).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(lotsOfAs).doesNotHaveDuplicates();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains duplicates.
   */
  SELF doesNotHaveDuplicates();

  /**
   * Verifies that the actual group starts with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual group.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).startsWith("a")
   *                .startsWith("a", "b");
   *
   * // assertion will fail
   * assertThat(abc).startsWith("c");</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not start with the given sequence of objects.
   */
  @SuppressWarnings("unchecked")
  SELF startsWith(ELEMENT... sequence);

  /**
   * Verifies that the actual group ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual group.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).endsWith("c")
   *                .endsWith("b", "c");
   *
   * // assertions will fail
   * assertThat(abc).endsWith("a");
   * assertThat(abc).endsWith("a", "b");</code></pre>
   *
   * @param first the first element of the sequence of objects to look for.
   * @param sequence the rest of the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not end with the given sequence of objects.
   */
  @SuppressWarnings("unchecked")
  SELF endsWith(ELEMENT first, ELEMENT... sequence);

  /**
   * Verifies that the actual group ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual group.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).endsWith(new String[0])
   *                .endsWith(new String[] {"c"})
   *                .endsWith(new String[] {"b", "c"});
   *
   * // assertions will fail
   * assertThat(abc).endsWith(new String[] {"a"});
   * assertThat(abc).endsWith(new String[] {"a", "b"});</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not end with the given sequence of objects.
   */
  SELF endsWith(ELEMENT[] sequence);

  /**
   * Verifies that the actual group contains at least a null element.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abNull = newArrayList("a", "b", null);
   *
   * // assertion will pass
   * assertThat(abNull).containsNull();
   *
   * // assertion will fail
   * assertThat(abc).containsNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain a null element.
   */
  SELF containsNull();

  /**
   * Verifies that the actual group does not contain null elements.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abNull = newArrayList("a", "b", null);
   *
   * // assertion will pass
   * assertThat(abc).doesNotContainNull();
   *
   * // assertion will fail
   * assertThat(abNull).doesNotContainNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains a null element.
   */
  SELF doesNotContainNull();

  /**
   * Verifies that each element value satisfies the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc  = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; singleCharacterString
   *      = new Condition&lt;&gt;(s -&gt; s.length() == 1, "single character String");
   *
   * // assertion will pass
   * assertThat(abc).are(singleCharacterString);
   *
   * // assertion will fail
   * assertThat(abcc).are(singleCharacterString);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements do not satisfy the given condition.
   */
  SELF are(Condition<? super ELEMENT> condition);

  /**
   * Verifies that each element value does not satisfy the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; moreThanOneCharacter =
   *     = new Condition&lt;&gt;(s -&gt; s.length() &gt; 1, "more than one character");
   *
   * // assertion will pass
   * assertThat(abc).areNot(moreThanOneCharacter);
   *
   * // assertion will fail
   * assertThat(abcc).areNot(moreThanOneCharacter);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  SELF areNot(Condition<? super ELEMENT> condition);

  /**
   * Verifies that all elements satisfy the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; onlyOneCharacter =
   *     = new Condition&lt;&gt;(s -&gt; s.length() == 1, "only one character");
   *
   * // assertion will pass
   * assertThat(abc).have(onlyOneCharacter);
   *
   * // assertion will fail
   * assertThat(abcc).have(onlyOneCharacter);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements do not satisfy the given condition.
   */
  SELF have(Condition<? super ELEMENT> condition);

  /**
   * Verifies that all elements do not satisfy the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; moreThanOneCharacter =
   *     = new Condition&lt;&gt;(s -&gt; s.length() &gt; 1, "more than one character");
   *
   * // assertion will pass
   * assertThat(abc).doNotHave(moreThanOneCharacter);
   *
   * // assertion will fail
   * assertThat(abcc).doNotHave(moreThanOneCharacter);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  SELF doNotHave(Condition<? super ELEMENT> condition);

  /**
   * Verifies that there are <b>at least</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.areAtLeast(2, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.areAtLeast(3, oddNumber);</code></pre>
   *
   * @param n the minimum number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element can not be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  SELF areAtLeast(int n, Condition<? super ELEMENT> condition);

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual group satisfying the given condition.
   * <p>
   * This method is an alias for {@code areAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> // jedi is a Condition&lt;String&gt;
   * assertThat(newLinkedHashSet("Luke", "Solo", "Leia")).areAtLeastOne(jedi);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} assertion object.
   * @see #haveAtLeast(int, Condition)
   */
  SELF areAtLeastOne(Condition<? super ELEMENT> condition);

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertions will pass
   * oneTwoThree.areAtMost(2, oddNumber)
   *            .areAtMost(3, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.areAtMost(1, oddNumber);</code></pre>
   *
   * @param n the number of times the condition should be at most verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  SELF areAtMost(int n, Condition<? super ELEMENT> condition);

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.areExactly(2, oddNumber);
   *
   * // assertions will fail
   * oneTwoThree.areExactly(1, oddNumber);
   * oneTwoThree.areExactly(3, oddNumber);</code></pre>
   *
   * @param n the exact number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  SELF areExactly(int n, Condition<? super ELEMENT> condition);

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual group satisfying the given condition.
   * <p>
   * This method is an alias for {@code haveAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;BasketBallPlayer&gt; bullsPlayers = newArrayList(butler, rose);
   *
   * // potentialMvp is a Condition&lt;BasketBallPlayer&gt;
   * assertThat(bullsPlayers).haveAtLeastOne(potentialMvp);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} assertion object.
   * @see #haveAtLeast(int, Condition)
   */
  SELF haveAtLeastOne(Condition<? super ELEMENT> condition);

  /**
   * Verifies that there are <b>at least <i>n</i></b> elements in the actual group satisfying the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.haveAtLeast(2, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.haveAtLeast(3, oddNumber);</code></pre>
   *
   * This method is an alias for {@link #areAtLeast(int, Condition)}.
   *
   * @param n the minimum number of times the condition must hold.
   * @param condition the given condition.
   * @return {@code this} assertion object.
   */
  SELF haveAtLeast(int n, Condition<? super ELEMENT> condition);

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertions will pass
   * oneTwoThree.haveAtMost(2, oddNumber);
   * oneTwoThree.haveAtMost(3, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.haveAtMost(1, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areAtMost(int, Condition)}.
   *
   * @param n the maximum number of times the condition must hold.
   * @param condition the given condition.
   * @return {@code this} assertion object.
   */
  SELF haveAtMost(int n, Condition<? super ELEMENT> condition);

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.haveExactly(2, oddNumber);
   *
   * // assertions will fail
   * oneTwoThree.haveExactly(1, oddNumber);
   * oneTwoThree.haveExactly(3, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areExactly(int, Condition)}.
   *
   * @param n the exact number of times the condition must hold.
   * @param condition the given condition.
   * @return {@code this} assertion object.
   */
  SELF haveExactly(int n, Condition<? super ELEMENT> condition);

  /**
   * Verifies that the actual group contains all the elements of given {@code Iterable}, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = Arrays.asList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).containsAll(Arrays.asList("b", "c"))
   *                .containsAll(Arrays.asList("a", "b", "c"));
   *
   * // assertions will fail
   * assertThat(abc).containsAll(Arrays.asList("d"));
   * assertThat(abc).containsAll(Arrays.asList("a", "b", "c", "d"));</code></pre>
   * <p>
   * If you want to directly specify the elements to check, use {@link #contains(Object...) contains(Object...)} instead.
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain all the elements of given {@code Iterable}.
   */
  SELF containsAll(Iterable<? extends ELEMENT> iterable);

  /**
   * Verifies that the unique element of the {@link Iterable} satisfies the given assertions expressed as a {@link Consumer},
   * if it does not, only the first error is reported, use {@link SoftAssertions} to get all the errors.
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Jedi&gt; jedis = asList(new Jedi("Yoda", "red"));
   *
   * // assertions will pass
   *
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Y"));
   *
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   assertThat(yoda.getName()).isEqualTo("Yoda");
   *   assertThat(yoda.getLightSaberColor()).isEqualTo("red");
   * });
   *
   * // assertions will fail
   *
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Vad"));
   *
   * // fail as one the assertions is not satisfied
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   assertThat(yoda.getName()).isEqualTo("Yoda");
   *   assertThat(yoda.getLightSaberColor()).isEqualTo("purple");
   * });
   *
   * // fail but only report the first error
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   assertThat(yoda.getName()).isEqualTo("Luke");
   *   assertThat(yoda.getLightSaberColor()).isEqualTo("green");
   * });
   *
   * // fail and reports the errors thanks to Soft assertions
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   SoftAssertions softly = new SoftAssertions();
   *   softly.assertThat(yoda.getName()).isEqualTo("Luke");
   *   softly.assertThat(yoda.getLightSaberColor()).isEqualTo("green");
   *   softly.assertAll();
   * });
   *
   * // even if the assertion is correct, there are too many jedis!
   * jedis.add(new Jedi("Luke", "green"));
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Yo"));</code></pre>
   *
   * @param elementAssertions the assertions to perform on the unique element.
   * @return {@code this} assertion object.
   * @throws AssertionError if the {@link Iterable} does not have a unique element.
   * @throws AssertionError if the {@link Iterable}'s unique element does not satisfies the given assertions.
   * @since 3.5.0
   */
  SELF hasOnlyOneElementSatisfying(Consumer<? super ELEMENT> elementAssertions);

  /**
   * Verifies that all elements of the actual group are instances of the given types.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;? extends Object&gt; objects = Arrays.asList("foo", new StringBuilder());
   *
   * // assertions will pass
   * assertThat(objects).hasOnlyElementsOfTypes(CharSequence.class)
   *                    .hasOnlyElementsOfTypes(String.class, StringBuilder.class);
   *
   * // assertions will fail
   * assertThat(objects).hasOnlyElementsOfTypes(Number.class);
   * assertThat(objects).hasOnlyElementsOfTypes(String.class, Number.class);
   * assertThat(objects).hasOnlyElementsOfTypes(String.class);</code></pre>
   *
   * @param expectedTypes the expected types
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if not all elements of the actual group are instances of one of the given types
   * @since 2.7.0 / 3.7.0
   */
  SELF hasOnlyElementsOfTypes(Class<?>... expectedTypes);

  /**
   * Verifies that the actual elements are of the given types in the given order, there should be as many expected types as there are actual elements.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Object&gt; list = Arrays.asList(1, "a", "b", 1.00);
   *
   * // assertion succeeds
   * assertThat(list).hasExactlyElementsOfTypes(Integer.class, String.class, String.class, Double.class);
   *
   * // assertions fail
   * // missing second String type
   * assertThat(list).hasExactlyElementsOfTypes(Integer.class, String.class, Double.class);
   * // no Float type in actual
   * assertThat(list).hasExactlyElementsOfTypes(Float.class, String.class, String.class, Double.class);
   * // correct types but wrong order
   * assertThat(list).hasExactlyElementsOfTypes(String.class, Integer.class, String.class, Double.class);
   * // actual has more elements than the specified expected types
   * assertThat(list).hasExactlyElementsOfTypes(String.class);</code></pre>
   *
   * @param expectedTypes the expected types
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given type array is {@code null}.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual elements types don't exactly match the given ones (in the given order).
   */
  SELF hasExactlyElementsOfTypes(Class<?>... expectedTypes);

  /**
   * Verifies that at least one element in the actual {@code Object} group has the specified type (matching
   * includes subclasses of the given type).
   * <p>
   * Example:
   * <pre><code class='java'> Number[] numbers = { 2, 6L, 8.0 };
   *
   * // successful assertion:
   * assertThat(numbers).hasAtLeastOneElementOfType(Long.class);
   *
   * // assertion failure:
   * assertThat(numbers).hasAtLeastOneElementOfType(Float.class);</code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual {@code Object} group does not have any elements of the given type.
   */
  SELF hasAtLeastOneElementOfType(Class<?> expectedType);

  /**
   * Verifies that all the elements in the actual {@code Object} group belong to the specified type (matching includes
   * subclasses of the given type).
   * <p>
   * Example:
   * <pre><code class='java'> Number[] numbers = { 2, 6, 8 };
   *
   * // successful assertion:
   * assertThat(numbers).hasOnlyElementsOfType(Integer.class);
   *
   * // assertion failure:
   * assertThat(numbers).hasOnlyElementsOfType(Long.class);</code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if one element is not of the expected type.
   */
  SELF hasOnlyElementsOfType(Class<?> expectedType);

  /**
   * Verifies that all the elements in the actual {@code Object} group do not belong to the specified types (including subclasses).
   * <p>
   * Example:
   * <pre><code class='java'> Number[] numbers = { 2, 6, 8.0 };
   *
   * // successful assertion:
   * assertThat(numbers).doesNotHaveAnyElementsOfTypes(Long.class, Float.class);
   *
   * // assertion failure:
   * assertThat(numbers).doesNotHaveAnyElementsOfTypes(Long.class, Integer.class);</code></pre>
   *
   * @param unexpectedTypes the not expected types.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if one element's type matches the given types.
   * @since 2.9.0 / 3.9.0
   */
  SELF doesNotHaveAnyElementsOfTypes(Class<?>... unexpectedTypes);

  /**
   * Same as {@link #containsExactly(Object...)} but handle the {@link Iterable} to array conversion : verifies that
   * actual contains exactly the elements of the given iterable and nothing else <b>in the same order</b>.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyElementsOf(newLinkedList(vilya, nenya, narya));
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(elvesRings).containsExactlyElementsOf(newLinkedList(nenya, vilya, narya));</code></pre>
   * <p>
   * If you want to directly specify the elements to check, use {@link #containsExactly(Object...) containsExactly(Object...)} instead.
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   *
   * @return {@code this} assertion object.
   */
  SELF containsExactlyElementsOf(Iterable<? extends ELEMENT> iterable);

  /**
   * Same semantic as {@link #containsOnly(Object[])} : verifies that actual contains all the elements of the given
   * iterable and nothing else, <b>in any order</b> and ignoring duplicates (i.e. once a value is found, its duplicates are also considered found).
   * <p>
   * <b>This assertion has been deprecated because its name is confusing</b>, users were expecting it to behave like {@link #isSubsetOf(Iterable)}.
   * <p>
   * For example this assertion fails when users expected it to pass:
   * <pre><code class='java'> Iterable&lt;Ring&gt; rings = list(nenya, vilya);
   *
   * // assertion fails because narya is not in rings, confusing!
   * assertThat(rings).containsOnlyElementsOf(list(nenya, vilya, narya));</code></pre>
   * <p>
   * Use {@link #isSubsetOf(Iterable)} to check that actual is a subset of given iterable, or if you need to same assertion semantics use {@link #hasSameElementsAs(Iterable)}.
   * <p>
   * Examples:
   * <pre><code class='java'> Iterable&lt;Ring&gt; rings = newArrayList(nenya, vilya);
   *
   * // assertion will pass
   * assertThat(rings).containsOnlyElementsOf(list(nenya, vilya))
   *                  .containsOnlyElementsOf(list(nenya, nenya, vilya, vilya));
   * assertThat(list(nenya, nenya, vilya, vilya)).containsOnlyElementsOf(rings);
   *
   * // assertion will fail as actual does not contain narya
   * assertThat(rings).containsOnlyElementsOf(list(nenya, vilya, narya));
   * // assertion will fail as actual contains nenya
   * assertThat(rings).containsOnlyElementsOf(list(vilya));</code></pre>
   * <p>
   * If you want to directly specify the elements to check, use {@link #containsOnly(Object...) containsOnly(Object...)} instead.
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   *
   * @return {@code this} assertion object.
   * @deprecated
   */
  @Deprecated
  SELF containsOnlyElementsOf(Iterable<? extends ELEMENT> iterable);

  /**
   * Same semantic as {@link #containsOnlyOnce(Object...)} : verifies that the actual group contains the elements of
   * the given iterable only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertions will pass
   * assertThat(list(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnceElementsOf(list(&quot;winter&quot;))
   *                                           .containsOnlyOnceElementsOf(list(&quot;coming&quot;, &quot;winter&quot;));
   *
   * // assertions will fail
   * assertThat(list(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnceElementsOf(list(&quot;Lannister&quot;));
   * assertThat(list(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnceElementsOf(list(&quot;Stark&quot;));
   * assertThat(list(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnceElementsOf(list(&quot;Stark&quot;, &quot;Lannister&quot;, &quot;Arya&quot;));</code></pre>
   * <p>
   * If you want to directly specify the elements to check with, use {@link #containsOnlyOnce(Object...)} instead.
   * @param iterable the given {@code Iterable} we will get elements from.
   *
   * @return {@code this} assertion object.
   *
   * @since 3.16.0
   */
  SELF containsOnlyOnceElementsOf(Iterable<? extends ELEMENT> iterable);

  /**
   * Verifies that actual contains all the elements of the given iterable and nothing else, <b>in any order</b>
   * and ignoring duplicates (i.e. once a value is found, its duplicates are also considered found).
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya))
   *                       .hasSameElementsAs(newArrayList(nenya, narya, vilya, nenya));
   *
   * // assertions will fail:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya));
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya, oneRing));</code></pre>
   *
   * @param iterable the Iterable whose elements we expect to be present
   * @return this assertion object
   * @throws AssertionError if the actual group is {@code null}
   * @throws NullPointerException if the given {@code Iterable} is {@code null}
   * @throws AssertionError if the actual {@code Iterable} does not have the same elements, in any order, as the given
   *           {@code Iterable}
   */
  SELF hasSameElementsAs(Iterable<? extends ELEMENT> iterable);

  /**
   * Verifies that actual does not contain any elements of the given {@link Iterable} (i.e. none).
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertion succeeds:
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("d", "e"));
   *
   * // assertion fails:
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("d", "e", "a"));</code></pre>
   * <p>
   * If you want to directly specify the elements not to find, use {@link #doesNotContain(Object...) doesNotContain(Object...)} instead.
   *
   * @param iterable the {@link Iterable} whose elements must not be in the actual group.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty iterable.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains some elements of the given {@link Iterable}.
   */
  SELF doesNotContainAnyElementsOf(Iterable<? extends ELEMENT> iterable);

  /**
   * Verifies that all the elements of actual are present in the given {@code Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * List&lt;Ring&gt; ringsOfPower = newArrayList(oneRing, vilya, nenya, narya, dwarfRing, manRing);
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass:
   * assertThat(elvesRings).isSubsetOf(ringsOfPower);
   *
   * // assertion will fail:
   * assertThat(elvesRings).isSubsetOf(newArrayList(nenya, narya));</code></pre>
   * <p>
   * If you want to directly specify the set of elements, use {@link #isSubsetOf(Object...) isSubsetOf(Object...)} instead.
   *
   * @param values the {@code Iterable} that should contain all actual elements.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of set {@code Iterable}.
   */
  SELF isSubsetOf(Iterable<? extends ELEMENT> values);

  /**
   * Verifies that all the elements of actual are present in the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertions will pass:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, narya)
   *                       .isSubsetOf(vilya, nenya, narya, dwarfRing);
   *
   * // assertions will fail:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya);
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, dwarfRing);</code></pre>
   * <p>
   * If you want to specify the set of elements an {@link Iterable}, use {@link #isSubsetOf(Iterable) isSubsetOf(Iterable)} instead.
   *
   * @param values the values that should be used for checking the elements of actual.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of the given values.
   */
  @SuppressWarnings("unchecked")
  SELF isSubsetOf(ELEMENT... values);

  /**
   * Verifies that all the elements of actual match the given {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc  = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * // assertion will pass
   * assertThat(abc).allMatch(s -&gt; s.length() == 1);
   *
   * // assertion will fail
   * assertThat(abcc).allMatch(s -&gt; s.length() == 1);</code></pre>
   *
   * Note that you can achieve the same result with {@link #are(Condition) are(Condition)} or {@link #have(Condition) have(Condition)}.
   *
   * @param predicate the given {@link Predicate}.
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements don't satisfy the given predicate.
   */
  SELF allMatch(Predicate<? super ELEMENT> predicate);

  /**
   * Verifies that all the elements of actual match the given {@link Predicate}. The predicate description is used
   * to get an informative error message.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * // assertion will pass
   * assertThat(abc).allMatch(s -&gt; s.length() == 1, "length of 1");
   *
   * // assertion will fail
   * assertThat(abcc).allMatch(s -&gt; s.length() == 1, "length of 1");</code></pre>
   *
   * The message of the failed assertion would be:
   * <pre><code class='java'>Expecting all elements of:
   *  &lt;["a", "b", "cc"]&gt;
   *  to match 'length of 1' predicate but this element did not:
   *  &lt;"cc"&gt;</code></pre>
   *
   *
   * @param predicate the given {@link Predicate}.
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements don't satisfy the given predicate.
   * @since 3.6.0
   */
  SELF allMatch(Predicate<? super ELEMENT> predicate, String predicateDescription);

  /**
   * Verifies that all the elements satisfy the given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to perform a group of assertions on elements.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(myIcelanderFriends).allSatisfy(person -&gt; {
   *                                 assertThat(person.getCountry()).isEqualTo("Iceland");
   *                                 assertThat(person.getPhoneCountryCode()).isEqualTo("+354");
   *                               });</code></pre>
   *
   * <p>
   * If the actual iterable is empty, this assertion succeeds as there is no elements to check.
   *
   * @param requirements the given {@link Consumer}.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError if one or more elements don't satisfy the given requirements.
   * @since 3.6.0
   */
  SELF allSatisfy(Consumer<? super ELEMENT> requirements);

  /**
   * Verifies that all the elements satisfy the given requirements expressed as a {@link ThrowingConsumer}.
   * <p>
   * This is useful to perform a group of assertions on elements.
   * <p>
   * This is the same assertion as {@link #allSatisfy(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'>  // read() throws IOException
   * // note that the code would not compile if isNotEmpty, startsWithA or startsWithZ were declared as a Consumer&lt;Reader&gt; 
   * ThrowingConsumer&lt;Reader&gt; isNotEmpty = reader -&gt; assertThat(reader.read()).isEqualTo(-1);
   * ThrowingConsumer&lt;Reader&gt; startsWithA = reader -&gt; assertThat(reader.read()).isEqualTo('A');
   *
   * // ABC.txt contains: ABC  
   * // XYZ.txt contains: XYZ  
   * Iterable&lt;FileReader&gt; fileReaders = Arrays.asList(new FileReader("ABC.txt"), new FileReader("XYZ.txt"));
   * 
   * // assertion succeeds as none of the files are empty
   * assertThat(fileReaders).allSatisfy(isNotEmpty);
   *
   * // assertion fails as XYZ.txt does not start with 'A':
   * assertThat(fileReaders).allSatisfy(startsWithA);</code></pre>
   * <p>
   * If the actual iterable is empty, this assertion succeeds as there is nothing to check.
   *
   * @param requirements the given {@link ThrowingConsumer}.
   * @return {@code this} object.
   * @throws NullPointerException if given {@link ThrowingConsumer} is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if one or more elements don't satisfy the given requirements.
   * @since 3.21.0
   */
  SELF allSatisfy(ThrowingConsumer<? super ELEMENT> requirements);

  /**
   * Verifies that each element satisfies the requirements corresponding to its index, so the first element must satisfy the
   * first requirements, the second element the second requirements etc...
   * <p>
   * Each requirements are expressed as a {@link Consumer}, there must be as many requirements as there are iterable elements.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;TolkienCharacter&gt; characters = list(frodo, aragorn, legolas);
   *
   * // assertions succeed
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isTrue(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));
   *
   * // you can specify more than one assertion per requirement
   * assertThat(characters).satisfiesExactly(character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Hobbit");
   *                                            assertThat(character.getName()).isEqualTo("Frodo");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.isMortal()).isTrue();
   *                                            assertThat(character.getName()).isEqualTo("Aragorn");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Elf");
   *                                            assertThat(character.getName()).isEqualTo("Legolas");
   *                                         });
   *
   * // assertion fails as aragorn does not meet the second requirements
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isFalse(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));</code></pre>
   *
   * @param allRequirements the requirements to meet.
   * @return {@code this} to chain assertions.
   * @throws NullPointerException if given requirements are null.
   * @throws AssertionError if any element does not satisfy the requirements at the same index
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   * @since 3.19.0
   */
  @SuppressWarnings("unchecked")
  SELF satisfiesExactly(Consumer<? super ELEMENT>... allRequirements);

  /**
   * Verifies that each element satisfies the requirements corresponding to its index, so the first element must satisfy the
   * first requirements, the second element the second requirements etc...
   * <p>
   * Each requirements are expressed as a {@link ThrowingConsumer}, there must be as many requirements as there are iterable elements.
   * <p>
   * This is the same assertion as {@link #satisfiesExactly(Consumer...)} but the given consumers can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;TolkienCharacter&gt; characters = list(frodo, aragorn, legolas);
   * 
   * // the code would compile even if TolkienCharacter.getRace(), isMortal() or getName() threw a checked exception
   *
   * // assertions succeed
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isTrue(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));
   *
   * // you can specify more than one assertion per requirement
   * assertThat(characters).satisfiesExactly(character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Hobbit");
   *                                            assertThat(character.getName()).isEqualTo("Frodo");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.isMortal()).isTrue();
   *                                            assertThat(character.getName()).isEqualTo("Aragorn");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Elf");
   *                                            assertThat(character.getName()).isEqualTo("Legolas");
   *                                         });
   *
   * // assertion fails as aragorn does not meet the second requirements
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isFalse(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));</code></pre>
   *
   * @param allRequirements the requirements to meet.
   * @return {@code this} to chain assertions.
   * @throws NullPointerException if given requirements are null.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if any element does not satisfy the requirements at the same index
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   * @since 3.21.0
   */
  @SuppressWarnings("unchecked")
  SELF satisfiesExactly(ThrowingConsumer<? super ELEMENT>... allRequirements);

  /**
   * Verifies that at least one combination of iterable elements exists that satisfies the consumers in order (there must be as
   * many consumers as iterable elements and once a consumer is matched it cannot be reused to match other elements).
   * <p>
   * This is a variation of {@link #satisfiesExactly(Consumer...)} where order does not matter.
   * <p>
   * Examples:
   * <pre><code class='java'> List&lt;String&gt; starWarsCharacterNames = list("Luke", "Leia", "Yoda");
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"), // matches "Yoda"
   *                                                               name -&gt; assertThat(name).contains("L"), // matches "Luke" and "Leia"
   *                                                               name -&gt; {
   *                                                                 assertThat(name).hasSize(4);
   *                                                                 assertThat(name).doesNotContain("a"); // matches "Luke" but not "Leia"
   *                                                               })
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"),
   *                                                               name -&gt; assertThat(name).contains("Le"))
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Le"),
   *                                                               name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"));
   *
   * // this assertion fails as 3 consumer/requirements are expected
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no element contains "Han" (first consumer/requirements can't be met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Han"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("Y"));
   *
   * // this assertion fails as "Yoda" element can't satisfy any consumers/requirements (even though all consumers/requirements are met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no combination of elements can satisfy the consumers in order
   * // the problem is if the last consumer is matched by Leia then no other consumer can match Luke (and vice versa)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("o"),
   *                                                               name -&gt; assertThat(name).contains("L"));</code></pre>
   *
   * @param allRequirements the consumers that are expected to be satisfied by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given consumers array or any consumer is {@code null}.
   * @throws AssertionError if there is no permutation of elements that satisfies the individual consumers in order
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   *
   * @since 3.19.0
   */
  @SuppressWarnings("unchecked")
  SELF satisfiesExactlyInAnyOrder(Consumer<? super ELEMENT>... allRequirements);

  /**
   * Verifies that at least one combination of iterable elements exists that satisfies the {@link ThrowingConsumer}s in order (there must be as
   * many consumers as iterable elements and once a consumer is matched it cannot be reused to match other elements).
   * <p>
   * This is a variation of {@link #satisfiesExactly(ThrowingConsumer...)} where order does not matter.
   * <p>
   * Examples:
   * <pre><code class='java'> List&lt;String&gt; starWarsCharacterNames = list("Luke", "Leia", "Yoda");
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"), // matches "Yoda"
   *                                                               name -&gt; assertThat(name).contains("L"), // matches "Luke" and "Leia"
   *                                                               name -&gt; {
   *                                                                 assertThat(name).hasSize(4);
   *                                                                 assertThat(name).doesNotContain("a"); // matches "Luke" but not "Leia"
   *                                                               })
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"),
   *                                                               name -&gt; assertThat(name).contains("Le"))
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Le"),
   *                                                               name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"));
   *
   * // this assertion fails as 3 consumers/requirements are expected
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no element contains "Han" (first consumer/requirements can't be met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Han"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("Y"));
   *
   * // this assertion fails as "Yoda" element can't satisfy any consumers/requirements (even though all consumers/requirements are met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no combination of elements can satisfy the consumers in order
   * // the problem is if the last consumer is matched by Leia then no other consumer can match Luke (and vice versa)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("o"),
   *                                                               name -&gt; assertThat(name).contains("L"));</code></pre>
   *
   * @param allRequirements the consumers that are expected to be satisfied by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given consumers array or any consumer is {@code null}.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if there is no permutation of elements that satisfies the individual consumers in order
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   * @since 3.21.0
   */
  @SuppressWarnings("unchecked")
  SELF satisfiesExactlyInAnyOrder(ThrowingConsumer<? super ELEMENT>... allRequirements);

  /**
   * Verifies that there is exactly one element in the iterable under tests that satisfies the {@link Consumer}.
   * <p>
   * Examples:
   * <pre><code class='java'> List&lt;String&gt; starWarsCharacterNames = list("Luke", "Leia", "Yoda");
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Y")) // matches only "Yoda"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Lu")) // matches only "Luke"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Le")); // matches only "Leia"
   *
   * // this assertion fails because the requirements are satisfied two times
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("a")); // matches "Leia" and "Yoda"
   *
   * // this assertion fails because no element contains "Han"
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Han"));</code></pre>
   *
   * @param requirements the {@link Consumer} that is expected to be satisfied only once by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given requirements are {@code null}.
   * @throws AssertionError if the requirements are not satisfied only once
   * @since 3.24.0
   */
  @SuppressWarnings("unchecked")
  SELF satisfiesOnlyOnce(Consumer<? super ELEMENT> requirements);

  /**
   * Verifies that there is exactly one element in the iterable under tests that satisfies the {@link ThrowingConsumer}.
   * <p>
   * Examples:
   * <pre><code class='java'> List&lt;String&gt; starWarsCharacterNames = list("Luke", "Leia", "Yoda");
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Y")) // matches only "Yoda"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Lu")) // matches only "Luke"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Le")); // matches only "Leia"
   *
   * // this assertion fails because the requirements are satisfied two times
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("a")); // matches "Leia" and "Yoda"
   *
   * // this assertion fails because no element contains "Han"
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Han"));</code></pre>
   *
   * @param requirements the {@link ThrowingConsumer} that is expected to be satisfied only once by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given requirements are {@code null}.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if the requirements are not satisfied only once
   * @since 3.24.0
   */
  @SuppressWarnings("unchecked")
  SELF satisfiesOnlyOnce(ThrowingConsumer<? super ELEMENT> requirements);

  /**
   * Verifies whether any elements match the provided {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * // assertion will pass
   * assertThat(abcc).anyMatch(s -&gt; s.length() == 2);
   *
   * // assertion will fail
   * assertThat(abcc).anyMatch(s -&gt; s.length() &gt; 2);</code></pre>
   *
   * Note that you can achieve the same result with {@link #areAtLeastOne(Condition) areAtLeastOne(Condition)}
   * or {@link #haveAtLeastOne(Condition) haveAtLeastOne(Condition)}.
   *
   * @param predicate the given {@link Predicate}.
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if no elements satisfy the given predicate.
   * @since 3.9.0
   */
  SELF anyMatch(Predicate<? super ELEMENT> predicate);

  /**
   * Verifies that at least one element satisfies the given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to check that a group of assertions is verified by (at least) one element.
   * <p>
   * If the group of elements to assert is empty, the assertion will fail.
   * <p>
   * Example:
   * <pre><code class='java'> // assume that one icelander in myIcelanderFriends has a name finishing by 'son'
   * assertThat(myIcelanderFriends).anySatisfy(person -&gt; {
   *                                  assertThat(person.getCountry()).isEqualTo("Iceland");
   *                                  assertThat(person.getSurname()).endsWith("son");
   *                                });
   *
   * // assertion fails for empty group, whatever the requirements are.
   * assertThat(emptyGroup).anySatisfy($ -&gt; assertThat(true).isTrue());</code></pre>
   *
   * @param requirements the given {@link Consumer}.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError no elements satisfy the given requirements.
   * @since 3.7.0
   */
  SELF anySatisfy(Consumer<? super ELEMENT> requirements);

  /**
   * Verifies that at least one element satisfies the given requirements expressed as a {@link ThrowingConsumer}.
   * <p>
   * This is useful to check that a group of assertions is verified by (at least) one element.
   * <p>
   * This is the same assertion as {@link #anySatisfy(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'>  // read() throws IOException
   * // note that the code would not compile if startsWithA, startsWithY or startsWithZ were declared as a Consumer&lt;Reader&gt; 
   * ThrowingConsumer&lt;Reader&gt; startsWithA = reader -&gt; assertThat(reader.read()).isEqualTo('A');
   * ThrowingConsumer&lt;Reader&gt; startsWithZ = reader -&gt; assertThat(reader.read()).isEqualTo('Z');
   *
   * // ABC.txt contains: ABC  
   * // XYZ.txt contains: XYZ  
   * Iterable&lt;FileReader&gt; fileReaders = Arrays.asList(new FileReader("ABC.txt"), new FileReader("XYZ.txt"));
   * 
   * // assertion succeeds as ABC.txt starts with 'A'
   * assertThat(fileReaders).anySatisfy(startsWithA);
   *
   * // assertion fails none of the files starts with 'Z':
   * assertThat(fileReaders).anySatisfy(startsWithZ);</code></pre>
   * <p>
   * If the actual iterable is empty, this assertion succeeds as there is nothing to check.
   *
   * @param requirements the given {@link ThrowingConsumer}.
   * @return {@code this} object.
   * @throws NullPointerException if given {@link ThrowingConsumer} is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError no elements satisfy the given requirements.
   * @since 3.21.0
   */
  SELF anySatisfy(ThrowingConsumer<? super ELEMENT> requirements);

  /**
   * Verifies that no elements satisfy the given restrictions expressed as a {@link Consumer}.
   * <p>
   * Example:
   * <pre><code class='java'> // assume that all icelander in myIcelanderFriends are not from Brazil
   * assertThat(myIcelanderFriends).noneSatisfy(person -&gt; {
   *                                  assertThat(person.getCountry()).isEqualTo("Brazil");
   *                                });
   *</code></pre>
   * Note that this assertion succeeds if the group (collection, array, ...) is empty whatever the restrictions are.
   *
   * @param restrictions the given restrictions as {@link Consumer} that no elements should met.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError if one or more elements satisfy the given requirements.
   * @since 3.10.0
   */
  SELF noneSatisfy(Consumer<? super ELEMENT> restrictions);

  /**
   * Verifies that no elements satisfy the given restrictions expressed as a {@link Consumer}.
   * <p>
   * This is useful to check that a group of assertions is verified by (at least) one element.
   * <p>
   * This is the same assertion as {@link #anySatisfy(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'>  // read() throws IOException
   * // note that the code would not compile if startsWithA, startsWithY or startsWithZ were declared as a Consumer&lt;Reader&gt; 
   * ThrowingConsumer&lt;Reader&gt; startsWithA = reader -&gt; assertThat(reader.read()).isEqualTo('A');
   * ThrowingConsumer&lt;Reader&gt; startsWithZ = reader -&gt; assertThat(reader.read()).isEqualTo('Z');
   *
   * // ABC.txt contains: ABC  
   * // XYZ.txt contains: XYZ  
   * Iterable&lt;FileReader&gt; fileReaders = Arrays.asList(new FileReader("ABC.txt"), new FileReader("XYZ.txt"));
   * 
   * // assertion succeeds as none of the file starts 'Z'
   * assertThat(fileReaders).noneSatisfy(startsWithZ);
   *
   * // assertion fails as ABC.txt starts with 'A':
   * assertThat(fileReaders).noneSatisfy(startsWithA);</code></pre>
   * <p>
   * Note that this assertion succeeds if the group (collection, array, ...) is empty whatever the restrictions are.
   *
   * @param restrictions the given {@link ThrowingConsumer}.
   * @return {@code this} object.
   * @throws NullPointerException if given {@link ThrowingConsumer} is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if one or more elements satisfy the given requirements.
   * @since 3.21.0
   */
  SELF noneSatisfy(ThrowingConsumer<? super ELEMENT> restrictions);

  /**
   * Verifies that the actual {@link Iterable} contains at least one of the given values.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = Arrays.asList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).containsAnyOf("b")
   *                .containsAnyOf("b", "c")
   *                .containsAnyOf("a", "b", "c")
   *                .containsAnyOf("a", "b", "c", "d")
   *                .containsAnyOf("e", "f", "g", "b");
   *
   * // assertions will fail
   * assertThat(abc).containsAnyOf("d");
   * assertThat(abc).containsAnyOf("d", "e", "f", "g");</code></pre>
   * <p>
   * If you want to specify the elements to check with an {@link Iterable}, use {@link #containsAnyElementsOf(Iterable) containsAnyElementsOf(Iterable)} instead.
   *
   * @param values the values whose at least one which is expected to be in the {@code Iterable} under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the {@code Iterable} under test is not empty.
   * @throws AssertionError if the {@code Iterable} under test is {@code null}.
   * @throws AssertionError if the {@code Iterable} under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  @SuppressWarnings("unchecked")
  SELF containsAnyOf(ELEMENT... values);

  /**
   * Verifies that the {@link Iterable} under test contains at least one of the given {@link Iterable} elements.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = Arrays.asList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).containsAnyElementsOf(Arrays.asList("b"))
   *                .containsAnyElementsOf(Arrays.asList("b", "c"))
   *                .containsAnyElementsOf(Arrays.asList("a", "b", "c"))
   *                .containsAnyElementsOf(Arrays.asList("a", "b", "c", "d"))
   *                .containsAnyElementsOf(Arrays.asList("e", "f", "g", "b"));
   *
   * // assertions will fail
   * assertThat(abc).containsAnyElementsOf(Arrays.asList("d"));
   * assertThat(abc).containsAnyElementsOf(Arrays.asList("d", "e", "f", "g"));</code></pre>
   * <p>
   * If you want to directly specify the elements to check, use {@link #containsAnyOf(Object...) containsAnyOf(Object...)} instead.
   *
   * @param iterable the iterable whose at least one element is expected to be in the {@code Iterable} under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the iterable of expected values is {@code null}.
   * @throws IllegalArgumentException if the iterable of expected values is empty and the {@code Iterable} under test is not empty.
   * @throws AssertionError if the {@code Iterable} under test is {@code null}.
   * @throws AssertionError if the {@code Iterable} under test does not contain any of elements from the given {@code Iterable}.
   * @since 2.9.0 / 3.9.0
   */
  SELF containsAnyElementsOf(Iterable<? extends ELEMENT> iterable);

  /**
   * Verifies that no elements match the given {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * // assertion will pass
   * assertThat(abcc).noneMatch(s -&gt; s.isEmpty());
   *
   * // assertion will fail
   * assertThat(abcc).noneMatch(s -&gt; s.length() == 2);</code></pre>
   *
   * Note that you can achieve the same result with {@link #areNot(Condition) areNot(Condition)}
   * or {@link #doNotHave(Condition) doNotHave(Condition)}.
   *
   * @param predicate the given {@link Predicate}.
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if any element satisfy the given predicate.
   * @since 3.9.0
   */
  SELF noneMatch(Predicate<? super ELEMENT> predicate);
}
