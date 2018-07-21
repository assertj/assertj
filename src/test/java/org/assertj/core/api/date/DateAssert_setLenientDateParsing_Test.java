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
import static org.assertj.core.api.Assertions.setLenientDateParsing;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import java.util.Date;

import org.assertj.core.api.DateAssertBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the lenient mode of date parsing used in date assertions with date represented as {@link String}.
 *
 * @author Michal Kordas
 */
public class DateAssert_setLenientDateParsing_Test extends DateAssertBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    setLenientDateParsing(true);
  }

  @Test
  public void should_parse_date_leniently() {
    final Date date = parse("2001-02-03");
    assertThat(date).isEqualTo("2001-01-34");
    assertThat(date).isEqualTo("2001-02-02T24:00:00");
    assertThat(date).isEqualTo("2001-02-04T-24:00:00.000");
  }

  @Test
  public void should_parse_date_time_leniently() {
    final Date dateTime = parseDatetime("2001-02-03T04:05:06");
    assertThat(dateTime).isEqualTo("2001-02-03T04:05:05.1000");
    assertThat(dateTime).isEqualTo("2001-02-03T04:04:66");
  }

  @Test
  public void should_parse_date_time_with_milliseconds_leniently() {
    final Date dateTimeWithMs = parseDatetimeWithMs("2001-02-03T04:05:06.700");
    assertThat(dateTimeWithMs).isEqualTo("2001-02-03T04:05:07.-300");
  }

  @Test
  public void should_parse_date_time_leniently_using_custom_date_string_representation() {
    final Date date = parse("2001-02-03");
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2001/01/34");
  }

  @Test
  public void should_fail_if_given_date_string_representation_cant_be_parsed() {
    final String dateAsString = "2001/02/03";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new Date()).isEqualTo(dateAsString));
  }

  @Test
  public void should_fail_if_date_can_be_parsed_leniently_but_lenient_mode_is_disabled() {
    final Date date = parse("2001-02-03");
    setLenientDateParsing(false);
    try {
      assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(date).isEqualTo("2001-01-34"))
                                                     .withMessageContaining("Failed to parse");
    } finally {
      setLenientDateParsing(true);
    }
  }

  @Override
  @AfterEach
  public void tearDown() {
    super.tearDown();
    setLenientDateParsing(false);
  }
}
