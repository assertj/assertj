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
package org.assertj.core.api.instant;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Instant;

import org.junit.Test;

public class InstantAssert_isAfter_Test extends InstantAssertBaseTest {

  @Test
  public void test_isAfter_assertion() {
    // WHEN
    assertThat(AFTER).isAfter(REFERENCE);
    assertThat(AFTER).isAfter(REFERENCE.toString());
    // THEN
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(REFERENCE, REFERENCE);
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(REFERENCE, REFERENCE);
  }

  @Test
  public void test_isAfter_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    thrown.expectAssertionError("%n" +
      "Expecting:%n" +
      "  <2007-12-03T10:15:30Z>%n" +
      "to be strictly after:%n" +
      "  <2007-12-03T10:15:35Z>");
    assertThat(instantReference).isAfter(instantAfter);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Instant actual = null;
      assertThat(actual).isAfter(Instant.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_date_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Instant.now()).isAfter((Instant) null))
                                        .withMessage("The Instant to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Instant.now()).isAfter((String) null))
                                        .withMessage("The String representing the Instant to compare actual with should not be null");
  }

  private static void verify_that_isAfter_assertion_fails_and_throws_AssertionError(Instant dateToCheck,
                                                                                    Instant reference) {
    assertThatThrownBy(() -> assertThat(dateToCheck).isAfter(reference)).isInstanceOf(AssertionError.class);
    assertThatThrownBy(() -> assertThat(dateToCheck).isAfter(reference.toString())).isInstanceOf(AssertionError.class);
  }

}
