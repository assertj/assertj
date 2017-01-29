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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.registerCustomDateFormat;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.DateUtil;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the default date format used when using date assertions with date represented as string.
 *
 * @author Joel Costigliola
 */
public class DateAssert_with_string_based_date_representation_Test extends DateAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();

  @Override
  @After
  public void tearDown() {
    useDefaultDateFormatsOnly();
  }

  @Test
  public void date_assertion_using_default_date_string_representation() {
    // datetime with ms is supported
    final Date date1timeWithMS = parseDatetimeWithMs("2003-04-26T03:01:02.999");
    assertThat(date1timeWithMS).isEqualTo("2003-04-26T03:01:02.999");
    // datetime without ms is supported
    final Date datetime = parseDatetime("2003-04-26T03:01:02");
    assertThat(datetime).isEqualTo("2003-04-26T03:01:02.000");
    assertThat(datetime).isEqualTo("2003-04-26T03:01:02");
    // date is supported
    final Date date = DateUtil.parse("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");
    assertThat(date).isEqualTo("2003-04-26T00:00:00.000");
  }

  @Test
  public void date_assertion_should_support_timestamp_string_representation() throws ParseException {
    Date date = DateUtil.newTimestampDateFormat().parse("2015-05-08 11:30:00.560");
    String timestampAsString = DateUtil.newTimestampDateFormat().format(new Timestamp(date.getTime()));

    assertThat(date).isEqualTo(timestampAsString);
  }

  @Test
  public void should_fail_if_given_date_string_representation_cant_be_parsed_with_default_date_formats() {
    final String dateAsString = "2003/04/26";
    thrown.expectAssertionError("Failed to parse 2003/04/26 with any of these date formats:%n" +
                                "   [yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                "    yyyy-MM-dd]");
    assertThat(new Date()).isEqualTo(dateAsString);
  }

  @Test
  public void date_assertion_using_custom_date_string_representation() {
    final Date date = DateUtil.parse("2003-04-26");
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26");
    assertThat(date).isEqualTo("2003/04/26");
  }

  @Test
  public void should_fail_if_given_date_string_representation_cant_be_parsed_with_any_custom_date_formats() {
    thrown.expectAssertionError("Failed to parse 2003 04 26 with any of these date formats:%n" +
                                "   [yyyy/MM/dd'T'HH:mm:ss,%n" +
                                "    yyyy/MM/dd,%n" +
                                "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                "    yyyy-MM-dd]");
    final Date date = DateUtil.parse("2003-04-26");
    registerCustomDateFormat("yyyy/MM/dd'T'HH:mm:ss");
    // registering again has no effect
    registerCustomDateFormat("yyyy/MM/dd'T'HH:mm:ss");
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003 04 26");
  }

  @Test
  public void date_assertion_using_custom_date_string_representation_then_switching_back_to_defaults_date_formats() {
    final Date date = DateUtil.parse("2003-04-26");
    // chained assertions
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26")
                    .withDefaultDateFormatsOnly().isEqualTo("2003-04-26");
    // new assertions
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26");
    assertThat(date).withDefaultDateFormatsOnly().isEqualTo("2003-04-26");
  }

  @Test
  public void use_custom_date_formats_set_from_Assertions_entry_point() {
    final Date date = DateUtil.parse("2003-04-26");

    registerCustomDateFormat("yyyy/MM/dd'T'HH:mm:ss");

    try {
      // fail : the registered format does not match the given date
      assertThat(date).isEqualTo("2003/04/26");
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("Failed to parse 2003/04/26 with any of these date formats:%n" +
                                      "   [yyyy/MM/dd'T'HH:mm:ss,%n" +
                                      "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                      "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                      "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                      "    yyyy-MM-dd]"));
    }

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
  public void use_custom_date_formats_first_then_defaults_to_parse_a_date() {
    // using default formats should work
    final Date date = DateUtil.parse("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26");

    try {
      // date with a custom format : failure since the default formats don't match.
      assertThat(date).isEqualTo("2003/04/26");
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("Failed to parse 2003/04/26 with any of these date formats:%n" +
                                      "   [yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                      "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                      "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                      "    yyyy-MM-dd]"));
    }

    // registering a custom date format to make the assertion pass
    registerCustomDateFormat("yyyy/MM/dd");
    assertThat(date).isEqualTo("2003/04/26");
    // the default formats are still available and should work
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");

    try {
      // but if not format at all matches, it fails.
      assertThat(date).isEqualTo("2003 04 26");
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(format("Failed to parse 2003 04 26 with any of these date formats:%n" +
                                      "   [yyyy/MM/dd,%n" +
                                      "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                      "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                      "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                      "    yyyy-MM-dd]"));

    }

    // register a new custom format should work
    registerCustomDateFormat("yyyy MM dd");
    assertThat(date).isEqualTo("2003 04 26");
  }

}
