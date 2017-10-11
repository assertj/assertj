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

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceAssert<V> extends AbstractAssert<AtomicReferenceAssert<V>, AtomicReference<V>> {

  public AtomicReferenceAssert(AtomicReference<V> actual) {
    super(actual, AtomicReferenceAssert.class);
  }

  /**
   * Verifies that the actual atomic has the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicReference("foo")).hasValue("foo");
   *
   * // assertion will fail
   * assertThat(new AtomicReference("foo")).hasValue("bar");</code></pre>
   * 
   * @param expectedValue the expected value.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic does not have the given value.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicReferenceAssert<V> hasValue(V expectedValue) {
    isNotNull();
    V actualValue = actual.get();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the actual atomic has not the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicReference("foo")).doesNotHaveValue("bar");
   *
   * // assertion will fail
   * assertThat(new AtomicReference("foo")).doesNotHaveValue("foo");</code></pre>
   * 
   * @param nonExpectedValue the value not expected.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic has the given value.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicReferenceAssert<V> doesNotHaveValue(V nonExpectedValue) {
    isNotNull();
    V actualValue = actual.get();
    if (objects.getComparisonStrategy().areEqual(actualValue, nonExpectedValue)) {
      throwAssertionError(shouldNotContainValue(actual, nonExpectedValue));
    }
    return myself;
  }

}
