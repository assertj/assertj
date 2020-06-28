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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.SubarraysShouldHaveSameSize.subarraysShouldHaveSameSize;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class SubarraysShouldHaveSameSize_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    int[][] actual = new int[][] { { 1, 2 }, { 3, 999 }, { 4, 5, 6 } };
    int[][] expected = new int[][] { { 1, 2 }, { 3 }, { 4, 5, 6 } };
    int[] actualSubArrayWithDifference = new int[] { 3, 999 };
    int[] expectedSubArrayWithDifference = new int[] { 3 };
    // WHEN
    ErrorMessageFactory errorMessageFactory = subarraysShouldHaveSameSize(actual, expected,
                                                                          actualSubArrayWithDifference,
                                                                          actualSubArrayWithDifference.length,
                                                                          expectedSubArrayWithDifference,
                                                                          expectedSubArrayWithDifference.length, 1);
    // WHEN
    String message = errorMessageFactory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "actual and expected 2d arrays should be deeply equal but rows at index 1 differ:%n" +
                                   "actual[1] size is 2 and expected[1] is 1.%n" +
                                   "actual[1] was:%n" +
                                   "  [3, 999]%n" +
                                   "expected[1] was:%n" +
                                   "  [3]%n" +
                                   "actual was:%n" +
                                   "  [[1, 2], [3, 999], [4, 5, 6]]%n" +
                                   "expected was:%n" +
                                   "  [[1, 2], [3], [4, 5, 6]]"));
  }

}
