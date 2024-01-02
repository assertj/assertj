/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that
 * {@code CronField} or {@code CornExpression} should valid.
 * @author Neil Wang
 */
public class ShouldCronExpressionBeValid extends BasicErrorMessageFactory {

  private static final String MESSAGE = "%n" +
    "actual cron expression is invalid.%n" +
    "actual was:%n" +
    "  %s%n";

  /**
   * Creates a new <code>{@link ShouldCronExpressionBeValid}</code>.
   * @param actual the actual cron field or cron expression.
   * @return the created {@code ErrorMessageFactory}
   */
  public static ErrorMessageFactory shouldCronExpressionBeValid(Object actual) {
    return new ShouldCronExpressionBeValid(actual);
  }

  private ShouldCronExpressionBeValid(Object actual) {
    super(MESSAGE, actual);
  }

}
