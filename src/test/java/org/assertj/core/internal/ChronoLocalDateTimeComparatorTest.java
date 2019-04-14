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

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.JapaneseDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChronoLocalDateTimeComparatorTest {

  private ChronoLocalDateTimeComparator comparator;

  @BeforeEach
  void setUp() {
    comparator = ChronoLocalDateTimeComparator.getInstance();
  }

  @Test
  void should_have_one_instance() {
    assertThat(comparator).isSameAs(ChronoLocalDateTimeComparator.getInstance());
  }

  @Test
  void should_have_description() {
    assertThat(comparator.description()).isEqualTo("ChronoLocalDateTimeComparator");
  }

  @Test
  void should_disregard_chronology_difference() {
    LocalDateTime now = LocalDateTime.now();
    JapaneseDate japaneseDate = JapaneseDate.from(now);
    ChronoLocalDateTime<JapaneseDate> nowInJapanese = japaneseDate.atTime(now.toLocalTime());

    assertThat(now.compareTo(nowInJapanese)).as("Built-in comparison should report that they differ").isNotZero();
    assertThat(comparator.compare(now, nowInJapanese)).isZero();
    assertThat(comparator.compare(now, now)).isZero();
  }
}
