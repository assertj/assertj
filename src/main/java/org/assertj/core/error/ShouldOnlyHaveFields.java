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

import static org.assertj.core.error.ShouldOnlyHaveFields.ErrorType.NOT_EXPECTED_ONLY;
import static org.assertj.core.error.ShouldOnlyHaveFields.ErrorType.NOT_FOUND_ONLY;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

import java.util.Collection;

/**
 * Creates an error message indicating that an assertion that verifies that a class has only the fields.
 *
 * @author Filip Hrisafov
 */
public class ShouldOnlyHaveFields extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldOnlyHaveFields}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected fields for this class
   * @param notFound fields in {@code expected} not found in the {@code actual}.
   * @param notExpected fields in the {@code actual} that were not in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldOnlyHaveFields(Class<?> actual, Collection<String> expected,
                                                            Collection<String> notFound,
                                                            Collection<String> notExpected) {
    return create(actual, expected, notFound, notExpected, false);
  }

  /**
   * Creates a new <code>{@link ShouldOnlyHaveFields}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected fields for this class
   * @param notFound fields in {@code expected} not found in the {@code actual}.
   * @param notExpected fields in the {@code actual} that were not in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldOnlyHaveDeclaredFields(Class<?> actual, Collection<String> expected,
                                                                    Collection<String> notFound,
                                                                    Collection<String> notExpected) {
    return create(actual, expected, notFound, notExpected, true);
  }

  private static ErrorMessageFactory create(Class<?> actual, Collection<String> expected, Collection<String> notFound,
                                            Collection<String> notExpected, boolean declared) {
    if (isNullOrEmpty(notExpected)) {
      return new ShouldOnlyHaveFields(actual, expected, notFound, NOT_FOUND_ONLY, declared);
    }

    if (isNullOrEmpty(notFound)) {
      return new ShouldOnlyHaveFields(actual, expected, notExpected, NOT_EXPECTED_ONLY, declared);
    }

    return new ShouldOnlyHaveFields(actual, expected, notFound, notExpected, declared);
  }

  private ShouldOnlyHaveFields(Class<?> actual, Collection<String> expected, Collection<String> notFound,
                                  Collection<String> notExpected,
                                  boolean declared) {
    super("%n" +
          "Expecting%n" +
          "  <%s>%n" +
          "to only have the following " + (declared ? "declared" : "public accessible") + " fields:%n" +
          "  <%s>%n" +
          "fields not found:%n" +
          "  <%s>%n" +
          "and fields not expected:%n" +
          "  <%s>", actual, expected, notFound, notExpected);
  }

  private ShouldOnlyHaveFields(Class<?> actual, Collection<String> expected,
                                  Collection<String> notFoundOrNotExpected,
                                  ErrorType errorType, boolean declared) {
    super("%n" +
          "Expecting%n" +
          "  <%s>%n" +
          "to only have the following " + (declared ? "declared" : "public accessible") + " fields:%n" +
          "  <%s>%n" +
          (errorType == NOT_FOUND_ONLY ? "but could not find the following fields:%n"
              : "but the following fields were unexpected:%n")
          +
          "  <%s>",
          actual, expected, notFoundOrNotExpected);
  }

  enum ErrorType {
    NOT_FOUND_ONLY, NOT_EXPECTED_ONLY
  }
}
