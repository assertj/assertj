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
package org.assertj.core.api.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.function.Predicate;

import org.assertj.core.api.PredicateAssert;
import org.assertj.core.api.PredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
class PredicateAssert_acceptsAll_Test extends PredicateAssertBaseTest {

  @Test
  void should_fail_when_predicate_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Predicate<String>) null).acceptsAll(newArrayList("first",
                                                                                                                                  "second")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_values() {
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ballSportPredicate).acceptsAll(newArrayList("football",
                                                                                                                            "basketball",
                                                                                                                            "curling")))
                                                   .withMessage(elementsShouldMatch(newArrayList("football",
                                                                                                 "basketball",
                                                                                                 "curling"),
                                                                                    "curling",
                                                                                    PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_all_values() {
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");

    assertThat(ballSportPredicate).acceptsAll(newArrayList("football", "basketball", "handball"));
  }

  @Override
  protected PredicateAssert<Boolean> invoke_api_method() {
    return assertions.acceptsAll(newArrayList(true, true));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(true, true), getActual(assertions),
                                     PredicateDescription.GIVEN);
  }
}
