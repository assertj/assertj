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
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.error.OptionalShouldBeAbsent.shouldBeAbsent;
import static org.assertj.guava.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.guava.error.OptionalShouldBePresentWithValue.shouldBePresentWithValue;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractObjectAssert;

import com.google.common.base.Optional;

/**
 *
 * Assertions for guava {@link Optional}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Optional)}</code>
 * <p>
 *
 * @author Kornel Kiełczewski
 *
 * @param <T> the type of elements of the tested Optional value
 */
public class OptionalAssert<T> extends AbstractAssert<OptionalAssert<T>, Optional<T>> {

  protected OptionalAssert(final Optional<T> actual) {
    super(actual, OptionalAssert.class);
  }

  // visible for test
  protected Optional<T> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link Optional} contains the given value.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Optional&lt;String&gt; optional = Optional.of(&quot;Test&quot;);
   *
   * assertThat(optional).contains(&quot;Test&quot;);</code></pre>
   *
   * @param value the value to look for in actual {@link Optional}.
   * @return this {@link OptionalAssert} for assertions chaining.
   *
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains nothing or does not have the given value.
   */
  @SuppressWarnings("deprecation")
  public OptionalAssert<T> contains(final Object value) {
    isNotNull();
    if (!actual.isPresent()) {
      throw assertionError(shouldBePresentWithValue(value));
    }
    if (!areEqual(actual.get(), value)) {
      throw assertionError(shouldBePresentWithValue(actual, value));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link Optional} contained instance is absent/null (ie. not {@link Optional#isPresent()}).<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Optional&lt;String&gt; optional = Optional.absent();
   *
   * assertThat(optional).isAbsent();</code></pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   *
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a (non-null) instance.
   */
  public OptionalAssert<T> isAbsent() {
    isNotNull();
    if (actual.isPresent()) {
      throw assertionError(shouldBeAbsent(actual));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link Optional} contains a (non-null) instance.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Optional&lt;String&gt; optional = Optional.of(&quot;value&quot;);
   *
   * assertThat(optional).isPresent();</code></pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   *
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a null instance.
   */
  public OptionalAssert<T> isPresent() {
    isNotNull();
    if (!actual.isPresent()) {
      throw assertionError(shouldBePresent(actual));
    }
    return this;
  }

  /**
   * Chain assertion on the content of the {@link Optional}.
   * <p>
   * Example :
   *
   * <pre><code class='java'> Optional&lt;Number&gt; optional = Optional.of(12L);
   *
   * assertThat(optional).extractingValue().isInstanceOf(Long.class);</code></pre>
   *
   * @return a new {@link AbstractObjectAssert} for assertions chaining on the content of the Optional.
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a null instance.
   */
  public AbstractObjectAssert<?, T> extractingValue() {
    isPresent();
    T assertion = actual.get();
    return assertThat(assertion);
  }

  /**
   * Chain assertion on the content of the {@link Optional}.
   * <p>
   * Example :
   *
   * <pre><code class='java'> Optional&lt;String&gt; optional = Optional.of("Bill");
   *
   * assertThat(optional).extractingCharSequence().startsWith("Bi");</code></pre>
   *
   * @return a new {@link AbstractCharSequenceAssert} for assertions chaining on the content of the Optional.
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a null instance.
   */
  public AbstractCharSequenceAssert<?, ? extends CharSequence> extractingCharSequence() {
    isPresent();
    assertThat(actual.get()).isInstanceOf(CharSequence.class);
    return assertThat((CharSequence) actual.get());
  }

}
