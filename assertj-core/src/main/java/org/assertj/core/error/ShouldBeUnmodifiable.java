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

public class ShouldBeUnmodifiable extends BasicErrorMessageFactory {

  private static final String UNEXPECTED_SUCCESS_PATTERN = "%n" +
                                                           "Expecting actual to be unmodifiable, but invoking%n" +
                                                           "  %s%n" +
                                                           "succeeded instead of throwing java.lang.UnsupportedOperationException";

  private static final String UNEXPECTED_FAILURE_PATTERN = "%n" +
                                                           "Expecting actual to be unmodifiable, but invoking%n" +
                                                           "  %s%n" +
                                                           "thrown%n" +
                                                           "  %s%n" +
                                                           "instead of java.lang.UnsupportedOperationException";

  public static ErrorMessageFactory shouldBeUnmodifiable(String method) {
    return new ShouldBeUnmodifiable(method);
  }

  private ShouldBeUnmodifiable(String method) {
    super(UNEXPECTED_SUCCESS_PATTERN, method);
  }

  public static ErrorMessageFactory shouldBeUnmodifiable(String method, RuntimeException cause) {
    return new ShouldBeUnmodifiable(method, cause);
  }

  private ShouldBeUnmodifiable(String method, RuntimeException cause) {
    super(UNEXPECTED_FAILURE_PATTERN, method, cause.toString());
  }

}
