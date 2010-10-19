/*
 * Created on Jul 15, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ErrorWhenConditionIsMet.errorWhenConditionMet;
import static org.fest.assertions.error.NotSatisfied.notSatisfied;

import org.fest.assertions.core.*;
import org.fest.util.VisibleForTesting;

/**
 * Verifies that a value satisfies a <code>{@link Condition}</code>.
 *
 * @author Alex Ruiz
 */
public class Conditions {

  private static final Conditions INSTANCE = new Conditions();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Conditions instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private Conditions() {
    this(Failures.instance());
  }

  @VisibleForTesting Conditions(Failures failures) {
    this.failures = failures;
  }

  /**
   * Asserts that the actual value satisfies the given <code>{@link Condition}</code>.
   * @param <T> the type of the actual value and the type of values that given {@code Condition} takes.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given {@code Condition}.
   */
  public <T> void assertSatisfies(AssertionInfo info, T actual, Condition<T> condition) {
    verifyIsNotNull(condition);
    if (condition.matches(actual)) return;
    throw failures.failure(info, notSatisfied(actual, condition));
  }

  /**
   * Asserts that the actual value does not satisfy the given <code>{@link Condition}</code>.
   * @param <T> the type of the actual value and the type of values that given {@code Condition} takes.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the actual value satisfies the given {@code Condition}.
   */
  public <T> void assertDoesNotSatisfy(AssertionInfo info, T actual, Condition<T> condition) {
    verifyIsNotNull(condition);
    if (!condition.matches(actual)) return;
    throw failures.failure(info, errorWhenConditionMet(actual, condition));
  }

  private void verifyIsNotNull(Condition<?> condition) {
    if (condition == null) throw new NullPointerException("The condition to evaluate should not be null");
  }
}
