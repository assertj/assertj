/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.ErrorMessages.dateToCompareActualWithIsNull;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.sql.Timestamp;
import java.util.Date;

import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.DateAssert#hasSameTimeAs(String)} </code>.
 *
 * @author Michal Kordas
 */
public class DateAssert_hasSameTimeAsDateInString_Test extends DateAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_verify_that_date_has_time_same_as_string_from_timestamp() {
    Date date = parseDatetime("2003-04-26T12:59:59.999");
    Timestamp timestamp = new Timestamp(date.getTime());
    assertThat(date).withDateFormat("yyyy-MM-dd HH:mm:ss.SSS").hasSameTimeAs(timestamp.toString());
  }

  @Test
  public void should_verify_that_date_has_same_time_as_string_from_date() {
    Date date = parseDatetime("2003-04-26T12:00:00");
    assertThat(date).hasSameTimeAs("2003-04-26T12:00:00");
  }

  @Test
  public void should_fail_when_checking_if_date_has_same_time_as_other_date() {
    Date date = parseDatetime("2003-04-26T12:00:00");
    thrown.expectAssertionError();
    assertThat(date).hasSameTimeAs("2003-04-27T12:00:00");
  }

  @Test
  public void should_fail_when_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((Date) null).hasSameTimeAs("2003-04-26T12:00:00");
  }

  @Test
  public void should_throw_exception_when_date_is_null() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());

    assertThat(new Date()).hasSameTimeAs((String) null);
  }
}
