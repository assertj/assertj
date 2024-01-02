package org.assertj.core.internal.cronexpressions;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CronExpression;
import org.assertj.core.internal.CronExpressionsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.test.TestData.someInfo;

/**
 * Tests for <code>{@link org.assertj.core.internal.CronExpressions#assertContainsExactlyDayOfMonth(AssertionInfo, CronExpression, String...)}</code>.
 * @author Neil Wang
 */
public class CronExpressions_assertContainsExactlyDayOfMonth_Test extends CronExpressionsBaseTest {

  @ParameterizedTest
  @MethodSource("succeedParams")
  void should_pass_day_of_month_contains_exactly(String cron, String[] expects) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    expressions.assertContainsExactlyDayOfMonth(info, actual, expects);
  }

  public static Stream<Arguments> succeedParams() {
    return Stream.of(
      Arguments.of("* * * 1 * *", new String[]{"1"}),
      Arguments.of("* * * LW * *", new String[]{"LW"}),
      Arguments.of("* * * 1/7 * *", new String[]{"1", "8", "15", "22", "29"})
    );
  }

  @ParameterizedTest
  @MethodSource("failedParams")
  void should_fail_day_of_month_contains_exactly(String cron, String[] expects) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    Throwable throwable = catchThrowable(() -> expressions.assertContainsExactlyDayOfMonth(info, actual, expects));
    assertThat(throwable).isInstanceOf(AssertionError.class);
  }

  public static Stream<Arguments> failedParams() {
    return Stream.of(
      Arguments.of("* * * 1,2,3 * *", new String[]{"1", "2"}),
      Arguments.of("* * * 1,2,3 * *", new String[]{"1", "4"}),
      Arguments.of("* * * 0/7 * *", new String[]{"1", "15", "8", "22", "29"})
    );
  }
}
