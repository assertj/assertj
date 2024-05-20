/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveMillis;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
@DisplayName("DurationAssert hasMillis")
class DurationAssert_hasMillis_Test {

  @Test
  void should_pass_if_duration_has_matching_millis() {
    // GIVEN
    Duration duration = Duration.ofMillis(32893L);
    // WHEN/THEN
    assertThat(duration).hasMillis(32893L);
  }

  @Test
  void should_fail_when_duration_is_null() {
    // GIVEN
    Duration duration = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(duration).hasMillis(190L);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_duration_does_not_have_matching_millis() {
    // GIVEN
    Duration duration = Duration.ofMillis(5866L);
    // WHEN
    ThrowingCallable code = () -> assertThat(duration).hasMillis(758L);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldHaveMillis(duration, 5866L, 758L).create());
  }

}
