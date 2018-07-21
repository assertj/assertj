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
package org.assertj.core.api.localdate;

import static java.lang.String.format;
import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LocalDateAssert_isToday_Test extends LocalDateAssertBaseTest {

  @Test
  public void test_isToday_assertion() {
    // WHEN
    assertThat(REFERENCE).isToday();
    // THEN
    verify_that_isToday_assertion_fails_and_throws_AssertionError(BEFORE);
    verify_that_isToday_assertion_fails_and_throws_AssertionError(AFTER);
  }

  @Test
  public void test_isToday_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(parse("2000-01-01")).isToday())
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       " <2000-01-01>%n" +
                                                                       "to be today but was not."));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      LocalDate actual = null;
      assertThat(actual).isToday();
    }).withMessage(actualIsNull());
  }

  private static void verify_that_isToday_assertion_fails_and_throws_AssertionError(LocalDate dateToCheck) {
    assertThatThrownBy(() -> assertThat(dateToCheck).isToday()).isInstanceOf(AssertionError.class);
  }

}
