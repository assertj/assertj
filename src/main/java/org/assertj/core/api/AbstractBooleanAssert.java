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

import java.util.Comparator;

import org.assertj.core.internal.Booleans;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Boolean}s.
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Mikhail Mazursky
 */
public abstract class AbstractBooleanAssert<SELF extends AbstractBooleanAssert<SELF>> extends AbstractAssert<SELF, Boolean> {

  @VisibleForTesting
  Booleans booleans = Booleans.instance();

  public AbstractBooleanAssert(Boolean actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is {@code true}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(true).isTrue();
   * assertThat(Boolean.TRUE).isTrue();
   *
   * // assertions will fail
   * assertThat(false).isTrue();
   * assertThat(Boolean.FALSE).isTrue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code true}.
   */
  public SELF isTrue() {
    return isEqualTo(true);
  }

  /**
   * Verifies that the actual value is {@code false}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(false).isFalse();
   * assertThat(Boolean.FALSE).isFalse();
   *
   * // assertions will fail
   * assertThat(true).isFalse();
   * assertThat(Boolean.TRUE).isFalse();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code false}.
   */
  public SELF isFalse() {
    return isEqualTo(false);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(true).isEqualTo(true);
   * assertThat(Boolean.FALSE).isEqualTo(false);
   * 
   * // assertions will fail
   * assertThat(true).isEqualTo(false);
   * assertThat(Boolean.TRUE).isEqualTo(false);</code></pre>
   * 
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(boolean expected) {
    booleans.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(true).isNotEqualTo(false);
   * assertThat(Boolean.FALSE).isNotEqualTo(true);
   *
   * // assertions will fail
   * assertThat(true).isNotEqualTo(true);
   * assertThat(Boolean.FALSE).isNotEqualTo(false);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(boolean other) {
    booleans.assertNotEqual(info, actual, other);
    return myself;
  }

  /**
   * Do not use this method.
   * 
   * @deprecated Custom Comparator is not supported for Boolean comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final SELF usingComparator(Comparator<? super Boolean> customComparator) {
    throw new UnsupportedOperationException("custom Comparator is not supported for Boolean comparison");
  }
}
