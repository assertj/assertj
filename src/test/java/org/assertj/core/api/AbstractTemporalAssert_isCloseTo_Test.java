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
package org.assertj.core.api;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.WEEKS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.assertj.core.data.TemporalUnitOffset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AbstractTemporalAssert_isCloseTo_Test extends BaseTest {

  private static AbstractTemporalAssert<?, ?>[] nullAsserts = {
      assertThat((Instant) null),
      assertThat((LocalDateTime) null),
      assertThat((LocalDate) null),
      assertThat((LocalTime) null),
      assertThat((OffsetDateTime) null),
      assertThat((ZonedDateTime) null),
      assertThat((OffsetTime) null)
  };

  private static final Instant _2017_Mar_12_07_10_Instant = Instant.parse("2017-03-12T07:10:00.00Z");
  private static final LocalDate _2017_Mar_10 = LocalDate.of(2017, Month.MARCH, 10);
  private static final LocalDate _2017_Mar_12 = LocalDate.of(2017, Month.MARCH, 12);
  private static final LocalDate _2017_Mar_27 = LocalDate.of(2017, Month.MARCH, 27);
  private static final LocalTime _07_10 = LocalTime.of(7, 10);
  private static final LocalDateTime _2017_Mar_12_07_10 = LocalDateTime.of(_2017_Mar_12, _07_10);

  private static AbstractTemporalAssert<?, ?>[] temporalAsserts = {
      assertThat(_2017_Mar_12_07_10_Instant),
      assertThat(_2017_Mar_12_07_10),
      assertThat(_2017_Mar_12),
      assertThat(_07_10),
      assertThat(OffsetDateTime.of(_2017_Mar_12_07_10, UTC)),
      assertThat(ZonedDateTime.of(_2017_Mar_12_07_10, ZoneId.of("America/New_York"))),
      assertThat(OffsetTime.of(_07_10, UTC))
  };

  private static final Instant _2017_Mar_12_07_12_Instant = Instant.parse("2017-03-12T07:12:00.00Z");
  private static final LocalTime _07_12 = LocalTime.of(7, 12);
  private static final LocalDateTime _2017_Mar_12_07_12 = LocalDateTime.of(_2017_Mar_12, _07_12);
  private static final LocalDateTime _2017_Mar_10_07_12 = LocalDateTime.of(_2017_Mar_10, _07_12);

  private static Temporal[] closeTemporals = {
      _2017_Mar_12_07_12_Instant,
      _2017_Mar_10_07_12,
      _2017_Mar_10,
      _07_12,
      OffsetDateTime.of(_2017_Mar_12_07_12, UTC),
      ZonedDateTime.of(_2017_Mar_10_07_12, ZoneId.of("America/New_York")),
      OffsetTime.of(_07_12, UTC)
  };

  private static final Instant _2017_Mar_08_07_10_Instant = Instant.parse("2017-03-08T07:10:00.00Z");
  private static final LocalTime _07_23 = LocalTime.of(7, 23);
  private static final LocalDate _2017_Mar_08 = LocalDate.of(2017, Month.MARCH, 8);
  private static final LocalDateTime _2017_Mar_12_07_23 = LocalDateTime.of(_2017_Mar_12, _07_23);
  private static final LocalDateTime _2017_Mar_08_07_10 = LocalDateTime.of(_2017_Mar_08, _07_10);

  private static Temporal[] farTemporals = new Temporal[] {
      _2017_Mar_08_07_10_Instant,
      _2017_Mar_08_07_10,
      _2017_Mar_27,
      _07_23,
      OffsetDateTime.of(_2017_Mar_12_07_23, UTC),
      ZonedDateTime.of(_2017_Mar_08_07_10, ZoneId.of("America/New_York")),
      OffsetTime.of(_07_23, UTC)
  };

  private static String[] differenceMessages = {
    format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nwithin 50 Hours but difference was 96 Hours",
      _2017_Mar_12_07_10_Instant, _2017_Mar_08_07_10_Instant),
      format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nwithin 50 Hours but difference was 96 Hours",
             _2017_Mar_12_07_10, _2017_Mar_08_07_10),
      format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nwithin 3 Days but difference was 15 Days",
             _2017_Mar_12, _2017_Mar_27),
      format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nwithin 5 Minutes but difference was 13 Minutes", _07_10,
             _07_23),
      format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nwithin 10 Minutes but difference was 13 Minutes",
             OffsetDateTime.of(_2017_Mar_12_07_10, UTC),
             OffsetDateTime.of(_2017_Mar_12_07_23, UTC)),
      format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nby less than 95 Hours but difference was 95 Hours",
             ZonedDateTime.of(_2017_Mar_12_07_10, ZoneId.of("America/New_York")),
             ZonedDateTime.of(_2017_Mar_08_07_10, ZoneId.of("America/New_York"))),
      format("%nExpecting:%n <%s>%nto be close to:%n <%s>%nwithin 2 Minutes but difference was 13 Minutes",
             OffsetTime.of(_07_10, UTC), OffsetTime.of(_07_23, UTC)),
  };

  private static TemporalUnitOffset[] offsets = {
      within(50, HOURS),
      within(50, HOURS),
      within(3, DAYS),
      within(5, MINUTES),
      within(10, MINUTES),
      byLessThan(95, HOURS),
      within(2, MINUTES)
  };

  private static TemporalUnitOffset[] inapplicableOffsets = { null, null, within(1, MINUTES),
      within(1, DAYS), null, null, within(1, WEEKS) };

  @Parameters
  public static Object[][] getParameters() {
    
    DateTimeFormatter[] formatters = {
        ISO_INSTANT,
        ISO_LOCAL_DATE_TIME,
        ISO_LOCAL_DATE,
        ISO_LOCAL_TIME,
        ISO_OFFSET_DATE_TIME,
        ISO_ZONED_DATE_TIME, ISO_TIME
    };

    int assertsLength = nullAsserts.length; // same as temporalAsserts.length
    Object[][] parameters = new Object[assertsLength][9];
    for (int i = 0; i < assertsLength; i++) {
      parameters[i][0] = nullAsserts[i];
      parameters[i][1] = temporalAsserts[i];
      parameters[i][2] = offsets[i];
      parameters[i][3] = closeTemporals[i];
      parameters[i][4] = formatters[i].format(closeTemporals[i]);
      parameters[i][5] = farTemporals[i];
      parameters[i][6] = formatters[i].format(farTemporals[i]);
      parameters[i][7] = differenceMessages[i];
      parameters[i][8] = inapplicableOffsets[i];
    }

    return parameters;
  }

  @Parameter(value = 0)
  public AbstractTemporalAssert<?, Temporal> nullAssert;

  @Parameter(value = 1)
  public AbstractTemporalAssert<?, Temporal> temporalAssert;

  @Parameter(value = 2)
  public TemporalUnitOffset offset;

  @Parameter(value = 3)
  public Temporal closeTemporal;

  @Parameter(value = 4)
  public String closeTemporalAsString;

  @Parameter(value = 5)
  public Temporal farTemporal;

  @Parameter(value = 6)
  public String farTemporalAsString;

  @Parameter(value = 7)
  public String differenceMessage;

  @Parameter(value = 8)
  public TemporalUnitOffset inapplicableOffset;

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    nullAssert.isCloseTo(closeTemporal, offset);
  }

  @Test
  public void should_fail_if_temporal_parameter_is_null() {
    expectException(NullPointerException.class, "The temporal object to compare actual with should not be null");
    temporalAssert.isCloseTo((Temporal) null, offset);
  }

  @Test
  public void should_fail_if_temporal_parameter_as_string_is_null() {
    expectException(NullPointerException.class,
                    "The String representing of the temporal object to compare actual with should not be null");
    temporalAssert.isCloseTo((String) null, offset);
  }

  @Test
  public void should_fail_if_offset_parameter_is_null() {
    expectException(NullPointerException.class, "The offset should not be null");
    temporalAssert.isCloseTo(closeTemporal, null);
  }

  @Test
  public void should_fail_when_offset_is_inapplicable() {
    if (inapplicableOffset != null) {
      expectException(UnsupportedTemporalTypeException.class, "Unsupported unit: " + inapplicableOffset.getUnit());
      temporalAssert.isCloseTo(closeTemporal, inapplicableOffset);
    }
  }

  @Test
  public void should_pass_when_within_offset() {
    temporalAssert.isCloseTo(closeTemporal, offset);
  }

  @Test
  public void should_pass_when_temporal_passed_as_string_is_within_offset() {
    temporalAssert.isCloseTo(closeTemporalAsString, offset);
  }

  @Test
  public void should_fail_outside_offset() {
    thrown.expectAssertionError(differenceMessage);
    temporalAssert.isCloseTo(farTemporal, offset);
  }

  @Test
  public void should_fail_when_temporal_passed_as_string_is_outside_offset() {
    thrown.expectAssertionError(differenceMessage);
    temporalAssert.isCloseTo(farTemporalAsString, offset);
  }

}
