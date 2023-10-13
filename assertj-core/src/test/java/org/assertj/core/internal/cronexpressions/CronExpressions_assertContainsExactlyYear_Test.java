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
 * Tests for <code>{@link org.assertj.core.internal.CronExpressions#assertContainsExactlyYear(AssertionInfo, CronExpression, String...)}</code>.
 * @author Neil Wang
 */
public class CronExpressions_assertContainsExactlyYear_Test extends CronExpressionsBaseTest {

  @ParameterizedTest
  @MethodSource("succeedParams")
  void should_pass_year_contains_exactly(String cron, String[] expects) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    expressions.assertContainsExactlyYear(info, actual, expects);
  }

  public static Stream<Arguments> succeedParams() {
    return Stream.of(
      Arguments.of("* * * * * 2005", new String[]{"2005"}),
      Arguments.of("* * * * * 2005-2009", new String[]{"2005", "2006", "2007", "2008", "2009"}),
      Arguments.of("* * * * * 2000-2023/10", new String[]{"2000", "2010", "2020"})
    );
  }

  @ParameterizedTest
  @MethodSource("failedParams")
  void should_fail_year_contains_exactly(String cron, String[] expects) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    Throwable throwable = catchThrowable(() -> expressions.assertContainsExactlyYear(info, actual, expects));
    assertThat(throwable).isInstanceOf(AssertionError.class);
  }

  public static Stream<Arguments> failedParams() {
    return Stream.of(
      Arguments.of("* * * * * 1,2,3", new String[]{"1", "2"}),
      Arguments.of("* * * * * 2009,2010", new String[]{"2008"}),
      Arguments.of("* * * * * 2000-2023/10", new String[]{"2000", "2010", "2030", "2020"})
    );
  }
}
