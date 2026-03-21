/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static org.assertj.core.error.UnsatisfiedRequirement.describeErrors;

import java.util.List;

import org.assertj.core.api.AssertionInfo;

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
   * Creates a new <code>{@link ShouldSatisfyOnlyOnce}</code> with per-element failure details.
   *
   * @param <E> the iterable elements type.
   * @param actual the actual iterable in the failed assertion.
   * @param satisfiedElements the elements which satisfied the requirement
   * @param unsatisfiedRequirements the per-element failure details
   * @param info the assertion info
   * @return the created {@link ErrorMessageFactory}.
   */
  public static <E> ErrorMessageFactory shouldSatisfyOnlyOnce(Iterable<? extends E> actual,
                                                              List<? extends E> satisfiedElements,
                                                              List<UnsatisfiedRequirement> unsatisfiedRequirements,
                                                              AssertionInfo info) {
    if (satisfiedElements.isEmpty()) {
      return new ShouldSatisfyOnlyOnce(actual, unsatisfiedRequirements, info);
    }
    return new ShouldSatisfyOnlyOnce(actual, satisfiedElements, unsatisfiedRequirements, info);
  }

  // 0 satisfied — show all per-element errors
  private ShouldSatisfyOnlyOnce(Iterable<?> actual, List<UnsatisfiedRequirement> unsatisfiedRequirements,
                                AssertionInfo info) {
    super(NO_ELEMENT_SATISFIED_REQUIREMENTS + "%n%n" + describeErrors(unsatisfiedRequirements, info), actual);
  }

  // 2+ satisfied — show which passed + why others failed
  private ShouldSatisfyOnlyOnce(Iterable<?> actual, List<?> satisfiedElements,
                                List<UnsatisfiedRequirement> unsatisfiedRequirements, AssertionInfo info) {
    super(MORE_THAN_ONE_ELEMENT_SATISFIED_REQUIREMENTS
          + (unsatisfiedRequirements.isEmpty() ? ""
              : "%n%nElements which did not satisfy the requirements:%n%n"
                + describeErrors(unsatisfiedRequirements, info)),
          actual, satisfiedElements.size(), satisfiedElements);
  }

}
