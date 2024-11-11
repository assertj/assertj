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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.VerboseCondition.verboseCondition;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VerboseConditionTest {

  private static final Condition<String> VERBOSE_CONDITION = verboseCondition(actual -> actual.length() < 4,
                                                                              "shorter than 4",
                                                                              s -> format(" but length was %s", s.length(), s));

  @Test
  public void should_succeed_and_display_description_without_actual() {
    assertThat(VERBOSE_CONDITION.matches("foo")).isTrue();
    assertThat(VERBOSE_CONDITION).hasToString("shorter than 4");
  }

  @Test
  public void should_fail_and_display_actual_description_as_per_transformation_function_with_isCondition() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("foooo").is(VERBOSE_CONDITION));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  \"foooo\"%n" +
                                           "to be shorter than 4 but length was 5"));
  }

  @Test
  public void should_fail_and_display_actual_description_as_per_transformation_function_with_hasCondition() {
    // GIVEN
    Condition<String> shortLength = verboseCondition(actual -> actual.length() < 4,
                                                     "length shorter than 4",
                                                     s -> format(" but length was %s", s.length(), s));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("foooo").has(shortLength));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  \"foooo\"%n" +
                                           "to have length shorter than 4 but length was 5"));
  }

  @Test
  public void multiple_matches_should_not_change_description() {
    VERBOSE_CONDITION.matches("foooo");
    assertThat(VERBOSE_CONDITION).hasToString("shorter than 4 but length was 5");
    VERBOSE_CONDITION.matches("foooo");
    VERBOSE_CONDITION.matches("foooo");
    assertThat(VERBOSE_CONDITION).hasToString("shorter than 4 but length was 5");
  }

  @Test
  public void should_throw_NullPointerException_if_condition_predicate_is_null() {
    assertThatNullPointerException().isThrownBy(() -> verboseCondition(null, "description", t -> ""));
  }

  @Test
  public void should_throw_NullPointerException_if_objectUnderTestDescriptor_parameter_is_null() {
    assertThatNullPointerException().isThrownBy(() -> verboseCondition(s -> s != null, "shorter than 4", null));
  }

}
