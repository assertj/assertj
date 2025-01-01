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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import java.util.function.Consumer;

import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.assertj.core.util.VisibleForTesting;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Condition} or a list of {@link Consumer}s cannot
 * be satisfied.
 */
public class ShouldSatisfy extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String CONDITION_SHOULD_BE_SATISFIED = "%nExpecting actual:%n  %s%nto satisfy:%n  %s";
  @VisibleForTesting
  public static final String CONSUMERS_SHOULD_BE_SATISFIED_IN_ANY_ORDER = "%nExpecting actual:%n  %s%nto satisfy all the consumers in any order.";
  @VisibleForTesting
  public static final String CONSUMERS_SHOULD_NOT_BE_NULL = "The Consumer<? super E>... expressing the assertions consumers must not be null";

  public static <T> ErrorMessageFactory shouldSatisfy(T actual, Condition<? super T> condition) {
    return new ShouldSatisfy(actual, condition);
  }

  /**
   * Creates a new <code>{@link ShouldSatisfy}</code>.
   *
   * @param <E> the iterable elements type.
   * @param actual the actual iterable in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <E> ErrorMessageFactory shouldSatisfyExactlyInAnyOrder(Iterable<E> actual) {
    return new ShouldSatisfy(actual);
  }

  private ShouldSatisfy(Object actual, Condition<?> condition) {
    super(CONDITION_SHOULD_BE_SATISFIED, actual, condition);
  }

  public static ErrorMessageFactory shouldSatisfyAll(Object actual, Description d) {
    return new ShouldSatisfy(actual, d);
  }

  private ShouldSatisfy(Object actual, Description d) {
    super(CONDITION_SHOULD_BE_SATISFIED, actual, d);
  }

  private <E> ShouldSatisfy(Iterable<E> actual) {
    super(CONSUMERS_SHOULD_BE_SATISFIED_IN_ANY_ORDER, actual);
  }
}
