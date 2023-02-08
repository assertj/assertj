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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class OptionalAssert_map_Test {

  @Test
  void should_fail_when_optional_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Optional<String>) null).map(String::length))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_when_optional_is_empty() {
    assertThat(Optional.<String> empty()).map(String::length).isEmpty();
  }

  @Test
  void should_pass_when_optional_contains_a_value() {
    assertThat(Optional.of("42")).map(String::length).contains(2);
    assertThat(Optional.of("42")).map(s -> null).isEmpty();
  }

}
