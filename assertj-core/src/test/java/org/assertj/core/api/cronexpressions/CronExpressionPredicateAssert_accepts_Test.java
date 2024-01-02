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
package org.assertj.core.api.cronexpressions;

import org.assertj.core.api.CronExpressionPredicate;
import org.assertj.core.api.CronExpressionPredicateAssert;
import org.assertj.core.internal.CronExpression;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.LongPredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * Test codes for {@link CronExpressionPredicateAssert#accepts(CronExpression...)}.
 * @author Neil Wang
 */
class CronExpressionPredicateAssert_accepts_Test extends CronExpressionPredicateAssertBaseTest {

  @Test
  void should_fail_when_predicate_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((CronExpressionPredicate) null).accepts(new CronExpression("* * * * * *"), new CronExpression("* * * * * ?")))
      .withMessage(actualIsNull());
  }

  @Test
  void should_pass_when_predicate_accepts_value() {
    CronExpressionPredicate predicate = Objects::nonNull;
    assertThat(predicate).accepts(new CronExpression("* * * * * *"));
  }

  @Test
  void should_fail_when_predicate_does_not_accept_values() {
    CronExpressionPredicate predicate = Objects::isNull;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(predicate).accepts(new CronExpression("* * * * * *")));
  }

  @Override
  protected CronExpressionPredicateAssert invoke_api_method() {
    return assertions.accepts(new CronExpression("* * * * * *"), new CronExpression("* * * * * ?"));
  }

  @Override
  protected void verify_internal_effects() {
  }
}
