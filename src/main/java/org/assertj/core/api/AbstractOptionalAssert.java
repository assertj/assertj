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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;

import java.util.Optional;

/**
 * Assertions for {@link java.util.Optional}.
 *
 * @param <T> type of the value contained in the {@link java.util.Optional}.
 * @author Jean-Christophe Gay
 * @author Nicolai Parlog
 */
public abstract class AbstractOptionalAssert<S extends AbstractOptionalAssert<S, T>, T> extends
    AbstractAssert<S, Optional<T>> {

  protected AbstractOptionalAssert(Optional<T> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.Optional}.
   * </p>
   * Assertion will pass :
   * 
   * <pre><code class='java'>
   * assertThat(Optional.of("something")).isPresent();
   * </code></pre>
   * 
   * Assertion will fail :
   * 
   * <pre><code class='java'>
   * assertThat(Optional.empty()).isPresent();
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isPresent() {
    isNotNull();
    if (!actual.isPresent()) throw failure(shouldBePresent());
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty.
   * </p>
   * Assertion will pass :
   * 
   * <pre><code class='java'>
   * assertThat(Optional.empty()).isEmpty();
   * </code></pre>
   * 
   * Assertion will fail :
   * 
   * <pre><code class='java'>
   * assertThat(Optional.of("something")).isEmpty();
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isEmpty() {
    isNotNull();
    if (actual.isPresent()) throw failure(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains a value {@link Object#equals(Object) equal} to the
   * argument.
   * </p>
   * Assertion will pass :
   * 
   * <pre><code class='java'>
   * assertThat(Optional.of("something")).contains("something");
   * assertThat(Optional.of(10)).contains(10);
   * </code></pre>
   * 
   * Assertion will fail :
   * 
   * <pre><code class='java'>
   * assertThat(Optional.of("something")).contains("something else");
   * assertThat(Optional.of(20)).contains(10);
   * </code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public S contains(T expectedValue) {
    isNotNull();
    if (expectedValue == null) throw new IllegalArgumentException("The expected value should not be <null>.");
    if (!actual.isPresent()) throw failure(shouldContain(expectedValue));
    if (!actual.get().equals(expectedValue)) throw failure(shouldContain(actual, expectedValue));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the instance given as an argument (i.e. it must be the
   * same instance).
   * </p>
   * Assertion will pass :
   * 
   * <pre><code class='java'>
   * String someString = "something";
   * assertThat(Optional.of(someString)).containsSame(someString);
   * 
   * // Java will create the same 'Integer' instance when boxing small ints
   * assertThat(Optional.of(10)).containsSame(10);
   * </code></pre>
   * 
   * Assertion will fail :
   * 
   * <pre><code class='java'>
   * // not even equal:
   * assertThat(Optional.of("something")).containsSame("something else");
   * assertThat(Optional.of(20)).containsSame(10);
   * 
   * // equal but not the same: 
   * assertThat(Optional.of(new String("something"))).containsSame(new String("something"));
   * assertThat(Optional.of(new Integer(10))).containsSame(new Integer(10));
   * </code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public S containsSame(T expectedValue) {
    isNotNull();
    if (expectedValue == null) throw new IllegalArgumentException("The expected value should not be <null>.");
    if (!actual.isPresent()) throw failure(shouldContain(expectedValue));
    if (actual.get() != expectedValue) throw failure(shouldContainSame(actual, expectedValue));
    return myself;
  }
}
