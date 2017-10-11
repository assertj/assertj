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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Assertions methods applicable to groups of objects (e.g. arrays or collections.)
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ELEMENT> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Florent Biville
 */
public interface ObjectEnumerableAssert<SELF extends ObjectEnumerableAssert<SELF, ELEMENT>, ELEMENT>
    extends EnumerableAssert<SELF, ELEMENT> {

  /**
   * Verifies that the actual group contains the given values, in any order.
   * <p>
   * Example :
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
  SELF contains(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in any order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertion will pass
   * assertThat(abc).containsOnly("c", "b", "a");
   * 
   * // assertion will fail because "c" is missing
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
  SELF containsOnly(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the actual group contains only null elements and nothing else.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * Iterable&lt;String&gt; items = Arrays.asList(null, null, null);
   * assertThat(items).containsOnlyNulls();
   *
   * // assertion will fail because items2 contains a not null element
   * Iterable&lt;String&gt; items2 = Arrays.asList(null, null, "notNull");
   * assertThat(items2).containsOnlyNulls();
   * 
   * // assertion will fail since an empty iterable does not contain any element and therefore no null ones.
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
  SELF containsOnlyOnce(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with groups that have a consistent iteration order (i.e. don't use it with
   * {@link HashSet}, prefer {@link #containsOnly(Object...)} in that case).
   * <p>
   * Example :
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
  SELF containsExactly(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   *
   * <p>
   * Example :
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
  SELF containsExactlyInAnyOrder(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   *
   * <p>
   * Example :
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya, vilya);
   * Iterable&lt;Ring&gt; elvesRingsSomeMissing = newArrayList(vilya, nenya, narya);
   * Iterable&lt;Ring&gt; elvesRingsDifferentOrder = newArrayList(nenya, narya, vilya, vilya);
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyInAnyOrder(elvesRingsDifferentOrder);
   *
   * // assertion will fail as vilya is contained twice in elvesRings.
   * assertThat(elvesRings).containsExactlyInAnyOrder(elvesRingsSomeMissing);</code></pre>
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
  SELF containsSequence(@SuppressWarnings("unchecked") ELEMENT... sequence);

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
  SELF doesNotContainSequence(@SuppressWarnings("unchecked") ELEMENT... sequence);

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
  SELF containsSubsequence(@SuppressWarnings("unchecked") ELEMENT... sequence);

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
  SELF doesNotContainSubsequence(@SuppressWarnings("unchecked") ELEMENT... sequence);

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
   * Example :
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
  SELF doesNotContain(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the actual group does not contain duplicates.
   * <p>
   * Example :
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
   * Example :
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
  SELF startsWith(@SuppressWarnings("unchecked") ELEMENT... sequence);

  /**
   * Verifies that the actual group ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual group.
   * <p>
   * Example :
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
  SELF endsWith(ELEMENT first, @SuppressWarnings("unchecked") ELEMENT... sequence);

  /**
   * Verifies that the actual group ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual group.
   * <p>
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Example :
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
   * Verifies that all elements of the actual group are instances of the given types.
   * <p>
   * Example :
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
   * @param types the expected classes and interfaces
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if not all elements of the actual group are instances of one of the given types
   * @since 2.7.0 / 3.7.0
   */
  SELF hasOnlyElementsOfTypes(Class<?>... types);

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
   * actual contains all the elements of the given iterable and nothing else <b>in the same order</b>.
   * <p>
   * Example :
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
   * iterable and nothing else, <b>in any order</b>.
   * <p>
   * Use {@link #isSubsetOf(Iterable)} to check that actual is a subset of given iterable
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Ring&gt; rings = newArrayList(nenya, vilya);
   * 
   * // assertion will pass
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(nenya, vilya))
   *                  .containsOnlyElementsOf(newLinkedList(nenya, nenya, vilya, vilya));
   * 
   * // assertion will fail as actual does not contain narya
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(nenya, vilya, narya));
   * // assertion will fail as actual contains nenya
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(vilya));</code></pre>
   * <p>
   * If you want to directly specify the elements to check, use {@link #containsOnly(Object...) containsOnly(Object...)} instead.
   * 
   * @param iterable the given {@code Iterable} we will get elements from.
   *
   * @return {@code this} assertion object.
   */
  SELF containsOnlyElementsOf(Iterable<? extends ELEMENT> iterable);

  /**
   * An alias of {@link #containsOnlyElementsOf(Iterable)} : verifies that actual contains all the elements of the
   * given iterable and nothing else, <b>in any order</b>.
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
  SELF isSubsetOf(@SuppressWarnings("unchecked") ELEMENT... values);

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
   * <pre><code class='java'> Iterable&lt;String&gt; abc  = newArrayList("a", "b", "c");
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
   * Verifies that all the elements satisfy given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to perform a group of assertions on elements.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(myIcelanderFriends).allSatisfy(person -&gt; {
   *                                 assertThat(person.getCountry()).isEqualTo("Iceland");
   *                                 assertThat(person.getPhoneCountryCode()).isEqualTo("+354");
   *                               });</code></pre>
   *
   * @param requirements the given {@link Consumer}.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError if one or more elements don't satisfy given requirements.
   * @since 3.6.0
   */
  SELF allSatisfy(Consumer<? super ELEMENT> requirements);

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
   *                                 assertThat(person.getCountry()).isEqualTo("Iceland");
   *                                 assertThat(person.getSurname()).endsWith("son");
   *                               });
   *                               
   * // assertion fails for empty group, whatever the requirements are.  
   * assertThat(emptyGroup).anySatisfy($ -&gt; assertThat(true).isTrue());</code></pre>
   *
   * @param requirements the given {@link Consumer}.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError none elements satisfy the given requirements.
   * @since 3.7.0
   */
  SELF anySatisfy(Consumer<? super ELEMENT> requirements);

  /**
   * Verifies that the actual {@link Iterable} contains at least one of the given values.
   * <p>
   * Example :
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
  public SELF containsAnyOf(@SuppressWarnings("unchecked") ELEMENT... values);

  /**
   * Verifies that the {@link Iterable} under test contains at least one of the given {@link Iterable} elements.
   * <p>
   * Example :
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
  public SELF containsAnyElementsOf(Iterable<ELEMENT> iterable);
}
