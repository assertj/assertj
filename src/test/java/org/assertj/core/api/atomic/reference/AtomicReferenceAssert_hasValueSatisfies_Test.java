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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.atomic.reference;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class AtomicReferenceAssert_hasValueSatisfies_Test {

  @Test
  void should_pass_when_actual_has_value_which_is_satisfies() {
    String initialValue = "FOO";
    String expectedValue = "foo";

    AtomicReference<String> actual = new AtomicReference<>(initialValue);

    assertThat(actual).hasValueSatisfies(value -> assertThat(value).isEqualToIgnoringCase(expectedValue));
  }

  @Test
  void should_fail_when_actual_has_value_which_does_not_satisfies() {
    String initialValue = "foo";
    String expectedValue = "bar";

    AtomicReference<String> actual = new AtomicReference<>(initialValue);

    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThat(actual).hasValueSatisfies(value -> assertThat(value).isEqualToIgnoringCase(expectedValue)))
      .withMessageContaining("<\"" + initialValue + "\">")
      .withMessageContaining("<\"" + expectedValue + "\">");
  }

  @Test
  void should_fail_when_requirements_is_null() {
    String initialValue = "foo";

    AtomicReference<String> actual = new AtomicReference<>(initialValue);

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> assertThat(actual).hasValueSatisfies(null))
      .withMessageContaining("Consumer");
  }

  @Test
  void should_fail_when_actual_is_null() {
    String expectedValue = "foo";

    AtomicReference<String> actual = null;

    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThat(actual).hasValueSatisfies(value -> assertThat(value).isEqualToIgnoringCase(expectedValue)))
      .withMessage(actualIsNull());
  }

}
