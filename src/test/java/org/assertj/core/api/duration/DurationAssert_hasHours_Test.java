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
package org.assertj.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveHours;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
@DisplayName("DurationAssert hasHours")
class DurationAssert_hasHours_Test extends BaseTest {

  @Test
  void should_pass_if_duration_has_matching_hours() {
    assertThat(Duration.ofHours(4L)).hasHours(4L);
  }

  @Test
  void should_fail_when_duration_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> assertThat((Duration) null).hasHours(5L)).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_duration_does_not_have_matching_hours() {
    assertThatAssertionErrorIsThrownBy(() -> assertThat(Duration.ofHours(10L)).hasHours(15L))
      .withMessage(shouldHaveHours(Duration.ofHours(10L), 10L, 15L).create());
  }

}
