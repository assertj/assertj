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
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.junit.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalDateTimeAssert_isBeforeOrEqualTo_Test extends LocalDateTimeAssertBaseTest {

  @Test
  public void test_isBeforeOrEqual_assertion() {
    // WHEN
    assertThat(BEFORE).isBeforeOrEqualTo(REFERENCE);
    assertThat(REFERENCE).isBeforeOrEqualTo(REFERENCE);
    // THEN
    verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(AFTER, REFERENCE);
  }

  @Test
  public void test_isBeforeOrEqual_assertion_error_message() {
    thrown.expectAssertionError("%nExpecting:%n  <2000-01-05T03:00:05>%nto be before or equals to:%n  <1998-01-01T03:03:03>");
    assertThat(LocalDateTime.of(2000, 1, 5, 3, 0, 5)).isBeforeOrEqualTo(LocalDateTime.of(1998, 1, 1, 3, 3, 3));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    LocalDateTime actual = null;
    assertThat(actual).isBeforeOrEqualTo(LocalDateTime.now());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The LocalDateTime to compare actual with should not be null");
    assertThat(LocalDateTime.now()).isBeforeOrEqualTo((LocalDateTime) null);
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
        "The String representing the LocalDateTime to compare actual with should not be null");
    assertThat(LocalDateTime.now()).isBeforeOrEqualTo((String) null);
  }

  private static void verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(LocalDateTime dateToCheck,
                                                                                            LocalDateTime reference) {
    assertThatThrownBy(() -> assertThat(dateToCheck).isBeforeOrEqualTo(reference)).isInstanceOf(AssertionError.class);
    assertThatThrownBy(() -> assertThat(dateToCheck).isBeforeOrEqualTo(reference.toString())).isInstanceOf(AssertionError.class);
  }

}
