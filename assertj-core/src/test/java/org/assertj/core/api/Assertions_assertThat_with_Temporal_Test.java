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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.jupiter.api.Test;

class Assertions_assertThat_with_Temporal_Test {

  @Test
  void should_create_Assert() {
    TemporalAssert assertions = assertThat((Temporal) ZonedDateTime.now());
    then(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    ZonedDateTime temporal = ZonedDateTime.now();
    // WHEN
    TemporalAssert assertions = assertThat((Temporal) temporal);
    // THEN
    then(assertions.getActual()).isSameAs(temporal);
  }

  @Test
  void should_not_be_ambiguous() {
    // GIVEN
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    Temporal temporal = ZonedDateTime.now();
    // WHEN/THEN
    then(assertThat(temporal)).isExactlyInstanceOf(TemporalAssert.class);
    then(assertThat(zonedDateTime)).isExactlyInstanceOf(ZonedDateTimeAssert.class);
  }
}
