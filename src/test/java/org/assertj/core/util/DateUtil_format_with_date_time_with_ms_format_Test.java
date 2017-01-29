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

import static org.assertj.core.util.DateUtil.formatAsDatetimeWithMs;

import static org.assertj.core.api.Assertions.*;

import java.text.*;
import java.util.Date;

import org.junit.Test;

/**
 * Tests for <code>{@link DateUtil#formatAsDatetimeWithMs(Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateUtil_format_with_date_time_with_ms_format_Test {

  @Test
  public void should_format_date_with_date_time_with_ms_format() throws ParseException {
    String dateAsString = "26/08/1994";
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    assertThat(formatAsDatetimeWithMs(formatter.parse(dateAsString))).isEqualTo("1994-08-26T00:00:00.000");
  }

  @Test
  public void should_return_null_if_date_is_null() {
    assertThat(formatAsDatetimeWithMs((Date) null)).isNull();
  }

}
