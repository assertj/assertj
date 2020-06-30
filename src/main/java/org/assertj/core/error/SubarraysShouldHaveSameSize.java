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

/**
 * Creates an error message indicating that an assertion that verifies that some subarray (in multidimensional arrays)
 * has certain size failed.
 *
 * @author Maciej Wajcht
 */
public class SubarraysShouldHaveSameSize extends BasicErrorMessageFactory {

  private static final String MESSAGE = "%n" +
                                        "actual and expected 2d arrays should be deeply equal but rows at index %s differ:%n" +
                                        "actual[%s] size is %s and expected[%s] is %s.%n" +
                                        "actual[%s] was:%n" +
                                        "  %s%n" +
                                        "expected[%s] was:%n" +
                                        "  %s%n" +
                                        "actual was:%n" +
                                        "  %s%n" +
                                        "expected was:%n" +
                                        "  %s";

  /**
   * Creates a new <code>{@link SubarraysShouldHaveSameSize}</code>.
   * @param actual the actual 2D array in the failed assertion.
   * @param expected the actual 2D array to compare actual with.
   * @param actualSubArray actual[index] array
   * @param actualSubArrayLength actual[index] lentgth
   * @param expectedSubArray expected[index]
   * @param expectedSubArrayLength actual[index] lentgth
   * @param index index of {@code actualSubArray}, e.g. {@code 3} when checking size (length) of {@code actual[3]}
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory subarraysShouldHaveSameSize(Object actual, Object expected, Object actualSubArray,
                                                                int actualSubArrayLength, Object expectedSubArray,
                                                                int expectedSubArrayLength, int index) {
    return new SubarraysShouldHaveSameSize(actual, expected, actualSubArray, actualSubArrayLength, expectedSubArray,
                                           expectedSubArrayLength, index);
  }

  private SubarraysShouldHaveSameSize(Object actual, Object expected, Object actualSubArray, int actualSubArrayLength,
                                      Object expectedSubArray, int expectedSubArrayLength, int index) {
    // reuse %s to let representation format the arrays but don't do it for integers as we want to keep the default toString of
    // int (that would mot be the case if the representation was changed to hex representation for example).
    super(format(MESSAGE, index, index, actualSubArrayLength, index, expectedSubArrayLength, index, "%s", index, "%s", "%s",
                 "%s"),
          actualSubArray, expectedSubArray, actual, expected);
  }

}
