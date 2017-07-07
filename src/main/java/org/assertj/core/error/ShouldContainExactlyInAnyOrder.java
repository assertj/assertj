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
package org.assertj.core.error;

import org.assertj.core.internal.*;

import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.ErrorType.*;
import static org.assertj.core.util.IterableUtil.*;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains exactly a given set
 * of values and nothing else failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Lovro Pandzic
 */
public class ShouldContainExactlyInAnyOrder extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainExactlyInAnyOrder}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainExactlyInAnyOrder(Object actual, Object expected, Iterable<?> notFound,
                                                                   Iterable<?> notExpected, ComparisonStrategy comparisonStrategy) {
    if (isNullOrEmpty(notExpected)) {
      return new ShouldContainExactlyInAnyOrder(actual, expected, notFound, NOT_FOUND_ONLY, comparisonStrategy);
    }

    if (isNullOrEmpty(notFound)) {
      return new ShouldContainExactlyInAnyOrder(actual, expected, notExpected, NOT_EXPECTED_ONLY, comparisonStrategy);
    }

    return new ShouldContainExactlyInAnyOrder(actual, expected, notFound, notExpected, comparisonStrategy);
  }

  private ShouldContainExactlyInAnyOrder(Object actual, Object expected, Iterable<?> notFound, Iterable<?> notExpected,
                                         ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to contain exactly in any order:%n" +
          "  <%s>%n" +
          "elements not found:%n" +
          "  <%s>%n" +
          "and elements not expected:%n" +
          "  <%s>%n%s", actual,
          expected, notFound, notExpected, comparisonStrategy);
  }

  private ShouldContainExactlyInAnyOrder(Object actual, Object expected, Iterable<?> notFoundOrNotExpected, ErrorType errorType,
                                         ComparisonStrategy comparisonStrategy) {
    // @format:off
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to contain exactly in any order:%n" +
          "  <%s>%n" + (errorType == NOT_FOUND_ONLY ?
          "but could not find the following elements:%n" : "but the following elements were unexpected:%n") +
          "  <%s>%n%s",
          actual, expected, notFoundOrNotExpected, comparisonStrategy);
    // @format:on
  }

  public enum ErrorType {
    NOT_FOUND_ONLY, NOT_EXPECTED_ONLY
  }
}
