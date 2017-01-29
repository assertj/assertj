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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.DateUtil.formatTimeDifference;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import java.util.Date;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link DateUtil#timeDifference(java.util.Date, java.util.Date)}</code>.
 *
 * @author Joel Costigliola
 */
public class DateUtil_formatTimeDifference_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_return_dates_time_difference() {
    final Date date1 = parseDatetimeWithMs("2003-01-01T00:00:00.888");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T00:00:00.999"))).isEqualTo("111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T00:00:01.999"))).isEqualTo("1s and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T00:00:01.888"))).isEqualTo("1s");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T00:02:01.999"))).isEqualTo("2m 1s and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T00:02:00.999"))).isEqualTo("2m and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T03:02:01.999"))).isEqualTo("3h 2m 1s and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T03:02:00.888"))).isEqualTo("3h and 2m");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-05T03:02:01.999"))).isEqualTo("4d 3h 2m 1s and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-02-01T03:02:01.999"))).isEqualTo("31d 3h 2m 1s and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-01-01T03:02:01.888"))).isEqualTo("3h 2m and 1s");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-02-01T00:02:00.999"))).isEqualTo("31d 2m and 111ms");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-02-01T00:02:00.888"))).isEqualTo("31d and 2m");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-02-01T01:00:00.888"))).isEqualTo("31d and 1h");
    assertThat(formatTimeDifference(date1, parseDatetimeWithMs("2003-02-01T00:00:00.999"))).isEqualTo("31d and 111ms");

    final Date date3 = parseDatetimeWithMs("2008-07-06T05:04:03.002");
    assertThat(formatTimeDifference(date3, parseDatetimeWithMs("2008-07-06T05:04:03.002"))).isEmpty();
    assertThat(formatTimeDifference(date3, parseDatetimeWithMs("2008-07-06T05:04:03.001"))).isEqualTo("1ms");
    assertThat(formatTimeDifference(date3, parseDatetimeWithMs("2008-07-01T04:03:02.001"))).isEqualTo("5d 1h 1m 1s and 1ms");
  }

  @Test
  public void should_throws_IllegalArgumentException_if_first_date_parameter_is_null() {
    thrown.expectIllegalArgumentException("Expecting date parameter not to be null");
    formatTimeDifference(new Date(), null);
  }

  @Test
  public void should_throws_IllegalArgumentException_if_second_date_parameter_is_null() {
    thrown.expectIllegalArgumentException("Expecting date parameter not to be null");
    formatTimeDifference(null, new Date());
  }

}
