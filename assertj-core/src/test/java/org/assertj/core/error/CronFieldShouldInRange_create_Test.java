package org.assertj.core.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldCronExpressionBeValid.shouldCronExpressionBeValid;

/**
 * Tests for <code>{@link ShouldCronExpressionBeValid}</code>.
 * @author Neil Wang
 */
class CronFieldShouldInRange_create_Test {

  @Test
  void should_create_error_message_with_expected_type() {
    // WHEN
    String errorMessage = shouldCronExpressionBeValid("1").create();
    // THEN
    then(errorMessage).isEqualTo("\n" +
      "actual cron expression is invalid.\n" +
      "actual was:\n" +
      "  \"1\"\n");
  }
}
