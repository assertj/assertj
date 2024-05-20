/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.date;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.registerCustomDateFormat;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the default date format used when using date assertions with date represented as string.
 *
 * @author Joel Costigliola
 */
class DateAssert_with_string_based_date_representation_Test extends DateAssertBaseTest {

  private TimeZone defaultTimeZone;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    defaultTimeZone = TimeZone.getDefault();
  }

  @Override
  @AfterEach
  public void tearDown() {
    useDefaultDateFormatsOnly();
    TimeZone.setDefault(defaultTimeZone);
  }

  @Test
  void date_assertion_using_default_date_string_representation() {
    // datetime with ms is supported
    final Date date1timeWithMS = parseDatetimeWithMs("2003-04-26T03:01:02.999");
    assertThat(date1timeWithMS).isEqualTo("2003-04-26T03:01:02.999");
    // datetime without ms is supported
    final Date datetime = parseDatetime("2003-04-26T03:01:02");
    assertThat(datetime).isEqualTo("2003-04-26T03:01:02.000");
    assertThat(datetime).isEqualTo("2003-04-26T03:01:02");
    // date is supported
    Date date = DateUtil.parse("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");
    assertThat(date).isEqualTo("2003-04-26T00:00:00.000");

    // TODO add test with nanos
  }

  @Test
  void date_assertion_should_support_local_date_string_representation() {
    // GIVEN
    LocalDate localDate = LocalDate.of(2003, 4, 26);
    ZoneId systemDefault = ZoneId.systemDefault();
    Date date = Date.from(localDate.atStartOfDay(systemDefault).toInstant());
    // WHEN/THEN
    then(date).isEqualTo("2003-04-26");
  }

  @Test
  void date_assertion_should_support_timestamp_string_representation() {
    // GIVEN
    Date date = new Date(Timestamp.valueOf("2003-04-26 13:01:02.999").getTime());
    // WHEN/THEN
    then(date).isEqualTo("2003-04-26 13:01:02.999");
  }

  @Test
  void date_assertion_should_support_date_with_iso_offset_datetime_string_representation() {
    // GIVEN
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2003, 4, 26, 13, 1, 2, 0, ZoneOffset.of("+01:00"));
    Date date = Date.from(offsetDateTime.toInstant());
    // WHEN/THEN
    then(date).isEqualTo("2003-04-26T13:01:02+01:00");
  }

  @Test
  void date_assertion_should_support_date_with_iso_offset_datetime_string_representation_with_millis() {
    // GIVEN
    int nanos = (int) TimeUnit.MILLISECONDS.toNanos(999);
    OffsetDateTime isoOffsetDateTime = OffsetDateTime.of(2003, 4, 26, 13, 1, 2, nanos, ZoneOffset.of("+02:00"));
    Date date = Date.from(isoOffsetDateTime.toInstant());
    // WHEN/THEN
    then(date).isEqualTo("2003-04-26T13:01:02.999+02:00");
  }

  @Test
  void date_assertion_should_support_date_with_iso_local_datetime_string_representation_with_millis() {
    // GIVEN
    int nanos = (int) TimeUnit.MILLISECONDS.toNanos(999);
    LocalDateTime localDateTimeWithMillis = LocalDateTime.of(2003, 4, 26, 13, 1, 2, nanos);
    Date date = Date.from(localDateTimeWithMillis.atZone(ZoneId.systemDefault()).toInstant());
    // WHEN/THEN
    then(date).isEqualTo("2003-04-26T13:01:02.999");
  }

  @Test
  void date_assertion_should_support_date_with_iso_local_datetime_string_representation() {
    // GIVEN
    LocalDateTime localDateTimeWithMillis = LocalDateTime.of(2003, 4, 26, 13, 1, 2, 0);
    Date date = Date.from(localDateTimeWithMillis.atZone(ZoneId.systemDefault()).toInstant());
    // WHEN/THEN
    then(date).isEqualTo("2003-04-26T13:01:02");
  }

  @Test
  void should_fail_if_given_date_string_representation_cant_be_parsed_with_default_date_formats() {
    final String dateAsString = "2003/04/26";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new Date()).isEqualTo(dateAsString))
                                                   .withMessage(format("Failed to parse 2003/04/26 with any of these date formats:%n"
                                                                       +
                                                                       "   [yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                                                       "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                                                       "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                                                       "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                                                       "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                                                       "    yyyy-MM-dd]"));
  }

  @Test
  void date_assertion_using_custom_date_string_representation() {
    final Date date = DateUtil.parse("2003-04-26");
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26");
    assertThat(date).isEqualTo("2003/04/26");
  }

  @Test
  void should_fail_if_given_date_string_representation_cant_be_parsed_with_any_custom_date_formats() {
    final Date date = DateUtil.parse("2003-04-26");
    registerCustomDateFormat("yyyy/MM/dd'T'HH:mm:ss");
    // registering again has no effect
    registerCustomDateFormat("yyyy/MM/dd'T'HH:mm:ss");
    AssertionError error = expectAssertionError(() -> assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003 04 26"));
    assertThat(error).hasMessage(format("Failed to parse 2003 04 26 with any of these date formats:%n"
                                        +
                                        "   [yyyy/MM/dd'T'HH:mm:ss,%n" +
                                        "    yyyy/MM/dd,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                        "    yyyy-MM-dd]"));
  }

  @Test
  void date_assertion_using_custom_date_string_representation_then_switching_back_to_defaults_date_formats() {
    final Date date = DateUtil.parse("2003-04-26");
    // chained assertions
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26")
                    .withDefaultDateFormatsOnly().isEqualTo("2003-04-26");
    // new assertions
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26");
    assertThat(date).withDefaultDateFormatsOnly().isEqualTo("2003-04-26");
  }

  @Test
  void use_custom_date_formats_set_from_Assertions_entry_point() {
    // GIVEN
    final Date date = DateUtil.parse("2003-04-26");

    registerCustomDateFormat("yyyy/MM/dd'T'HH:mm:ss");

    // WHEN
    // fail : the registered format does not match the given date
    AssertionError error = expectAssertionError(() -> assertThat(date).isEqualTo("2003/04/26"));

    // THEN
    assertThat(error).hasMessage(format("Failed to parse 2003/04/26 with any of these date formats:%n" +
                                        "   [yyyy/MM/dd'T'HH:mm:ss,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                        "    yyyy-MM-dd]"));

    // register the expected custom formats, they are used in the order they have been registered.
    registerCustomDateFormat("yyyy/MM/dd");
    assertThat(date).isEqualTo("2003/04/26");
    // another to register a DateFormat
    registerCustomDateFormat(new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS"));
    // the assertion uses the last custom date format registered.
    assertThat(date).isEqualTo("2003/04/26T00:00:00.000");

    useDefaultDateFormatsOnly();
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");
    assertThat(date).isEqualTo("2003-04-26T00:00:00.000");
  }

  @Test
  void use_custom_date_formats_first_then_defaults_to_parse_a_date() {
    // GIVEN
    // using default formats should work
    final Date date = DateUtil.parse("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26");

    // WHEN
    // date with a custom format : failure since the default formats don't match.
    AssertionError error = expectAssertionError(() -> assertThat(date).isEqualTo("2003/04/26"));

    // THEN
    assertThat(error).hasMessage(format("Failed to parse 2003/04/26 with any of these date formats:%n" +
                                        "   [yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                        "    yyyy-MM-dd]"));

    // registering a custom date format to make the assertion pass
    registerCustomDateFormat("yyyy/MM/dd");
    assertThat(date).isEqualTo("2003/04/26");
    // the default formats are still available and should work
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");

    // WHEN
    // but if not format at all matches, it fails.
    error = expectAssertionError(() -> assertThat(date).isEqualTo("2003 04 26"));

    // THEN
    assertThat(error).hasMessage(format("Failed to parse 2003 04 26 with any of these date formats:%n" +
                                        "   [yyyy/MM/dd,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                        "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                        "    yyyy-MM-dd]"));

    // register a new custom format should work
    registerCustomDateFormat("yyyy MM dd");
    assertThat(date).isEqualTo("2003 04 26");
  }

  @Test
  void default_date_formats_should_support_default_timezone_change() {
    // GIVEN
    TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    // need to call a date assertion to initialize the default date formats before changing the timezone.
    assertThat(Date.from(Instant.parse("2024-03-01T00:00:00.000+01:00"))).as("In CET time zone").isEqualTo("2024-03-01");
    // WHEN
    TimeZone.setDefault(TimeZone.getTimeZone("WET"));
    // THEN
    then(Date.from(Instant.parse("2024-03-01T00:00:00.000+00:00"))).as("In WET time zone").isEqualTo("2024-03-01");
  }
}
