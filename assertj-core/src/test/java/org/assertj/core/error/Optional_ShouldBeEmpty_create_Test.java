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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.jupiter.api.Test;

class Optional_ShouldBeEmpty_create_Test {

  @Test
  void should_create_error_message_for_optional() {
    // WHEN
    String errorMessage = shouldBeEmpty(Optional.of("not-empty")).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting an empty Optional but was containing value: \"not-empty\""));
  }

  @Test
  void should_fail_with_empty_optional() {
    // WHEN
    // THEN
    thenExceptionOfType(NoSuchElementException.class).isThrownBy(() -> shouldBeEmpty(Optional.empty()).create());
  }

  @Test
  void should_create_error_message_for_optionaldouble() {
    // WHEN
    String errorMessage = shouldBeEmpty(OptionalDouble.of(1)).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting an empty OptionalDouble but was containing value: 1.0"));
  }

  @Test
  void should_create_error_message_for_optionalint() {
    // WHEN
    String errorMessage = shouldBeEmpty(OptionalInt.of(1)).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting an empty OptionalInt but was containing value: 1"));
  }

  @Test
  void should_create_error_message_for_optionallong() {
    // WHEN
    String errorMessage = shouldBeEmpty(OptionalLong.of(1L)).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting an empty OptionalLong but was containing value: 1L"));
  }
}
