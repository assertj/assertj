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
package org.assertj.core.util;

import static org.assertj.core.util.DateUtil.hourOfDayOf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.text.*;
import java.util.Date;

import org.junit.*;
import org.assertj.core.test.ExpectedException;

/**
 * Tests for <code>{@link DateUtil#hourOfDayOf(Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateUtil_hourOfDayOf_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_return_hour_of_day_of_date() throws ParseException {
    String dateAsString = "26/08/1994T22:35:00";
    Date date = new SimpleDateFormat("dd/MM/yyyy'T'hh:mm:ss").parse(dateAsString);
    assertThat(hourOfDayOf(date)).isEqualTo(22);
  }

  @Test
  public void should_throws_NullPointerException_if_date_parameter_is_null() {
    thrown.expectNullPointerException();
    hourOfDayOf(null);
  }

}
