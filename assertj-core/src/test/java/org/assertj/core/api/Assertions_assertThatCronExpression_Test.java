package org.assertj.core.api;

import org.assertj.core.internal.CronExpression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldCronExpressionBeValid.shouldCronExpressionBeValid;

/**
 * Tests for code {@link Assertions#assertThat(CronExpression)}.
 * @author Neil Wang
 */
public class Assertions_assertThatCronExpression_Test {

  @ParameterizedTest
  @MethodSource("failedParams")
  void should_fail_if_actual_cron_expression_is_not_valid(String cron, String throwMessage) {
    CronExpression actual = new CronExpression(cron);
    Throwable error = catchThrowable(() -> assertThat(actual).isValid());
    assertThat(error).isInstanceOf(AssertionError.class)
      .hasMessage(shouldCronExpressionBeValid(throwMessage).create());
  }

  public static Stream<Arguments> failedParams() {
    return Stream.of(
      Arguments.of("*", "*"),
      Arguments.of("0 0 2 1-0 * *", "1-0")
    );
  }

  @ParameterizedTest
  @MethodSource("succeedParams")
  void should_pass_if_actual_cron_expression_is_valid(String cron, String[] expectSeconds, String[] expectedMinutes, String[] expectedHours,
                                                      String[] expectedDayOfMonth, String[] expectedMonth, boolean useYear,
                                                      String[] expectedYearOrDayOfWeek) {
    CronExpression actual = new CronExpression(cron);
    assertThat(actual).isValid();
    assertThat(actual).containsExactlySeconds(expectSeconds)
      .containsExactlyMinutes(expectedMinutes)
      .containsExactlyHours(expectedHours)
      .containsExactlyDayOfMonth(expectedDayOfMonth)
      .containsExactlyMonth(expectedMonth);
    if (useYear) {
      assertThat(actual).containsExactlyYear(expectedYearOrDayOfWeek);
    } else {
      assertThat(actual).containsExactlyDayOfWeek(expectedYearOrDayOfWeek);
    }
  }

  public static Stream<Arguments> succeedParams() {
    return Stream.of(
      Arguments.of("@yearly",
        new String[]{"0"}, new String[]{"0"}, new String[]{"0"}, new String[]{"1"}, new String[]{"1"}, false, new String[]{"*"}),
      Arguments.of("@annually",
        new String[]{"0"}, new String[]{"0"}, new String[]{"0"}, new String[]{"1"}, new String[]{"1"}, false, new String[]{"*"}),
      Arguments.of("@monthly",
        new String[]{"0"}, new String[]{"0"}, new String[]{"0"}, new String[]{"1"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("@daily",
        new String[]{"0"}, new String[]{"0"}, new String[]{"0"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("@midnight",
        new String[]{"0"}, new String[]{"0"}, new String[]{"0"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("@hourly",
        new String[]{"0"}, new String[]{"0"}, new String[]{"*"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("* * * * * *",
        new String[]{"*"}, new String[]{"*"}, new String[]{"*"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("0 0 2 1 * *",
        new String[]{"0"}, new String[]{"0"}, new String[]{"2"}, new String[]{"1"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("0 15 10 ? * MON-FRI",
        new String[]{"0"}, new String[]{"15"}, new String[]{"10"}, new String[]{"?"}, new String[]{"*"}, false, new String[]{"1", "2", "3", "4", "5"}),
      Arguments.of("0 15 10 ? * 6L",
        new String[]{"0"}, new String[]{"15"}, new String[]{"10"}, new String[]{"?"}, new String[]{"*"}, false, new String[]{"6L"}),
      Arguments.of("0 0 10,14,16 * * ?",
        new String[]{"0"}, new String[]{"0"}, new String[]{"10", "14", "16"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"?"}),
      Arguments.of("0 0/30 9-11 * * ?",
        new String[]{"0"}, new String[]{"0", "30"}, new String[]{"9", "10", "11"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"?"}),
      Arguments.of("0 0 12 ? * WED",
        new String[]{"0"}, new String[]{"0"}, new String[]{"12"}, new String[]{"?"}, new String[]{"*"}, false, new String[]{"3"}),
      Arguments.of("0 0 12 * * ?",
        new String[]{"0"}, new String[]{"0"}, new String[]{"12"}, new String[]{"*"}, new String[]{"*"}, false, new String[]{"?"}),
      Arguments.of("0,1,2,3,4 15-18/5 10-10/2 ? * *",
        new String[]{"0", "1", "2", "3", "4"}, new String[]{"15"}, new String[]{"10"}, new String[]{"?"}, new String[]{"*"}, false, new String[]{"*"}),
      Arguments.of("0 15 10 * * 2005",
        new String[]{"0"}, new String[]{"15"}, new String[]{"10"}, new String[]{"*"}, new String[]{"*"}, true, new String[]{"2005"}),
      Arguments.of("0 15 10 ? * 6#3",
        new String[]{"0"}, new String[]{"15"}, new String[]{"10"}, new String[]{"?"}, new String[]{"*"}, false, new String[]{"6#3"})
    );
  }
}
