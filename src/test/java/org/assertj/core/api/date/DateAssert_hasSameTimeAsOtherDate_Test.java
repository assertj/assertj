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
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.ErrorMessages.dateToCompareActualWithIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.sql.Timestamp;
import java.util.Date;

import org.assertj.core.api.DateAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.DateAssert#hasSameTimeAs(java.util.Date)} </code>.
 *
 * @author Alexander Bischof
 */
public class DateAssert_hasSameTimeAsOtherDate_Test extends DateAssertBaseTest {

  @Test
  public void should_verify_that_actual_has_time_equals_to_expected() {
    Date date = new Date();
    Timestamp timestamp = new java.sql.Timestamp(date.getTime());
    assertThat(date).hasSameTimeAs(timestamp);
    assertThat(timestamp).hasSameTimeAs(date);
  }

  @Test
  public void should_fail_when_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Date) null).hasSameTimeAs(new Date()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_throw_exception_when_date_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(new Date()).hasSameTimeAs((Date) null))
                                    .withMessage(dateToCompareActualWithIsNull());
  }
}
