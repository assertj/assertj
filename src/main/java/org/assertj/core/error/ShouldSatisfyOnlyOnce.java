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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.util.VisibleForTesting;

/**
 * Creates an error message indicating that an assertion that verifies that requirements are not satisfied only once.
 */
public class ShouldSatisfyOnlyOnce extends BasicErrorMessageFactory {

  @VisibleForTesting
  public static final String REQUIREMENTS_SHOULD_BE_SATISFIED_ONLY_ONCE = "%nExpecting actual:%n  %s%nto satisfy the requirements only once but it did %s times";

  /**
   * Creates a new <code>{@link ShouldSatisfyOnlyOnce}</code>.
   *
   * @param <E> the iterable elements type.
   * @param actual the actual iterable in the failed assertion.
   * @param countOfSatisfactions the count of times that the requirements were satisfied
   * @return the created {@link ErrorMessageFactory}.
   */
  public static <E> ErrorMessageFactory shouldSatisfyOnlyOnce(Iterable<E> actual, int countOfSatisfactions) {
    return new ShouldSatisfyOnlyOnce(actual, countOfSatisfactions);
  }

  private ShouldSatisfyOnlyOnce(Iterable<?> actual, int countOfSatisfactions) {
    super(REQUIREMENTS_SHOULD_BE_SATISFIED_ONLY_ONCE, actual, countOfSatisfactions);
  }
}
