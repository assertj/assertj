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
package org.assertj.core.api.offsetdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;
import org.assertj.core.api.AbstractOffsetDateTimeAssertBaseTest;
import org.assertj.core.api.OffsetDateTimeAssert;
import org.junit.jupiter.api.Test;

class OffsetDateTimeAssert_isInTheFuture_Test extends AbstractOffsetDateTimeAssertBaseTest {

  @Override
  protected OffsetDateTimeAssert invoke_api_method() {
    return assertions;
  }

  @Override
  protected void verify_internal_effects() {
    // NO-OP
  }

  @Test
  void should_pass_if_actual_is_in_the_future() {
    assertThat(AFTER).isInTheFuture();
  }

  @Test
  void should_pass_if_actual__with_different_offset_is_in_the_future() {
    assertThat(AFTER_WITH_DIFFERENT_OFFSET).isInTheFuture();
  }

  @Test
  void should_fail_if_actual_is_now() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(REFERENCE).isInTheFuture());
    // THEN
    then(assertionError).hasMessage(shouldBeInTheFuture(REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_with_different_offset_is_now() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(REFERENCE_WITH_DIFFERENT_OFFSET).isInTheFuture());
    // THEN
    then(assertionError).hasMessage(shouldBeInTheFuture(REFERENCE_WITH_DIFFERENT_OFFSET).create());
  }

  @Test
  void should_fail_if_actual_is_in_the_past() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(BEFORE).isInTheFuture());
    // THEN
    then(assertionError).hasMessage(shouldBeInTheFuture(BEFORE).create());
  }

  @Test
  void should_fail_if_actual_with_different_offset_is_in_the_past() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(BEFORE_WITH_DIFFERENT_OFFSET).isInTheFuture());
    // THEN
    then(assertionError).hasMessage(shouldBeInTheFuture(BEFORE_WITH_DIFFERENT_OFFSET).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    OffsetDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isInTheFuture());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }
}
