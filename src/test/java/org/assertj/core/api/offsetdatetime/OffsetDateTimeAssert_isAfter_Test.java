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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.offsetdatetime;

import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class OffsetDateTimeAssert_isAfter_Test extends OffsetDateTimeAssertBaseTest {

  @Test
  public void test_isAfter_assertion() {
    // WHEN
    assertThat(AFTER).isAfter(REFERENCE);
    assertThat(AFTER).isAfter(REFERENCE.toString());
    // THEN
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(REFERENCE, REFERENCE);
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(BEFORE, REFERENCE);
  }

  @Test
  public void test_isAfter_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(parse("2000-01-01T03:00:05.123Z")).isAfter(parse("2000-01-01T03:00:05.123456789Z")))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       "  <2000-01-01T03:00:05.123Z>%n" +
                                                                       "to be strictly after:%n" +
                                                                       "  <2000-01-01T03:00:05.123456789Z>"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      OffsetDateTime actual = null;
      assertThat(actual).isAfter(OffsetDateTime.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(OffsetDateTime.now()).isAfter((OffsetDateTime) null))
                                        .withMessage("The OffsetDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(OffsetDateTime.now()).isAfter((String) null))
                                        .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

  private static void verify_that_isAfter_assertion_fails_and_throws_AssertionError(OffsetDateTime dateToCheck,
                                                                                    OffsetDateTime reference) {
    assertThatThrownBy(() -> assertThat(dateToCheck).isAfter(reference)).isInstanceOf(AssertionError.class);
    assertThatThrownBy(() -> assertThat(dateToCheck).isAfter(reference.toString())).isInstanceOf(AssertionError.class);
  }

}
