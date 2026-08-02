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

import org.jspecify.annotations.Nullable;

/** Creates errors for iterables containing elements satisfying forbidden requirements. */
public class NoElementsShouldSatisfy extends BasicErrorMessageFactory {

  /**
   * Creates an error for an element satisfying forbidden requirements.
   *
   * @param actual the actual iterable
   * @param faultyElement the element satisfying the requirements
   * @return the error message factory
   */
  public static ErrorMessageFactory noElementsShouldSatisfy(@Nullable Object actual, @Nullable Object faultyElement) {
    return new NoElementsShouldSatisfy(actual, faultyElement);
  }

  private NoElementsShouldSatisfy(@Nullable Object actual, @Nullable Object faultyElement) {
    super("%n" +
          "Expecting no elements of:%n" +
          "  %s%n" +
          "to satisfy the given assertions requirements but these elements did:%n" +
          "  %s",
          actual, faultyElement);
  }

}
