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
package org.assertj.core.api.atomic.reference;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

class AtomicReferenceAssert_doesNotHaveNullValue_Test {

  @Test
  void should_pass_when_actual_does_not_have_the_null_value() {
    AtomicReference<String> actual = new AtomicReference<>("foo");
    assertThat(actual).doesNotHaveNullValue();
  }

  @Test
  void should_fail_when_actual_has_the_null_value() {
    AtomicReference<String> actual = new AtomicReference<>(null);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).doesNotHaveNullValue())
                                                   .withMessage(shouldNotContainValue(actual, null).create());
  }

}
