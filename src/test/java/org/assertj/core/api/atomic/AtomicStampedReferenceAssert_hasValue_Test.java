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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.atomic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveReference.shouldHaveReference;
import static org.assertj.core.error.ShouldHaveStamp.shouldHaveStamp;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.atomic.AtomicStampedReference;

import org.junit.jupiter.api.Test;

class AtomicStampedReferenceAssert_hasValue_Test {

  private String expectedValue = "expectedValue";

  @Test
  void should_fail_when_atomicStampedReference_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((AtomicStampedReference<String>) null).hasReference(expectedValue))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null_and_does_not_contains_expected_value() {
    AtomicStampedReference<String> actual = new AtomicStampedReference<>("actual", 1234);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(null))
                                                   .withMessage(shouldHaveReference(actual, actual.getReference(),
                                                                                    null).create());
  }

  @Test
  void should_fail_if_atomicStampedReference_does_not_contain_expected_value() {
    AtomicStampedReference<String> actual = new AtomicStampedReference<>("actual", 1234);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(expectedValue))
                                                   .withMessage(shouldHaveReference(actual, actual.getReference(),
                                                                                    expectedValue).create());
  }

  @Test
  void should_pass_if_atomicStampedReference_contains_expected_value() {
    assertThat(new AtomicStampedReference<>(expectedValue, 1234)).hasReference(expectedValue);
  }

  @Test
  void should_pass_if_atomicStampedReference_contains_expected_value_and_has_expected_stamp() {
    int stamp = 1234;
    assertThat(new AtomicStampedReference<>(expectedValue, stamp)).hasReference(expectedValue).hasStamp(1234);
  }

  @Test
  void should_fail_if_atomicStampedReference_contains_expected_value_and_hasStamp_does_not() {
    int actualStamp = 1234;
    int expectedStamp = 5678;
    AtomicStampedReference<String> actual = new AtomicStampedReference<>(expectedValue, actualStamp);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(expectedValue)
                                                                                       .hasStamp(expectedStamp))
                                                   .withMessage(shouldHaveStamp(actual, expectedStamp).create());
  }
}
