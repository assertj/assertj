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

import static org.assertj.core.util.DateUtil.truncateTime;

import static org.assertj.core.api.Assertions.*;

import java.text.*;
import java.util.Date;

import org.junit.Test;

/**
 * Tests for <code>{@link DateUtil#millisecondOf(Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateUtil_truncateTime_Test {

  @Test
  public void should_return_millisecond_of_date() throws ParseException {
    Date date = new SimpleDateFormat("dd/MM/yyyy'T'hh:mm:ss:SS").parse("26/08/1994T22:35:17:29");
    Date expectedDate = new SimpleDateFormat("dd/MM/yyyy").parse("26/08/1994");
    assertThat(truncateTime(date)).isEqualTo(expectedDate);
  }

  @Test
  public void should_return_null_if_date_is_null() {
    assertThat(truncateTime(null)).isNull();
  }

}
