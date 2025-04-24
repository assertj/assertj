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

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies that requirements are not satisfied only once.
 */
public class ShouldSatisfyOnlyOnce extends BasicErrorMessageFactory {

  private static final String NO_ELEMENT_SATISFIED_REQUIREMENTS = "%nExpecting exactly one element of actual:%n" +
                                                                  "  %s%n" +
                                                                  "to satisfy the requirements but none did";

  // @format:off
  private static final String MORE_THAN_ONE_ELEMENT_SATISFIED_REQUIREMENTS = "%n" +
                                                                             "Expecting exactly one element of actual:%n" +
                                                                             "  %s%n" +
                                                                             "to satisfy the requirements but these %s elements did:%n" +
                                                                             "  %s";
  // @format:on

  /**
   * Creates a new <code>{@link ShouldSatisfyOnlyOnce}</code>.
   *
   * @param <E> the iterable elements type.
   * @param actual the actual iterable in the failed assertion.
   * @param satisfiedElements the elements which satisfied the requirement
   * @return the created {@link ErrorMessageFactory}.
   */
  public static <E> ErrorMessageFactory shouldSatisfyOnlyOnce(Iterable<? extends E> actual, List<? extends E> satisfiedElements) {
    return satisfiedElements.isEmpty() ? new ShouldSatisfyOnlyOnce(actual) : new ShouldSatisfyOnlyOnce(actual, satisfiedElements);
  }

  private ShouldSatisfyOnlyOnce(Iterable<?> actual) {
    super(NO_ELEMENT_SATISFIED_REQUIREMENTS, actual);
  }

  private ShouldSatisfyOnlyOnce(Iterable<?> actual, List<?> satisfiedElements) {
    super(MORE_THAN_ONE_ELEMENT_SATISFIED_REQUIREMENTS, actual, satisfiedElements.size(), satisfiedElements);
  }
}
