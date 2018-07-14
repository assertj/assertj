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

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@RunWith(Theories.class)
public class LocalDateAssert_isAfterOrEqualTo_Test extends LocalDateAssertBaseTest {

  @Theory
  public void test_isAfterOrEqual_assertion(LocalDate referenceDate, LocalDate dateBefore,
	                                        LocalDate dateAfter) {
	// GIVEN
	testAssumptions(referenceDate, dateBefore, dateAfter);
	// WHEN
	assertThat(dateAfter).isAfterOrEqualTo(referenceDate);
	assertThat(referenceDate).isAfterOrEqualTo(referenceDate);
	// THEN
	verify_that_isAfterOrEqual_assertion_fails_and_throws_AssertionError(dateBefore, referenceDate);
  }

  @Test
  public void test_isAfterOrEqual_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> 
    assertThat(LocalDate.of(2000, 1, 5)).isAfterOrEqualTo(LocalDate.of(2012, 1, 1))).withMessage(format("%n" +
                                                                                                        "Expecting:%n" +
                                                                                                        "  <2000-01-05>%n"
                                                                                                        +
                                                                                                        "to be after or equals to:%n"
                                                                                                        +
                                                                                                        "  <2012-01-01>"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      LocalDate actual = null;
      assertThat(actual).isAfterOrEqualTo(LocalDate.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_date_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalDate.now()).isAfterOrEqualTo((LocalDate) null))
                                        .withMessage("The LocalDate to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalDate.now()).isAfterOrEqualTo((String) null))
                                        .withMessage("The String representing the LocalDate to compare actual with should not be null");
  }

  private static void verify_that_isAfterOrEqual_assertion_fails_and_throws_AssertionError(LocalDate dateToCheck,
                                                                                           LocalDate reference) {
    assertThatThrownBy(() -> assertThat(dateToCheck).isAfterOrEqualTo(reference)).isInstanceOf(AssertionError.class);
    assertThatThrownBy(() -> assertThat(dateToCheck).isAfterOrEqualTo(reference.toString())).isInstanceOf(AssertionError.class);
  }

}
