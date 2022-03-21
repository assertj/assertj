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
package org.assertj.core.api.intpredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.assertj.core.api.IntPredicateAssert;
import org.assertj.core.api.IntPredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
class IntPredicateAssert_accepts_Test extends IntPredicateAssertBaseTest {

  @Test
  void should_fail_when_predicate_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((IntPredicate) null).accepts(1, 2, 3))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value() {
    IntPredicate predicate = val -> val <= 2;
    Predicate<Integer> wrapPredicate = predicate::test;
    int expectedValue = 3;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(predicate).accepts(expectedValue))
                                                   .withMessage(shouldAccept(wrapPredicate, expectedValue,
                                                                             PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value_with_string_description() {
    IntPredicate predicate = val -> val <= 2;
    Predicate<Integer> wrapPredicate = predicate::test;
    int expectedValue = 3;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(predicate).as("test").accepts(expectedValue))
                                                   .withMessage("[test] " + shouldAccept(wrapPredicate, expectedValue,
                                                                                         PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_value() {
    IntPredicate predicate = val -> val <= 2;

    assertThat(predicate).accepts(1);
  }

  @Test
  void should_fail_when_predicate_does_not_accept_values() {
    IntPredicate predicate = val -> val <= 2;
    int[] matchValues = new int[] { 1, 2, 3 };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(predicate).accepts(matchValues))
                                                   .withMessage(elementsShouldMatch(matchValues, 3,
                                                                                    PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_all_values() {
    IntPredicate predicate = val -> val <= 2;

    assertThat(predicate).accepts(1, 2);
  }

  @Override
  protected IntPredicateAssert invoke_api_method() {
    return assertions.accepts(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(1, 2), wrapped, PredicateDescription.GIVEN);
  }
}
