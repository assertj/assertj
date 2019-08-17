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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OffsetDateTimeByInstantComparatorTest {

  private OffsetDateTimeByInstantComparator comparator;

  @BeforeEach
  public void setUp() {
    comparator = OffsetDateTimeByInstantComparator.getInstance();
  }

  @Test
  public void should_have_one_instance() {
    assertThat(comparator).isSameAs(OffsetDateTimeByInstantComparator.getInstance());
  }

  @Test
  public void should_have_description() {
    assertThat(comparator.description()).isEqualTo("OffsetDateTime.timeLineOrder()");
  }

  @Test
  public void should_disregard_time_zone_difference() {
    ZonedDateTime now = ZonedDateTime.now();
    OffsetDateTime inParis = now.withZoneSameInstant(ZoneId.of("Europe/Paris")).toOffsetDateTime();
    OffsetDateTime inNewYork = now.withZoneSameInstant(ZoneId.of("America/New_York")).toOffsetDateTime();

    assertThat(inParis.compareTo(inNewYork)).as("Built-in comparison should report that they differ").isNotZero();
    assertThat(comparator.compare(inParis, inNewYork)).isZero();
  }
}
