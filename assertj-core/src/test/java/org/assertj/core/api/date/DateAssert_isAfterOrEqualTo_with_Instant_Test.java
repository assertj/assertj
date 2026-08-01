/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAfterOrEqualTo.shouldBeAfterOrEqualTo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.Test;

/**
 * Behavioral tests for {@link org.assertj.core.api.AbstractDateAssert#isAfterOrEqualTo(Instant)}.
 * <p>
 * Covers the case where a millis-only {@link Timestamp} (e.g. from {@code System.currentTimeMillis()}) is
 * compared to an {@link Instant} with sub-millisecond precision — comparison correctly fails due to nanos,
 * and the failure message must show that precision (see issue #4241).
 */
class DateAssert_isAfterOrEqualTo_with_Instant_Test {

  @Test
  void should_pass_if_timestamp_is_after_or_equal_to_instant() {
    // GIVEN
    Instant instant = Instant.parse("2011-01-01T00:00:00.123456Z");
    Timestamp later = Timestamp.from(instant.plusMillis(1));
    Timestamp equalWithNanos = Timestamp.from(instant);
    // WHEN/THEN
    then(later).isAfterOrEqualTo(instant);
    then(equalWithNanos).isAfterOrEqualTo(instant);
  }

  @Test
  void should_fail_if_millis_only_timestamp_is_before_instant_with_sub_ms_precision() {
    // GIVEN — Instant has micros; Timestamp from epoch millis truncates them
    Instant withMicros = Instant.parse("2011-01-01T00:00:00.123456Z");
    Timestamp actualMillisOnly = new Timestamp(withMicros.toEpochMilli());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actualMillisOnly).isAfterOrEqualTo(withMicros));
    // THEN — message uses Timestamp.from(instant) as the comparison target (dateFrom)
    then(assertionError).hasMessage(shouldBeAfterOrEqualTo(actualMillisOnly, Timestamp.from(withMicros)).create());
    then(assertionError).hasMessageContaining("123456");
  }

}
