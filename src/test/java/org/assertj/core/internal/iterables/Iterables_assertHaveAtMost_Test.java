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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ElementsShouldHaveAtMost.elementsShouldHaveAtMost;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesWithConditionsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertHaveAtMost(AssertionInfo, Iterable, int, Condition)}</code> .
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
class Iterables_assertHaveAtMost_Test extends IterablesWithConditionsBaseTest {

  @Test
  void should_pass_if_satisfies_at_most_times_condition() {
    actual = newArrayList("Yoda", "Luke", "Leia");
    iterables.assertHaveAtMost(someInfo(), actual, 2, jediPower);
    verify(conditions).assertIsNotNull(jediPower);
  }

  @Test
  void should_pass_if_never_satisfies_condition_() {
    actual = newArrayList("Chewbacca", "Leia", "Obiwan");
    iterables.assertHaveAtMost(someInfo(), actual, 2, jediPower);
    verify(conditions).assertIsNotNull(jediPower);
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      actual = newArrayList("Yoda", "Luke");
      iterables.assertHaveAtMost(someInfo(), actual, 2, null);
    }).withMessage("The condition to evaluate should not be null");
    verify(conditions).assertIsNotNull(null);
  }

  @Test
  void should_fail_if_condition_is_not_met_much() {
    testCondition.shouldMatch(false);
    AssertionInfo info = someInfo();
    actual = newArrayList("Yoda", "Luke", "Obiwan");

    Throwable error = catchThrowable(() -> iterables.assertHaveAtMost(someInfo(), actual, 2, jediPower));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(conditions).assertIsNotNull(jediPower);
    verify(failures).failure(info, elementsShouldHaveAtMost(actual, 2, jediPower));
  }

}
