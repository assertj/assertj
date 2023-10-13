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
 * Tests for <code>{@link org.assertj.core.internal.CronExpressions#assertContainsExactlyDayOfWeek(AssertionInfo, CronExpression, String...)}</code>.
 * @author Neil Wang
 */
public class CronExpressions_assertContainsExactlyDayOfWeek_Test extends CronExpressionsBaseTest {

  @ParameterizedTest
  @MethodSource("succeedParams")
  void should_pass_day_of_week_contains_exactly(String cron, String[] expects) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    expressions.assertContainsExactlyDayOfWeek(info, actual, expects);
  }

  public static Stream<Arguments> succeedParams() {
    return Stream.of(
      Arguments.of("* * * * * 1", new String[]{"1"}),
      Arguments.of("* * * * * 6#2", new String[]{"6#2"}),
      Arguments.of("* * * * * MON-FRI", new String[]{"1", "2", "3", "4", "5"}),
      Arguments.of("* * * * * 1/3", new String[]{"1", "4", "7"})
    );
  }

  @ParameterizedTest
  @MethodSource("failedParams")
  void should_fail_day_of_week_contains_exactly(String cron, String[] expects) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    Throwable throwable = catchThrowable(() -> expressions.assertContainsExactlyDayOfWeek(info, actual, expects));
    assertThat(throwable).isInstanceOf(AssertionError.class);
  }

  public static Stream<Arguments> failedParams() {
    return Stream.of(
      Arguments.of("* * * * * 1,2,3", new String[]{"1", "2"}),
      Arguments.of("* * * * * 1,2,3", new String[]{"1", "4"}),
      Arguments.of("* * * * * 1/3", new String[]{"1", "7", "4"}),
      Arguments.of("* * * * * 2005", new String[]{"2005"})
    );
  }
}
