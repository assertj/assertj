package org.assertj.core.internal.cronexpressions;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CronField;
import org.assertj.core.internal.CronFieldValidation;
import org.assertj.core.internal.CronUnit;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldCronExpressionBeValid.shouldCronExpressionBeValid;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.CronUnit.*;
import static org.assertj.core.test.TestData.someInfo;

/**
 * Tests for <code>{@link CronFieldValidation#assertIsValid(AssertionInfo, CronField, org.assertj.core.internal.Failures)}</code>.
 * @author Neil Wang
 */
public class CronFieldValidation_assertIsValid_Test {

  @ParameterizedTest
  @MethodSource("succeedParams")
  void should_pass_when_cron_field_is_valid(String cronField, CronUnit cronUnit) {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN THEN
    CronFieldValidation.instance().assertIsValid(info, new CronField(cronUnit, cronField), Failures.instance());
  }

  public static Stream<Arguments> succeedParams() {
    return Stream.of(
      Arguments.of("*", HOURS),
      Arguments.of("5", HOURS),
      Arguments.of("1,2", HOURS),
      Arguments.of("1-5", HOURS),
      Arguments.of("1,2,3-6", HOURS),
      Arguments.of("1,3-7/100", HOURS),
      Arguments.of("?", YEAR),
      Arguments.of("?", DAY_OF_WEEK),
      Arguments.of("JAN-MAY", MONTH),
      Arguments.of("mon", DAY_OF_WEEK),
      Arguments.of("?", DAY_OF_MONTH),
      Arguments.of("L", DAY_OF_MONTH),
      Arguments.of("2001-2005", YEAR)
    );
  }

  @ParameterizedTest
  @MethodSource("failedParams")
  void should_fail_when_cron_field_is_invalid(String cronField, CronUnit cronUnit, String expectedMessage) {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    Throwable throwable = catchThrowable(() -> CronFieldValidation.instance().assertIsValid(info, new CronField(cronUnit, cronField), Failures.instance()));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
      .hasMessageContaining(expectedMessage);
  }

  public static Stream<Arguments> failedParams() {
    return Stream.of(
      Arguments.of("-1", HOURS, shouldCronExpressionBeValid("-1").create()),
      Arguments.of("1,-1", HOURS, shouldCronExpressionBeValid("1,-1").create()),
      Arguments.of("e", HOURS, shouldCronExpressionBeValid("e").create()),
      Arguments.of("1,e", HOURS, shouldCronExpressionBeValid("1,e").create()),
      Arguments.of("1,2-e", HOURS, shouldCronExpressionBeValid("1,2-e").create()),
      Arguments.of("1,3-7/10/2", HOURS, shouldCronExpressionBeValid("1,3-7/10/2").create()),
      Arguments.of("1-2-3", HOURS, shouldCronExpressionBeValid("1-2-3").create()),
      Arguments.of("3-1", HOURS, shouldCronExpressionBeValid("3-1").create()),
      Arguments.of("?", HOURS, shouldCronExpressionBeValid("?").create())
    );
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    Throwable throwable = catchThrowable(() -> CronFieldValidation.instance().assertIsValid(info, null, Failures.instance()));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
      .hasMessageContaining(shouldNotBeNull("actual").create());
  }

  @Test
  void should_fail_if_value_in_actual_is_null() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    Throwable throwable = catchThrowable(() -> CronFieldValidation.instance().assertIsValid(info, new CronField(HOURS, null), Failures.instance()));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
      .hasMessageContaining(shouldNotBeNull("actual").create());
  }

  @Nested
  class CronUnit_matchesPattern_Test {

    @ParameterizedTest
    @CsvSource({
      "SECONDS,       *",
      "MINUTES,       *",
      "HOURS,         *",
      "DAY_OF_MONTH,  *",
      "DAY_OF_MONTH,  L",
      "DAY_OF_MONTH,  ?",
      "DAY_OF_MONTH,  6W",
      "DAY_OF_MONTH,  12W",
      "DAY_OF_MONTH,  31W",
      "DAY_OF_MONTH,  LW",
      "MONTH,         *",
      "DAY_OF_WEEK,   *",
      "DAY_OF_WEEK,   ?",
      "DAY_OF_WEEK,   1#1",
      "DAY_OF_WEEK,   6#10",
      "DAY_OF_WEEK,   6L",
      "YEAR,          *",
      "YEAR,          ?"
    })
    void should_matches_pattern(CronUnit unit, String actual) {
      assertThat(Pattern.matches(unit.wildcardPattern(), actual)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
      "SECONDS,       abc",
      "MINUTES,       2,3",
      "HOURS,         ef",
      "DAY_OF_MONTH,  WL",
      "DAY_OF_MONTH,  W",
      "DAY_OF_MONTH,  ?a",
      "DAY_OF_MONTH,  -2W",
      "DAY_OF_MONTH,  32W",
      "DAY_OF_WEEK,   -1#1",
      "DAY_OF_WEEK,   1#",
      "DAY_OF_WEEK,   #2",
      "DAY_OF_WEEK,   3#a",
      "DAY_OF_WEEK,   8L",
    })
    void should_fail_to_matches_pattern(CronUnit unit, String actual) {
      assertThat(Pattern.matches(unit.wildcardPattern(), actual)).isFalse();
    }

  }

}
