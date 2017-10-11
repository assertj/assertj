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

import static org.assertj.core.error.ShouldContainOnlyNulls.ErrorType.EMPTY;
import static org.assertj.core.error.ShouldContainOnlyNulls.ErrorType.NON_NULL_ELEMENTS;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains only null elements failed. A group
 * of elements can be a collection or an array.
 *
 * @author Billy Yuan
 */
public class ShouldContainOnlyNulls extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainOnlyNulls}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyNulls(Object actual) {
    return new ShouldContainOnlyNulls(actual, EMPTY, null);
  }

  public static ErrorMessageFactory shouldContainOnlyNulls(Object actual, Iterable<?> nonNullElements) {
    return new ShouldContainOnlyNulls(actual, NON_NULL_ELEMENTS, nonNullElements);
  }

  private ShouldContainOnlyNulls(Object actual, ErrorType errorType, Iterable<?> notExpected) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to contain only null elements but " + describe(errorType),
          actual, notExpected);
  }

  private static String describe(ErrorType errorType) {
    switch (errorType) {
    case EMPTY:
      return "it was empty";
    case NON_NULL_ELEMENTS:
    default:
      return "some elements were not:%n  <%s>";
    }
  }

  public enum ErrorType {
    EMPTY, NON_NULL_ELEMENTS
  }
}
