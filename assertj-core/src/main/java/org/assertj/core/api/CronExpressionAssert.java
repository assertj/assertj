package org.assertj.core.api;

import org.assertj.core.internal.CronExpression;

/**
 * Assertion methods for {@link CronExpression}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(CronExpression)}</code> or
 * <code>{@link Assertions#assertThatCronExpression(String)}</code>.
 * </p>
 * @author Neil Wang
 */
public class CronExpressionAssert extends AbstractCronExpressionAssert<CronExpressionAssert> {

  public CronExpressionAssert(CronExpression cronExpression) {
    super(cronExpression, CronExpressionAssert.class);
  }

}
