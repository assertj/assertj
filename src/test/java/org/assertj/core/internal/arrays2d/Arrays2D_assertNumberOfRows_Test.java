package org.assertj.core.internal.arrays2d;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDimensions.shouldHaveFirstDimension;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class Arrays2D_assertNumberOfRows_Test extends Arrays2D_BaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    char[][] actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertNumberOfRows(someInfo(), failures, actual, 1));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource("arrays2d")
  void should_fail_if_number_of_rows_of_actual_is_not_equal_to_expected_size(char[][] actual) {
    // GIVEN
    int expectedNumberOfRows = 2;
    // WHEN
    AssertionError assertionError = expectAssertionError(
      () -> arrays.assertNumberOfRows(someInfo(), failures, actual, expectedNumberOfRows));
    // THEN
    then(assertionError).hasMessage(shouldHaveFirstDimension(actual, actual.length, expectedNumberOfRows).create());
  }

  @ParameterizedTest
  @MethodSource("arrays2d")
  void should_pass_if_number_of_rows_of_actual_is_equal_to_expected_size(char[][] actual) {
    // GIVEN
    int expectedNumberOfRows = 3;
    // WHEN / THEN
    arrays.assertNumberOfRows(someInfo(), failures, actual, expectedNumberOfRows);
  }

  private static Stream<char[][]> arrays2d() {
    return Stream.of(new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } },
                     new char[][] { { 'a' }, { 'b' }, { 'c', 'd' } },
                     new char[][] { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } });
  }

}
