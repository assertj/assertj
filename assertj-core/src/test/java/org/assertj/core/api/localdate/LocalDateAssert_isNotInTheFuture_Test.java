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
package org.assertj.core.api.localdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNotInTheFuture.shouldBeNotInTheFuture;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class LocalDateAssert_isNotInTheFuture_Test extends LocalDateAssertBaseTest {

  @Test
  void should_pass_if_actual_is_in_the_past() {
    assertThat(BEFORE).isNotInTheFuture();
  }

  @Test
  void should_pass_if_actual_is_today() {
    assertThat(REFERENCE).isNotInTheFuture();
  }

  @Test
  void should_fail_if_actual_is_in_the_future() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(AFTER).isNotInTheFuture());
    // THEN
    then(assertionError).hasMessage(shouldBeNotInTheFuture(AFTER).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDate actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotInTheFuture());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_collect_error_in_soft_assertions() {
    // GIVEN
    var softly = new SoftAssertions();
    // WHEN
    softly.assertThat(AFTER).isNotInTheFuture();
    // THEN
    then(softly.errorsCollected()).hasSize(1);
  }

}
