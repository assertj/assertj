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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalDateAssert_isBeforeOrEqualTo_Test extends LocalDateAssertBaseTest {

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
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(LocalDate.of(2000, 1, 5)).isBeforeOrEqualTo(LocalDate.of(1998, 1, 1)))
                                                   .withMessage(format("%nExpecting:%n  <2000-01-05>%nto be before or equals to:%n  <1998-01-01>"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      LocalDate actual = null;
      assertThat(actual).isBeforeOrEqualTo(LocalDate.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_date_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalDate.now()).isBeforeOrEqualTo((LocalDate) null))
                                        .withMessage("The LocalDate to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalDate.now()).isBeforeOrEqualTo((String) null))
                                        .withMessage("The String representing the LocalDate to compare actual with should not be null");
  }

  private static void verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(LocalDate dateToCheck,
                                                                                            LocalDate reference) {
    assertThatThrownBy(() -> assertThat(dateToCheck).isBeforeOrEqualTo(reference)).isInstanceOf(AssertionError.class);
    assertThatThrownBy(() -> assertThat(dateToCheck).isBeforeOrEqualTo(reference.toString())).isInstanceOf(AssertionError.class);
  }

}
