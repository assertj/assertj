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
package org.assertj.tests.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Filip Hrisafov
 */
class DurationAssert_isPositive_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Duration actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPositive());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "PT0S", "PT-1S" })
  void should_fail_if_actual_is_not_positive(Duration actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPositive());
    // THEN
    then(assertionError).hasMessage(shouldBeGreater(actual, Duration.ZERO).create());
  }

  @Test
  void should_pass_if_actual_is_positive() {
    // GIVEN
    Duration actual = Duration.ofSeconds(1);
    // WHEN/THEN
    assertThat(actual).isPositive();
  }

}
