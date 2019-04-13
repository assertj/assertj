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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;

import static org.assertj.core.api.Assertions.*;

class ChronoZonedDateTimeByInstantComparatorTest {

  private ChronoZonedDateTimeByInstantComparator comparator;

  @BeforeEach
  public void setUp() {
    comparator = ChronoZonedDateTimeByInstantComparator.getInstance();
  }

  @Test
  public void should_have_one_instance() {
    assertThat(comparator).isSameAs(ChronoZonedDateTimeByInstantComparator.getInstance());
  }

  @Test
  public void should_have_description() {
    assertThat(comparator.description()).isEqualTo("ChronoZonedDateTimeByInstantComparator");
  }

  @Test
  public void should_disregard_time_zone_difference() {
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime inParis = now.withZoneSameInstant(ZoneId.of("Europe/Paris"));
    ZonedDateTime inNewYork = now.withZoneSameInstant(ZoneId.of("America/New_York"));

    assertThat(inParis.compareTo(inNewYork)).as("Built-in comparison should report that they differ").isNotZero();
    assertThat(comparator.compare(inParis, inNewYork)).isZero();
  }

  @Test
  public void should_disregard_chronology_difference() {
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime inTokyo = now.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
    ChronoZonedDateTime<JapaneseDate> inTokyoJapanese = JapaneseChronology.INSTANCE.zonedDateTime(now);

    assertThat(inTokyoJapanese.compareTo(inTokyo)).as("Built-in comparison should report that they differ").isNotZero();
    assertThat(comparator.compare(inTokyoJapanese, inTokyo)).isZero();
  }
}
