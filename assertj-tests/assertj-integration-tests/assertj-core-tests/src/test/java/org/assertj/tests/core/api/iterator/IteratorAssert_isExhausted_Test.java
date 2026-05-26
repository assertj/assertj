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
package org.assertj.tests.core.api.iterator;

import static java.util.Collections.emptyIterator;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

class IteratorAssert_isExhausted_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isExhausted());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_at_least_one_element() {
    // GIVEN
    Iterator<?> actual = singleton(42).iterator();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isExhausted());
    // THEN
    then(assertionError).hasMessageContaining(shouldBeExhausted().create());
  }

  @Test
  void should_pass_if_actual_has_no_more_elements() {
    // GIVEN
    Iterator<?> actual = emptyIterator();
    // WHEN/THEN
    assertThat(actual).isExhausted();
  }

}
