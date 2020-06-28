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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

public class ShouldHaveSameDimensionsAs extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSameDimensionsAs}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param expectedSize the expected size.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSameDimensionsAs(Object actual, Object expected, Object actualSize,
                                                               Object expectedSize) {
    return new ShouldHaveSameDimensionsAs(actual, expected, actualSize, expectedSize);
  }

  public static ErrorMessageFactory shouldHaveSameDimensionsAs(int rowIndex, int actualRowSize, int expectedRowSize,
                                                               Object actualRow, Object expectedRow, Object actual,
                                                               Object expected) {
    return new ShouldHaveSameDimensionsAs(rowIndex, actualRowSize, expectedRowSize, actualRow, expectedRow, actual, expected);
  }

  private ShouldHaveSameDimensionsAs(Object actual, Object expected, Object actualSize, Object expectedSize) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%n"
                 + "Actual and expected should have same dimensions but actual and expected have different number of rows.%n"
                 + "Actual has %s rows while expected has %s.%n"
                 + "Actual was:%n"
                 + "  %s%n"
                 + "Expected was:%n"
                 + "  %s", actualSize, expectedSize, "%s", "%s"),
          actual, expected);
  }

  private ShouldHaveSameDimensionsAs(int rowIndex, int actualRowSize, int expectedRowSize, Object actualRow, Object expectedRow,
                                     Object actual, Object expected) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("%n"
                 + "Actual and expected should have same dimensions but rows at index %d don't have the same size.%n"
                 + "Actual row size is %d while expected row size is %d%n"
                 + "Actual row was:%n"
                 + " %s%n"
                 + "Expected row was:%n"
                 + " %s%n"
                 + "Actual was:%n"
                 + " %s%n"
                 + "Expected was:%n"
                 + " %s",
                 rowIndex, actualRowSize, expectedRowSize, "%s", "%s", "%s", "%s"),
          actualRow, expectedRow, actual, expected);
  }
}
