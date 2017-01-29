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

import static org.assertj.core.util.DateUtil.toCalendar;

import static org.assertj.core.api.Assertions.*;

import java.text.*;
import java.util.*;

import org.junit.Test;

/**
 * Tests for <code>{@link DateUtil#toCalendar(java.util.Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateUtil_toCalendar_Test {

  @Test
  public void should_convert_date_to_calendar() throws ParseException {
    String dateAsString = "26/08/1994";
    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateAsString);
    Calendar calendar = new GregorianCalendar();
    // clear all fields to have a Date without time (no hours, minutes...).
    calendar.clear();
    calendar.set(1994, 07, 26); // month is 0 value based.
    assertThat(toCalendar(date)).isEqualTo(calendar);
  }

  @Test
  public void should_return_null_if_date_to_convert_is_null() {
    assertThat(toCalendar(null)).isNull();
  }

}
