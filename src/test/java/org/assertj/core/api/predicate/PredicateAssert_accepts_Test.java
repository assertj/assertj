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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;

import org.assertj.core.api.PredicateAssert;
import org.assertj.core.api.PredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
public class PredicateAssert_accepts_Test extends PredicateAssertBaseTest {

  @Test
  public void should_fail_when_predicate_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Predicate<String>) null).accepts("first", "second"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_values() {
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ballSportPredicate).accepts("football",
                                                                                                            "basketball",
                                                                                                            "curling"))
                                                   .withMessage(elementsShouldMatch(newArrayList("football",
                                                                                                 "basketball",
                                                                                                 "curling"),
                                                                                    "curling",
                                                                                    PredicateDescription.GIVEN).create());
  }

  @Test
  public void should_pass_when_predicate_accepts_all_values() {
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");

    assertThat(ballSportPredicate).accepts("football", "basketball", "handball");
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_value() {
    Predicate<String> predicate = val -> val.equals("something");
    String expectedValue = "something else";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(predicate).accepts(expectedValue))
                                                   .withMessage(shouldAccept(predicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  public void should_fail_when_predicate_does_not_accept_value_with_string_description() {
    Predicate<String> predicate = val -> val.equals("something");
    String expectedValue = "something else";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(predicate).as("test").accepts(expectedValue))
                                                   .withMessage("[test] " + shouldAccept(predicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  public void should_pass_when_predicate_accepts_value() {
    Predicate<String> predicate = val -> val.equals("something");

    assertThat(predicate).accepts("something");
  }

  @Test
  public void should_pass_and_only_invoke_predicate_once_for_single_value() {
    // GIVEN
    Predicate<Object> predicate = mock(Predicate.class);
    when(predicate.test(any())).thenReturn(true);
    // WHEN
    assertThat(predicate).accepts("something");
    // THEN
    verify(predicate, times(1)).test("something");
  }


  @Override
  protected PredicateAssert<Boolean> invoke_api_method() {
    return assertions.accepts(true, true);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(true, true), getActual(assertions),
                                     PredicateDescription.GIVEN);
  }

}
