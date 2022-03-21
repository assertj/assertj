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
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
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
class PredicateAssert_rejectsAllTest extends PredicateAssertBaseTest {

  @Test
  void should_fail_when_predicate_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Predicate<String>) null).rejectsAll(newArrayList("first",
                                                                                                                                  "second")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_when_predicate_accepts_some_value() {
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ballSportPredicate).rejectsAll(newArrayList("curling",
                                                                                                                            "judo",
                                                                                                                            "football")))
                                                   .withMessage(noElementsShouldMatch(newArrayList("curling", "judo",
                                                                                                   "football"),
                                                                                      "football",
                                                                                      PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_no_value() {
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");

    assertThat(ballSportPredicate).rejectsAll(newArrayList("curling", "judo", "marathon"));
  }

  @Override
  protected PredicateAssert<Boolean> invoke_api_method() {
    return assertions.rejectsAll(newArrayList(false, false));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertNoneMatch(getInfo(assertions), newArrayList(false, false), getActual(assertions),
                                      PredicateDescription.GIVEN);
  }
}
