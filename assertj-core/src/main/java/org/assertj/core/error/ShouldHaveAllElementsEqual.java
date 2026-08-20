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

import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that all the elements of a group are equal failed. A group
 * of elements can be a collection, an array or a {@code String}.
 */
public class ShouldHaveAllElementsEqual extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveAllElementsEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param first the first element of {@code actual}, all other elements are expected to be equal to it.
   * @param notEqualElements the elements of {@code actual} that are not equal to {@code first}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return an instance of {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveAllElementsEqual(Object actual, Object first, Object notEqualElements,
                                                               ComparisonStrategy comparisonStrategy) {
    return new ShouldHaveAllElementsEqual(actual, first, notEqualElements, comparisonStrategy);
  }

  private ShouldHaveAllElementsEqual(Object actual, Object first, Object notEqualElements,
                                     ComparisonStrategy comparisonStrategy) {
    super("%nExpecting all elements to be equal to:%n  %s%nbut the following element(s) were not:%n  %s%nin:%n  %s%n%s",
          first, notEqualElements, actual, comparisonStrategy);
  }

}
