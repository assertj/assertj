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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveNanos;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
@DisplayName("DurationAssert hasNanos")
class DurationAssert_hasNanos_Test {

  @Test
  void should_pass_if_duration_has_matching_nanos() {
    // GIVEN
    Duration duration = Duration.ofNanos(145692L);
    // WHEN/THEN
    assertThat(duration).hasNanos(145692L);
  }

  @Test
  void should_fail_when_duration_is_null() {
    // GIVEN
    Duration duration = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(duration).hasNanos(190L);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_duration_does_not_have_matching_nanos() {
    // GIVEN
    Duration duration = Duration.ofNanos(1892L);
    // WHEN
    ThrowingCallable code = () -> assertThat(duration).hasNanos(190L);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldHaveNanos(duration, 1892L, 190L).create());
  }

}
