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
package org.assertj.core.api;

import java.util.HashSet;

/**
 * Assertions methods applicable to groups of objects (e.g. arrays or collections.)
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <T> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 * @author Nicolas François
 */
public interface ObjectEnumerableAssert<S extends ObjectEnumerableAssert<S, T>, T> extends EnumerableAssert<S, T> {

  /**
   * Verifies that the actual group contains the given values, in any order.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * assertThat(abc).contains("b", "a"); // passes
   * </code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values.
   */
  S contains(@SuppressWarnings("unchecked") T... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, in any order.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * assertThat(abc).containsOnly("c", "b", "a"); // passes
   * assertThat(abc).containsOnly("a", "b"); // fails, as other values are present
   * </code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more values than the given ones.
   */
  S containsOnly(@SuppressWarnings("unchecked") T... values);

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * 
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;winter&quot;);
   * assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;coming&quot;, &quot;winter&quot;);
   * 
   * // assertions will fail
   * assertThat(newArrayList(&quot;Aria&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;);
   * assertThat(newArrayList(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;Lannister&quot;);
   * assertThat(newArrayList(&quot;Aria&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;, &quot;Lannister&quot;,
   *                                                                                              &quot;Aria&quot;);
   * </code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  S containsOnlyOnce(@SuppressWarnings("unchecked") T... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with group that have a consistent iteration order (i.e. don't use it with
   * {@link HashSet}, prefer {@link #containsOnly(Object...)} in that case).
   * <p>
   * Example :
   * 
   * <pre><code class='java'>
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertion will pass
   * assertThat(elvesRings).containsExactly(vilya, nenya, narya);
   * 
   * // assertion will fail as actual and expected orders differ.
   * assertThat(elvesRings).containsExactly(nenya, vilya, narya);
   * </code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  S containsExactly(@SuppressWarnings("unchecked") T... values);

  /**
   * Verifies that the actual group contains the given sequence, without any other values between them.
   * <p>
   * 
   * <pre><code class='java'>
   * Example:
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertion will pass
   * assertThat(elvesRings).containsSequence(vilya, nenya);
   * 
   * // assertion will fail
   * assertThat(elvesRings).containsSequence(vilya, narya);
   * assertThat(elvesRings).containsSequence(nenya, vilya);
   * </code></pre>
   * 
   * </p>
   * 
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given sequence.
   */
  S containsSequence(@SuppressWarnings("unchecked") T... sequence);

  /**
   * Verifies that the actual group contains the given subsequence (possibly with other values between them).
   * <p>
   * 
   * <pre><code class='java'>
   * Example:
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertion will pass
   * assertThat(elvesRings).containsSubsequence(vilya, nenya);
   * assertThat(elvesRings).containsSubsequence(vilya, narya);
   * 
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(nenya, vilya);
   * </code></pre>
   * 
   * </p>
   * 
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given subsequence.
   */
  S containsSubsequence(@SuppressWarnings("unchecked") T... sequence);

  /**
   * Verifies that the actual group does not contain the given values.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * assertThat(abc).doesNotContain("d", "e"); // passes
   * </code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains any of the given values.
   */
  S doesNotContain(@SuppressWarnings("unchecked") T... values);

  /**
   * Verifies that the actual group does not contain duplicates.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; lotsOfAs = newArrayList("a", "a", "a");
   *
   * assertThat(abc).doesNotHaveDuplicates(); // passes
   * assertThat(lotsOfAs).doesNotHaveDuplicates(); // fails
   * </code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains duplicates.
   */
  S doesNotHaveDuplicates();

  /**
   * Verifies that the actual group starts with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual group.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * assertThat(abc).startsWith("a", "b"); // passes
   * assertThat(abc).startsWith("c"); // fails
   * </code></pre>
   * 
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not start with the given sequence of objects.
   */
  S startsWith(@SuppressWarnings("unchecked") T... sequence);

  /**
   * Verifies that the actual group ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual group.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * assertThat(abc).endsWith("b", "c"); // passes
   * assertThat(abc).endsWith("a"); // fails
   * </code></pre>
   * 
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not end with the given sequence of objects.
   */
  S endsWith(@SuppressWarnings("unchecked") T... sequence);

  /**
   * Verifies that the actual group contains at least a null element.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abNull = newArrayList("a", "b", null);
   *
   * assertThat(abNull).containsNull(); // passes
   * assertThat(abc).containsNull(); // fails
   * </code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain a null element.
   */
  S containsNull();

  /**
   * Verifies that the actual group does not contain null elements.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abNull = newArrayList("a", "b", null);
   *
   * assertThat(abc).doesNotContainNull(); // passes
   * assertThat(abNull).doesNotContainNull(); // fails
   * </code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains a null element.
   */
  S doesNotContainNull();

  /**
   * Verifies that each element value satisfies the given condition
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; singleCharacterString = new Condition&gt;String&lt;() {
   *   public boolean matches(String value) {
   *     return value.length() == 1;
   *   }
   * });
   *
   * assertThat(abc).are(singleCharacterString); // passes
   * assertThat(abcc).are(singleCharacterString); // fails
   * </code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  S are(Condition<? super T> condition);

  /**
   * Verifies that each element value does not satisfy the given condition
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; longerThanOneCharacter = new Condition&gt;String&lt;() {
   *   public boolean matches(String value) {
   *     return value.length() > 1;
   *   }
   * });
   *
   * assertThat(abc).areNot(longerThanOneCharacter); // passes
   * assertThat(abcc).areNot(longerThanOneCharacter); // fails
   * </code></pre>
   * 
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  S areNot(Condition<? super T> condition);

  /**
   * Verifies that each element value satisfies the given condition
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; singleCharacterString = new Condition&lt;String&gt;() {
   *   public boolean matches(String value) {
   *     return value.length() == 1;
   *   }
   * });
   *
   * assertThat(abc).have(singleCharacterString); // passes
   * assertThat(abcc).have(singleCharacterString); // fails
   * </code></pre>
   * 
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  S have(Condition<? super T> condition);

  /**
   * Verifies that each element value does not satisfy the given condition
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; longerThanOneCharacter = new Condition&gt;String&lt;() {
   *   public boolean matches(String value) {
   *     return value.length() > 1;
   *   }
   * });
   *
   * assertThat(abc).doNotHave(longerThanOneCharacter); // passes
   * assertThat(abcc).doNotHave(longerThanOneCharacter); // fails
   * </code></pre>
   * 
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  S doNotHave(Condition<? super T> condition);

  /**
   * Verifies that there is <b>at least</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(String value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * oneTwoThree.areAtLeast(2, odd); // passes
   * oneTwoThree.areAtLeast(3, odd); // fails
   * </code></pre>
   * 
   * @param n the minimum number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element can not be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  S areAtLeast(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual group satisfying the given condition.
   * <p/>
   * This method is an alias for {@code areAtLeast(1, condition)}.
   * <p/>
   * Example:
   * <pre><code class='java'>
   * // jedi is a Condition&lt;String&gt;
   * assertThat(newLinkedHashSet("Luke", "Solo", "Leia")).areAtLeastOne(jedi);
   * </code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  S areAtLeastOne(Condition<? super T> condition);

  
  /**
   * Verifies that there is <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(String value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * oneTwoThree.areAtMost(2, odd); // passes
   * oneTwoThree.areAtMost(3, odd); // passes
   * oneTwoThree.areAtMost(1, odd); // fails
   * </code></pre>
   * 
   * @param n the number of times the condition should be at most verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  S areAtMost(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(String value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * oneTwoThree.areExactly(2, odd); // passes
   * oneTwoThree.areExactly(1, odd); // fails
   * oneTwoThree.areExactly(3, odd); // fails
   * </code></pre>
   * 
   * @param n the exact number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  S areExactly(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual group satisfying the given condition.
   * <p/>
   * This method is an alias for {@code haveAtLeast(1, condition)}.
   * <p/>
   * Example:
   * <pre><code class='java'>
   * List&lt;BasketBallPlayer&gt; bullsPlayers = newArrayList(noah, rose);
   * 
   * // potentialMvp is a Condition&lt;BasketBallPlayer&gt;
   * assertThat(bullsPlayers).haveAtLeastOne(potentialMvp);
   * </code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  S haveAtLeastOne(Condition<? super T> condition);

  /**
   * Verifies that there is <b>at least <i>n</i></b> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(String value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * oneTwoThree.haveAtLeast(2, odd); // passes
   * oneTwoThree.haveAtLeast(3, odd); // fails
   * </code></pre>
   *
   * This method is an alias for {@link #areAtLeast(int, Condition)}.
   */
  S haveAtLeast(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(String value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * oneTwoThree.haveAtMost(2, odd); // passes
   * oneTwoThree.haveAtMost(3, odd); // passes
   * oneTwoThree.haveAtMost(1, odd); // fails
   * </code></pre>
   *
   * This method is an alias {@link #areAtMost(int, Condition)}.
   */
  S haveAtMost(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(String value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * oneTwoThree.haveExactly(2, odd); // passes
   * oneTwoThree.haveExactly(1, odd); // fails
   * oneTwoThree.haveExactly(3, odd); // fails
   * </code></pre>
   *
   * This method is an alias {@link #areExactly(int, Condition)}.
   */
  S haveExactly(int n, Condition<? super T> condition);

  /**
   * Verifies that the actual group contains all the elements of given {@code Iterable}, in any order.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; cb = newArrayList("c", "b");
   *
   * assertThat(abc).containsAll(cb); // passes
   * </code></pre>
   * 
   * @param iterable the given {@code Iterable} we will get elements from.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain all the elements of given {@code Iterable}.
   */
  S containsAll(Iterable<? extends T> iterable);

  /**
   * Verifies that at least one element in the actual {@code Object} group belong to the specified type (matching
   * includes subclasses of the given type).
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * Number[] numbers = { 2, 6L, 8.0 };
   * 
   * // successful assertion:
   * assertThat(numbers).hasAtLeastOneElementOfType(Long.class);
   * 
   * // assertion failure:
   * assertThat(numbers).hasAtLeastOneElementOfType(Float.class);
   * </code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual {@code Object} group does not have any elements of the given type.
   */
  S hasAtLeastOneElementOfType(Class<?> expectedType);

  /**
   * Verifies that all the elements in the actual {@code Object} group belong to the specified type (matching includes
   * subclasses of the given type).
   * <p/>
   * Example:
   * 
   * <pre><code class='java'>
   * Number[] numbers = { 2, 6, 8 };
   * 
   * // successful assertion:
   * assertThat(numbers).hasOnlyElementsOfType(Integer.class);
   * 
   * // assertion failure:
   * assertThat(numbers).hasOnlyElementsOfType(Long.class);
   * </code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if one element is not of the expected type.
   */
  public S hasOnlyElementsOfType(Class<?> expectedType);
}
