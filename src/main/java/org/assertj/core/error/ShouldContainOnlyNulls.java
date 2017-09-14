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

import static org.assertj.core.error.ShouldContainOnlyNulls.ErrorType.NOT_EXPECTED_ONLY;
import static org.assertj.core.error.ShouldContainOnlyNulls.ErrorType.NOT_FOUND_ONLY;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

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
   * @param notExpected the values that are not null elements in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyNulls(Object actual, Iterable<?> notExpected) {
    if (isNullOrEmpty(notExpected))
      return new ShouldContainOnlyNulls(actual, notExpected, NOT_FOUND_ONLY);
    else return new ShouldContainOnlyNulls(actual, notExpected, NOT_EXPECTED_ONLY);
  }

  private ShouldContainOnlyNulls(Object actual, Iterable<?> notExpected, ErrorType errorType) {
    super("%n" +
      "Expecting:%n" +
        "  <%s>%n" +
        "to contain only:%n" +
        "  <null> element %n" + (errorType == NOT_FOUND_ONLY ?
        "but no any element found" : "but the following elements were unexpected:%n" +
        "  <%s>%n"), actual, notExpected);
  }

  public enum ErrorType {
    NOT_FOUND_ONLY, NOT_EXPECTED_ONLY
  }
}
