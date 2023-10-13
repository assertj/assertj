package org.assertj.core.internal.cronexpressions;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CronExpression;
import org.assertj.core.internal.CronExpressions;
import org.assertj.core.internal.CronExpressionsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldCronExpressionBeValid.shouldCronExpressionBeValid;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link CronExpressions#assertIsValid(AssertionInfo, CronExpression)}</code>.
 * @author Neil Wang
 */
public class CronExpressions_assertIsValid_Test extends CronExpressionsBaseTest {

  @ParameterizedTest
  @MethodSource("failedParams")
  void should_fail_if_actual_cron_expression_is_not_valid(String cron, String throwMessage) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    Throwable error = catchThrowable(() -> expressions.assertIsValid(info, actual));
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldCronExpressionBeValid(throwMessage));
  }

  public static Stream<Arguments> failedParams() {
    return Stream.of(
      Arguments.of("*", "*"),
      Arguments.of("0 0 2 1-0 * *", "1-0")
    );
  }

  @ParameterizedTest
  @MethodSource("succeedParams")
  void should_pass_if_actual_cron_expression_is_valid(String cron) {
    AssertionInfo info = someInfo();
    CronExpression actual = new CronExpression(cron);
    expressions.assertIsValid(info, actual);
  }

  public static Stream<Arguments> succeedParams() {
    return Stream.of(
      Arguments.of("@yearly"),
      Arguments.of("@annually"),
      Arguments.of("@monthly"),
      Arguments.of("@daily"),
      Arguments.of("@midnight"),
      Arguments.of("@hourly"),
      Arguments.of("* * * * * *"),
      Arguments.of("0 0 2 1 * *"),
      Arguments.of("0 15 10 ? * MON-FRI"),
      Arguments.of("0 15 10 ? * 6L"),
      Arguments.of("0 0 10,14,16 * * ?"),
      Arguments.of("0 0/30 9-17 * * ?"),
      Arguments.of("0 0 12 ? * WED"),
      Arguments.of("0 0 12 * * ?"),
      Arguments.of("0,1,2,3,4 15-18/5 10-10/2 ? * *"),
      Arguments.of("0 15 10 * * 2005"),
      Arguments.of("0 15 10 ? * 6#3")
    );
  }


}
