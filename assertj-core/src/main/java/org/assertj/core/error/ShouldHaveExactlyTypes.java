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

import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

public class ShouldHaveExactlyTypes extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveTypes(Object actual, Iterable<Class<?>> expectedTypes,
                                                    Iterable<Class<?>> expectedTypesNotFoundInActual,
                                                    Iterable<Class<?>> actualTypesNotExpected) {
    if (!isNullOrEmpty(actualTypesNotExpected) && !isNullOrEmpty(expectedTypesNotFoundInActual)) {
      return new ShouldHaveExactlyTypes(actual, expectedTypes, expectedTypesNotFoundInActual, actualTypesNotExpected);
    }
    // empty actualTypesNotExpected means expectedTypesNotFoundInActual is not empty
    boolean expectedTypesNotFoundInActualOnly = isNullOrEmpty(actualTypesNotExpected);
    Iterable<Class<?>> diff = expectedTypesNotFoundInActualOnly ? expectedTypesNotFoundInActual : actualTypesNotExpected;
    return new ShouldHaveExactlyTypes(actual, expectedTypes, diff, expectedTypesNotFoundInActualOnly);
  }

  public static ErrorMessageFactory elementsTypesDifferAtIndex(Object actualElement, Class<?> expectedElement,
                                                               int indexOfDifference) {
    return new ShouldHaveExactlyTypes(actualElement, expectedElement, indexOfDifference);
  }

  private ShouldHaveExactlyTypes(Object actual, Iterable<Class<?>> expected, Iterable<Class<?>> expectedTypesNotFoundInActual,
                                 Iterable<Class<?>> actualTypesNotExpected) {
    super("%n" +
          "Expecting actual elements:%n" +
          "  %s%n" +
          "to have the following types (in this order):%n" +
          "  %s%n" +
          "but there were no actual elements with these types:%n" +
          "  %s%n" +
          "and these actual elements types were not expected:%n" +
          "  %s",
          actual, expected, expectedTypesNotFoundInActual, actualTypesNotExpected);
  }

  private ShouldHaveExactlyTypes(Object actual, Iterable<Class<?>> expected, Iterable<Class<?>> diff,
                                 boolean expectedTypesNotFoundInActualOnly) {
    // @format:off
    super("%n" +
          "Expecting actual elements:%n" +
          "  %s%n" +
          "to have the following types (in this order):%n" +
          "  %s%n" +
          (expectedTypesNotFoundInActualOnly
              ? "but there were no actual elements with these types"
              : "but these actual elements types were not expected") + ":%n" +
          "  %s",
          actual, expected, diff);
    // @format:on
  }

  private ShouldHaveExactlyTypes(Object actualElement, Class<?> expectedType, int indexOfDifference) {
    super("%n" +
          "actual element at index %s does not have the expected type, element was:%s%n" +
          "actual element type: %s%n" +
          "expected type      : %s",
          indexOfDifference, actualElement, actualElement.getClass(), expectedType);
  }

}
