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
package org.assertj.tests.core.api.iterable;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class IterableAssert_isEmpty_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterable<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEmpty());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_empty() {
    // GIVEN
    Iterable<?> actual = List.of("element");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBeEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    Iterable<?> actual = List.of();
    // WHEN/THEN
    assertThat(actual).isEmpty();
  }

}
