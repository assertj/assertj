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
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;

import java.util.Optional;

/**
 * Assertions for {@link java.util.Optional}.
 *
 * @param <T> type of the value contained in the {@link java.util.Optional}.
 * @author Jean-Christophe Gay
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
   * <pre><code class='java'> assertThat(Optional.of("something")).isPresent();</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.empty()).isPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public S isPresent() {
    isNotNull();
    if (!actual.isPresent()) throw failure(shouldBePresent(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.empty()).isEmpty();</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).isEmpty();</code></pre>
   *
   * @return this assertion object.
   */
  public S isEmpty() {
    isNotNull();
    if (actual.isPresent()) throw failure(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the value in argument.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).contains("something");
   * assertThat(Optional.of(10)).contains(10);</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).contains("something else");
   * assertThat(Optional.of(20)).contains(10);</code></pre>
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
   * Verifies that the actual {@link Optional} contains a value that is an instance of the argument.
   * </p>
   * Assertions will pass:
   *
   * <pre><code class='java'>
   * assertThat(Optional.of("something")).containsInstanceOf(String.class)
   *                                     .containsInstanceOf(Object.class);
   *                                     
   * assertThat(Optional.of(10)).containsInstanceOf(Integer.class);</code></pre>
   *
   * Assertion will fail:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(Integer.class);</code></pre>
   *
   * @param clazz the expected class of the value inside the {@link Optional}.
   * @return this assertion object.
   */
  public S containsInstanceOf(Class<?> clazz) {
    isNotNull();
    if (!actual.isPresent()) throw failure(shouldBePresent(actual));
    if (!clazz.isInstance(actual.get())) throw failure(shouldContainInstanceOf(actual, clazz));
    return myself;
  }
}
