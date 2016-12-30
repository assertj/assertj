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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.util.HashSet;

/**
 * Assertions methods applicable to groups of objects (e.g. arrays or collections.)
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
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
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * 
   * // assertions will pass
   * assertThat(abc).contains("b", "a");
   * assertThat(abc).contains("b", "a", "b");
   * 
   * // assertion will fail
   * assertThat(abc).contains("d");</code></pre>
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
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertion will pass
   * assertThat(abc).containsOnly("c", "b", "a");
   * 
   * // assertion will fail because "c" is missing
   * assertThat(abc).containsOnly("a", "b");</code></pre>
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
  S containsOnlyOnce(@SuppressWarnings("unchecked") T... values);

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
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   */
  S containsExactlyInAnyOrder(@SuppressWarnings("unchecked") T... values);

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
   * assertThat(elvesRings).containsSequence(vilya, nenya);
   * assertThat(elvesRings).containsSequence(nenya, narya);
   * 
   * // assertions will fail, the elements order is correct but there is a value between them (nenya) 
   * assertThat(elvesRings).containsSequence(vilya, narya);  
   * assertThat(elvesRings).containsSequence(nenya, vilya);</code></pre>
   * 
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given sequence.
   */
  S containsSequence(@SuppressWarnings("unchecked") T... sequence);

  /**
   * Verifies that the actual group contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(vilya, nenya);
   * assertThat(elvesRings).containsSubsequence(vilya, narya);
   * 
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(nenya, vilya);</code></pre>
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
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).doesNotContain("d")
   * assertThat(abc).doesNotContain("d", "e");
   * 
   * // assertions will fail
   * assertThat(abc).doesNotContain("a");
   * assertThat(abc).doesNotContain("a", "b");
   * assertThat(abc).doesNotContain("c", "d");</code></pre>
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
  S doesNotHaveDuplicates();

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
   * assertThat(abc).startsWith("a");
   * assertThat(abc).startsWith("a", "b");
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
  S startsWith(@SuppressWarnings("unchecked") T... sequence);

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
   * assertThat(abc).endsWith("b", "c");
   * 
   * // assertions will fail
   * assertThat(abc).endsWith("a");
   * assertThat(abc).endsWith("a", "b");</code></pre>
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
  S containsNull();

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
  S doesNotContainNull();

  /**
   * Verifies that each element value satisfies the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc  = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; singleCharacterString = new Condition&lt;String&gt;() {
   *   public boolean matches(String value) {
   *     return value.length() == 1;
   *   }
   * });
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
  S are(Condition<? super T> condition);

  /**
   * Verifies that each element value does not satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; moreThanOneCharacter = new Condition&lt;String&gt() {
   *   public boolean matches(String value) {
   *     return value.length() > 1;
   *   }
   * });
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
  S areNot(Condition<? super T> condition);

  /**
   * Verifies that all elements satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; onlyOneCharacter = new Condition&lt;String&gt;() {
   *   public boolean matches(String value) {
   *     return value.length() == 1;
   *   }
   * });
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
  S have(Condition<? super T> condition);

  /**
   * Verifies that all elements do not satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;String&gt; abcc = newArrayList("a", "b", "cc");
   *
   * Condition&lt;String&gt; moreThanOneCharacter = new Condition&lt;String&gt() {
   *   public boolean matches(String value) {
   *     return value.length() > 1;
   *   }
   * });
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
  S doNotHave(Condition<? super T> condition);

  /**
   * Verifies that there are <b>at least</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;Integer&gt;() {
   *   public boolean matches(Integer value) {
   *     return value % 2 == 1;
   *   }
   * });
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
  S areAtLeast(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual group satisfying the given condition.
   * <p>
   * This method is an alias for {@code areAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> // jedi is a Condition&lt;String&gt;
   * assertThat(newLinkedHashSet("Luke", "Solo", "Leia")).areAtLeastOne(jedi);</code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  S areAtLeastOne(Condition<? super T> condition);

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;Integer&gt;() {
   *   public boolean matches(Integer value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * // assertions will pass
   * oneTwoThree.areAtMost(2, oddNumber);
   * oneTwoThree.areAtMost(3, oddNumber);
   * 
   * // assertion will fail
   * oneTwoThree.areAtMost(1, odd);</code></pre>
   * 
   * @param n the number of times the condition should be at most verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  S areAtMost(int n, Condition<? super T> condition);

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; odd = new Condition&lt;Integer&gt;() {
   *   public boolean matches(Integer value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * // assertion will pass
   * oneTwoThree.areExactly(2, odd);
   * 
   * // assertions will fail
   * oneTwoThree.areExactly(1, odd);
   * oneTwoThree.areExactly(3, odd);</code></pre>
   * 
   * @param n the exact number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  S areExactly(int n, Condition<? super T> condition);

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual group satisfying the given condition.
   * <p>
   * This method is an alias for {@code haveAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;BasketBallPlayer&gt; bullsPlayers = newArrayList(noah, rose);
   * 
   * // potentialMvp is a Condition&lt;BasketBallPlayer&gt;
   * assertThat(bullsPlayers).haveAtLeastOne(potentialMvp);</code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  S haveAtLeastOne(Condition<? super T> condition);

  /**
   * Verifies that there are <b>at least <i>n</i></b> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;Integer&gt;() {
   *   public boolean matches(Integer value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * // assertion will pass
   * oneTwoThree.haveAtLeast(2, oddNumber);
   * 
   * // assertion will fail
   * oneTwoThree.haveAtLeast(3, oddNumber);</code></pre>
   *
   * This method is an alias for {@link #areAtLeast(int, Condition)}.
   */
  S haveAtLeast(int n, Condition<? super T> condition);

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;Integer&gt;() {
   *   public boolean matches(Integer value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * // assertions will pass
   * oneTwoThree.haveAtMost(2, oddNumber);
   * oneTwoThree.haveAtMost(3, oddNumber);
   * 
   * // assertion will fail
   * oneTwoThree.haveAtMost(1, odd);</code></pre>
   *
   * This method is an alias {@link #areAtMost(int, Condition)}.
   */
  S haveAtMost(int n, Condition<? super T> condition);

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Integer&gt; oneTwoThree = newArrayList(1, 2, 3);
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;Integer&gt;() {
   *   public boolean matches(Integer value) {
   *     return value % 2 == 1;
   *   }
   * });
   *
   * // assertion will pass
   * oneTwoThree.haveExactly(2, oddNumber);
   * 
   * // assertions will fail
   * oneTwoThree.haveExactly(1, oddNumber);
   * oneTwoThree.haveExactly(3, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areExactly(int, Condition)}.
   */
  S haveExactly(int n, Condition<? super T> condition);

  /**
   * Verifies that the actual group contains all the elements of given {@code Iterable}, in any order.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = Arrays.asList("a", "b", "c");
   * 
   * // assertions will pass
   * assertThat(abc).containsAll(Arrays.asList("b", "c"));
   * assertThat(abc).containsAll(Arrays.asList("a", "b", "c"));
   * 
   * // assertions will fail
   * assertThat(abc).containsAll(Arrays.asList("d"));
   * assertThat(abc).containsAll(Arrays.asList("a", "b", "c", "d"));</code></pre>
   * 
   * @param iterable the given {@code Iterable} we will get elements from.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain all the elements of given {@code Iterable}.
   */
  S containsAll(Iterable<? extends T> iterable);

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
  S hasAtLeastOneElementOfType(Class<?> expectedType);

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
  S hasOnlyElementsOfType(Class<?> expectedType);

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
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   */
  S containsExactlyElementsOf(Iterable<? extends T> iterable);

  /**
   * Same semantic as {@link #containsOnly(Object[])} : verifies that actual contains all the elements of the given
   * iterable and nothing else, <b>in any order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;Ring&gt; rings = newArrayList(nenya, vilya);
   * 
   * // assertion will pass
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(nenya, vilya));
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(nenya, nenya, vilya, vilya));
   * 
   * // assertion will fail as actual does not contain narya
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(nenya, vilya, narya));
   * // assertion will fail as actual contains nenya
   * assertThat(rings).containsOnlyElementsOf(newLinkedList(vilya));</code></pre>
   * 
   * @param iterable the given {@code Iterable} we will get elements from.
   */
  S containsOnlyElementsOf(Iterable<? extends T> iterable);

  /**
   * An alias of {@link #containsOnlyElementsOf(Iterable)} : verifies that actual contains all the elements of the
   * given iterable and nothing else, <b>in any order</b>.
   * </p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertions will pass:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya));
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya, nenya));
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
  S hasSameElementsAs(Iterable<? extends T> iterable);

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
   *
   * @param iterable the {@link Iterable} whose elements must not be in the actual group.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty iterable.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains some elements of the given {@link Iterable}.
   */
  S doesNotContainAnyElementsOf(Iterable<? extends T> iterable);

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
   * 
   * @param values the {@code Iterable} that should contain all actual elements.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of set {@code Iterable}.
   */
  S isSubsetOf(Iterable<? extends T> values);
  
  /**
   * Verifies that all the elements of actual are present in the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // an Iterable is used in the example but it would also work with an array
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertions will pass:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, narya);
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, narya, dwarfRing);
   * 
   * // assertions will fail:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya);
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, dwarfRing);</code></pre>
   * 
   * @param values the values that should be used for checking the elements of actual.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of the given values.
   */
  S isSubsetOf(@SuppressWarnings("unchecked") T... values);
}
