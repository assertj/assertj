package org.assertj.core.api.cronexpressions;

import org.assertj.core.api.CronExpressionAssert;
import org.assertj.core.internal.CronExpression;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for <code>{@link CronExpressionAssert#isValid()}</code>.
 *
 * @author Neil Wang
 */
public class CronExpressionAssert_isValid_Test {

  @Test
  public void should_return_this() {
    CronExpressionAssert assertions = new CronExpressionAssert(new CronExpression("* * * * * *"));
    CronExpressionAssert returned = assertions.isValid();
    assertThat(returned).isSameAs(assertions);
  }

  @Test
  public void should_verify_cron_expressions() {
    CronExpressionAssert assertions = new CronExpressionAssert(new CronExpression("* * * * * *"));
    assertions.isValid();
  }

}
