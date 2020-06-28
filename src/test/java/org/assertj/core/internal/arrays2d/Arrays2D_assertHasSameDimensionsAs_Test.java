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
package org.assertj.core.internal.arrays2d;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameDimensionsAs.shouldHaveSameDimensionsAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Arrays2D_assertHasSameDimensionsAs_Test extends Arrays2D_BaseTest {

  private char[][] actual = new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' } };

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    ThrowingCallable assertion = () -> arrays.assertHasSameDimensionsAs(someInfo(), actual,
                                                                        new String[][] { { "a", "b", "c" }, { "d", "e", "f" } });
    AssertionError error = expectAssertionError(assertion);
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource("should_fail_if_arrays_first_dimensions_differ_parameters")
  public void should_fail_if_arrays_first_dimensions_differ(String[][] other) {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasSameDimensionsAs(info, actual, other));
    // THEN
    then(error).hasMessage(shouldHaveSameDimensionsAs(actual, other, actual.length, other.length).create(info.description(),
                                                                                                         info.representation()));
  }

  static Stream<String[][]> should_fail_if_arrays_first_dimensions_differ_parameters() {
    return Stream.of(new String[][] { { "a", "b" } },
                     new String[][] { { "a", "b" }, { "c", "d" }, { "e", "f" } },
                     new String[][] { { "a", "b" }, { "c", "d" }, { "e", "f" }, { "g", "h" } });
  }

  @ParameterizedTest
  @MethodSource("should_fail_if_any_array_rows_dimensions_differ_parameters")
  public void should_fail_if_any_array_rows_dimensions_differ(String[][] other, int rowIndex, int expectedRowSize,
                                                              Object actualRow, Object expectedRow) {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertHasSameDimensionsAs(info, actual, other));
    // THEN
    ErrorMessageFactory factory = shouldHaveSameDimensionsAs(rowIndex, 3, expectedRowSize, actualRow, expectedRow, actual, other);
    then(assertionError).hasMessage(factory.create(info.description(), info.representation()));
  }

  static Stream<Arguments> should_fail_if_any_array_rows_dimensions_differ_parameters() {
    return Stream.of(arguments(new String[][] { { "a", "b" }, { "c", "d" } },
                               0, 2, array('a', 'b', 'c'), array("a", "b")),
                     arguments(new String[][] { { "a", "b" }, { "d", "e", "f" } },
                               0, 2, array('a', 'b', 'c'), array("a", "b")),
                     arguments(new String[][] { { "a", "b", "c" }, { "d", "e" } },
                               1, 2, array('d', 'e', 'f'), array("d", "e")),
                     arguments(new String[][] { { "a", "b", "c" }, { "d", "e", "f", "g" } },
                               1, 4, array('d', 'e', 'f'), array("d", "e", "f", "g")),
                     arguments(new String[][] { { "a", "b", "c", "d" }, { "e", "f", "g", "h" } },
                               0, 4, array('a', 'b', 'c'), array("a", "b", "c", "d")));
  }

  @Test
  public void should_pass_if_arrays_have_the_dimensions() {
    arrays.assertHasSameDimensionsAs(someInfo(), actual, new String[][] { { "a", "b", "c" }, { "d", "e", "f" } });
  }
}
