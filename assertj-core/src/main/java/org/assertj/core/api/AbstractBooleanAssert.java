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

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeFalse.shouldBeFalse;
import static org.assertj.core.error.ShouldBeTrue.shouldBeTrue;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;

import java.util.Comparator;

import org.assertj.core.internal.Failures;

/**
 * Base class for all implementations of assertions for {@link Boolean}s.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 */
public abstract class AbstractBooleanAssert<SELF extends AbstractBooleanAssert<SELF>> extends AbstractAssert<SELF, Boolean> {

  protected AbstractBooleanAssert(Boolean actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is {@code true}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(true).isTrue();
   * assertThat(Boolean.TRUE).isTrue();
   *
   * // assertions fail:
   * assertThat(false).isTrue();
   * assertThat(Boolean.FALSE).isTrue();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code true}.
   */
  public SELF isTrue() {
    objects.assertNotNull(info, actual);
    if (actual) return myself;
    throw Failures.instance().failure(info, shouldBeTrue(actual), actual, true);
  }

  /**
   * Verifies that the actual value is {@code false}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(false).isFalse();
   * assertThat(Boolean.FALSE).isFalse();
   *
   * // assertions fail:
   * assertThat(true).isFalse();
   * assertThat(Boolean.TRUE).isFalse();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code false}.
   */
  public SELF isFalse() {
    objects.assertNotNull(info, actual);
    if (actual == false) return myself;
    throw Failures.instance().failure(info, shouldBeFalse(actual), actual, false);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(true).isEqualTo(true);
   * assertThat(Boolean.FALSE).isEqualTo(false);
   *
   * // assertions fail:
   * assertThat(true).isEqualTo(false);
   * assertThat(Boolean.TRUE).isEqualTo(false);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(boolean expected) {
    if (actual == null || actual != expected)
      throw Failures.instance().failure(info, shouldBeEqual(actual, expected, info.representation()));
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(true).isNotEqualTo(false);
   * assertThat(Boolean.FALSE).isNotEqualTo(true);
   *
   * // assertions fail:
   * assertThat(true).isNotEqualTo(true);
   * assertThat(Boolean.FALSE).isNotEqualTo(false);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(boolean other) {
    if (actual != null && actual == other) throwAssertionError(shouldNotBeEqual(actual, other));
    return myself;
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated Custom Comparator is not supported for Boolean comparison.
   */
  @Override
  @Deprecated
  public final SELF usingComparator(Comparator<? super Boolean> customComparator) {
    return usingComparator(customComparator, null);
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated Custom Comparator is not supported for Boolean comparison.
   */
  @Override
  @Deprecated
  public final SELF usingComparator(Comparator<? super Boolean> customComparator, String customComparatorDescription) {
    throw new UnsupportedOperationException("custom Comparator is not supported for Boolean comparison");
  }
}
