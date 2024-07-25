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

import java.util.Comparator;

import org.assertj.core.internal.Characters;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Character}s.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 */
public abstract class AbstractCharacterAssert<SELF extends AbstractCharacterAssert<SELF>> extends
    AbstractComparableAssert<SELF, Character> {

  @VisibleForTesting
  Characters characters = Characters.instance();

  protected AbstractCharacterAssert(Character actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat('a').isEqualTo('a');
   *
   * // assertions will fail
   * assertThat('a').isEqualTo('b');
   * assertThat('a').isEqualTo('A');</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(char expected) {
    characters.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('a').isNotEqualTo('b');
   * assertThat('a').isNotEqualTo('A');
   *
   * // assertion will fail
   * assertThat('a').isNotEqualTo('a');</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(char other) {
    characters.assertNotEqual(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('A').isLessThan('a');
   * assertThat('a').isLessThan('b');
   *
   * // assertions will fail
   * assertThat('a').isLessThan('A');
   * assertThat('b').isLessThan('a');
   * assertThat('a').isLessThan('a');</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(char other) {
    characters.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('A').isLessThanOrEqualTo('a');
   * assertThat('A').isLessThanOrEqualTo('A');
   *
   * // assertion will fail
   * assertThat('b').isLessThanOrEqualTo('a');</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(char other) {
    characters.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('a').isGreaterThan('A');
   * assertThat('b').isGreaterThan('a');
   *
   * // assertions will fail
   * assertThat('A').isGreaterThan('a');
   * assertThat('a').isGreaterThan('b');
   * assertThat('a').isGreaterThan('a');</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(char other) {
    characters.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Use unicode character representation instead of standard representation in error messages.
   * <p>
   * It can be useful when comparing UNICODE characters - many unicode chars have duplicate characters assigned,
   * it is thus impossible to find differences from the standard error message:
   * <p>
   * With standard error message:
   * <pre><code class='java'> assertThat('µ').isEqualTo('μ');
   *
   * org.junit.ComparisonFailure:
   * Expected :'μ'
   * Actual   :'µ'</code></pre>
   *
   * With unicode based error message:
   * <pre><code class='java'> assertThat('µ').inUnicode().isEqualTo('μ');
   *
   * org.junit.ComparisonFailure:
   * Expected :\u03bc
   * Actual   :\u00b5</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF inUnicode() {
    info.useUnicodeRepresentation();
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('A').isGreaterThanOrEqualTo('A');
   * assertThat('b').isGreaterThanOrEqualTo('a');
   *
   * // assertion will fail
   * assertThat('a').isGreaterThanOrEqualTo('b');</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(char other) {
    characters.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is a lowercase character.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat('a').isLowerCase();
   *
   * // assertions will fail
   * assertThat('A').isLowerCase();
   * assertThat(' ').isLowerCase();
   * assertThat('.').isLowerCase();
   * assertThat('1').isLowerCase();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a lowercase character.
   */

  public SELF isLowerCase() {
    characters.assertLowerCase(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is a uppercase character.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat('A').isUpperCase();
   *
   * // assertions will fail
   * assertThat('a').isUpperCase();
   * assertThat(' ').isUpperCase();
   * assertThat('.').isUpperCase();
   * assertThat('1').isUpperCase();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a uppercase character.
   */
  public SELF isUpperCase() {
    characters.assertUpperCase(info, actual);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Character> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Character> customComparator, String customComparatorDescription) {
    this.characters = new Characters(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.characters = Characters.instance();
    return super.usingDefaultComparator();
  }
}
