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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveDays;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveMonths;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveYears;

import java.time.Period;

import org.junit.jupiter.api.Test;

/**
 * @author Hayden Meloche
 */
class ShouldHavePeriod_create_test {

  @Test
  void should_create_error_message_for_years() {
    // GIVEN
    int actualYears = 1;
    int expectedYears = 2;
    Period period = Period.ofYears(actualYears);
    // WHEN
    String errorMessage = shouldHaveYears(period, actualYears, expectedYears).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P1Y>%nto have 2 years but had 1"));
  }

  @Test
  void should_create_error_message_for_1_year() {
    // GIVEN
    int actualYears = 2;
    int expectedYears = 1;
    Period period = Period.ofYears(actualYears);
    // WHEN
    String errorMessage = shouldHaveYears(period, actualYears, expectedYears).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P2Y>%nto have 1 year but had 2"));
  }

  @Test
  void should_create_error_message_for_negative_1_year() {
    // GIVEN
    int actualYears = 2;
    int expectedYears = -1;
    Period period = Period.ofYears(actualYears);
    // WHEN
    String errorMessage = shouldHaveYears(period, actualYears, expectedYears).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P2Y>%nto have -1 year but had 2"));
  }

  @Test
  void should_create_error_message_for_days() {
    // GIVEN
    int actualDays = 1;
    int expectedDays = 2;
    Period period = Period.ofDays(actualDays);
    // WHEN
    String errorMessage = shouldHaveDays(period, actualDays, expectedDays).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P1D>%nto have 2 days but had 1"));
  }

  @Test
  void should_create_error_message_for_1_day() {
    // GIVEN
    int actualDays = 2;
    int expectedDays = 1;
    Period period = Period.ofDays(actualDays);
    // WHEN
    String errorMessage = shouldHaveDays(period, actualDays, expectedDays).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P2D>%nto have 1 day but had 2"));
  }

  @Test
  void should_create_error_message_for_negative_1_days() {
    // GIVEN
    int actualDays = 1;
    int expectedDays = -1;
    Period period = Period.ofDays(actualDays);
    // WHEN
    String errorMessage = shouldHaveDays(period, actualDays, expectedDays).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P1D>%nto have -1 day but had 1"));
  }

  @Test
  void should_create_error_message_for_months() {
    // GIVEN
    int actualMonths = 1;
    int expectedMonths = 2;
    Period period = Period.ofMonths(actualMonths);
    // WHEN
    String errorMessage = shouldHaveMonths(period, actualMonths, expectedMonths).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P1M>%nto have 2 months but had 1"));
  }

  @Test
  void should_create_error_message_for_1_months() {
    // GIVEN
    int actualMonths = 2;
    int expectedMonths = 1;
    Period period = Period.ofMonths(actualMonths);
    // WHEN
    String errorMessage = shouldHaveMonths(period, actualMonths, expectedMonths).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P2M>%nto have 1 month but had 2"));
  }

  @Test
  void should_create_error_message_for_negative_1_months() {
    // GIVEN
    int actualMonths = 2;
    int expectedMonths = -1;
    Period period = Period.ofMonths(actualMonths);
    // WHEN
    String errorMessage = shouldHaveMonths(period, actualMonths, expectedMonths).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Period:%n <P2M>%nto have -1 month but had 2"));
  }
}
