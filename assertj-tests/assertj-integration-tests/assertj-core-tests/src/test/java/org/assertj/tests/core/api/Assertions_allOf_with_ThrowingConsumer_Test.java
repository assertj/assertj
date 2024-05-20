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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.MultipleFailuresError;

class Assertions_allOf_with_ThrowingConsumer_Test {

  @Test
  void should_pass_if_all_consumers_pass() {
    // GIVEN
    ThrowingConsumer<String> underTest = allOf(e -> assertThat(e).isNotNull(),
                                               e -> assertThat(e).isNotBlank());
    // WHEN/THEN
    underTest.accept("value");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = "value")
  void should_fail_if_any_consumer_fails(String value) {
    // GIVEN
    ThrowingConsumer<String> underTest = allOf(e -> assertThat(e).isBlank(),
                                               e -> assertThat(e).isNotBlank());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> underTest.accept(value));
    // THEN
    then(assertionError).isInstanceOf(MultipleFailuresError.class);
  }

}
