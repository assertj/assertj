/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Dates.parseDatetime;
import static org.assertj.core.util.Dates.parseDatetimeWithMs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.Dates;

/**
 * Tests the default date format used when using date assertions with date represented as string.
 *
 * @author Joel Costigliola
 */
public class DateAssert_with_string_based_date_representation_Test extends DateAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();

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
    final Date date = Dates.parse("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");
    assertThat(date).isEqualTo("2003-04-26T00:00:00.000");
  }

  @Test
  public void should_fail_if_given_date_string_representation_cant_be_parsed_with_default_date_formats() {
    final String dateAsString = "2003/04/26";
    thrown.expectAssertionError("Failed to parse " + dateAsString + " with any of these date formats: " +
                                  "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
    assertThat(new Date()).isEqualTo(dateAsString);
  }

  @Test
  public void date_assertion_using_custom_date_string_representation() {
    final Date date = Dates.parse("2003-04-26");
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26");
  }

  @Test
  public void should_fail_if_given_date_string_representation_cant_be_parsed_with_custom_date_formats() {
    thrown.expectAssertionError("Failed to parse 2003-04-26 with date format: yyyy/MM/dd");
    final Date date = Dates.parse("2003-04-26");
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003-04-26");
  }

  @Test
  public void date_assertion_using_custom_date_string_representation_then_switching_back_to_defaults_date_formats() {
    final Date date = Dates.parse("2003-04-26");
    // chained assertions
    assertThat(date)
      .withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26")
      .withDefaultDateFormats().isEqualTo("2003-04-26");
    // new assertions
    assertThat(date).withDateFormat("yyyy/MM/dd").isEqualTo("2003/04/26");
    assertThat(date).withDefaultDateFormats().isEqualTo("2003-04-26");
  }

  @Test
  public void use_different_date_format_fromAssertions_entry_point() {
    final Date date = Dates.parse("2003-04-26");

    Assertions.useDateFormat("yyyy/MM/dd");
    assertThat(date).isEqualTo("2003/04/26");
    Assertions.useDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
    assertThat(date).isEqualTo("2003/04/26");

    Assertions.useDefaultDateFormats();
    assertThat(date).isEqualTo("2003-04-26");
    assertThat(date).isEqualTo("2003-04-26T00:00:00");
    assertThat(date).isEqualTo("2003-04-26T00:00:00.000");

    Assertions.useIsoDateFormat();
    assertThat(date).isEqualTo("2003-04-26");

    Assertions.useIsoDateTimeFormat();
    assertThat(date).isEqualTo("2003-04-26T00:00:00");

    Assertions.useIsoDateTimeWithMsFormat();
    assertThat(date).isEqualTo("2003-04-26T00:00:00.000");
    // switch back to defaults to avoid side effects on other tests (I know, they should be independent).
    Assertions.useDefaultDateFormats();
  }

}
