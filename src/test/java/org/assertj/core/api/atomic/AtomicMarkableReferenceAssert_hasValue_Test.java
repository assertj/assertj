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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.atomic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeMarked.shouldBeMarked;
import static org.assertj.core.error.ShouldBeMarked.shouldNotBeMarked;
import static org.assertj.core.error.ShouldHaveReference.shouldHaveReference;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.atomic.AtomicMarkableReference;

import org.junit.jupiter.api.Test;

public class AtomicMarkableReferenceAssert_hasValue_Test {

  private String expectedValue = "expectedValue";

  @Test
  public void should_fail_when_AtomicMarkableReference_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((AtomicMarkableReference<String>) null).hasReference(expectedValue))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_expected_value_is_null_and_does_not_contain_expected_value() {
    AtomicMarkableReference<String> actual = new AtomicMarkableReference<>("actual", true);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(null))
                                                   .withMessage(shouldHaveReference(actual, actual.getReference(), null).create());
  }

  @Test
  public void should_fail_if_atomicMarkableReference_does_not_contain_expected_value() {
    AtomicMarkableReference<String> actual = new AtomicMarkableReference<>("actual", true);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(expectedValue))
                                                   .withMessage(shouldHaveReference(actual, actual.getReference(), expectedValue).create());
  }

  @Test
  public void should_pass_if_AtomicMarkableReference_contains_expected_value() {
    assertThat(new AtomicMarkableReference<>(expectedValue, true)).hasReference(expectedValue);
    assertThat(new AtomicMarkableReference<>(expectedValue, true)).hasReference(expectedValue);
  }

  @Test
  public void should_pass_if_atomicMarkableReference_contains_expected_value_and_is_marked() {
    assertThat(new AtomicMarkableReference<>(expectedValue, true)).hasReference(expectedValue).isMarked();
  }

  @Test
  public void should_pass_if_atomicMarkableReference_contains_expected_value_and_is_not_marked() {
    assertThat(new AtomicMarkableReference<>(expectedValue, false)).hasReference(expectedValue).isNotMarked();
  }

  @Test
  public void should_fail_if_atomicMarkableReference_contains_expected_value_and_is_not_marked() {
    AtomicMarkableReference<String> actual = new AtomicMarkableReference<>(expectedValue, false);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(expectedValue).isMarked())
                                                   .withMessage(shouldBeMarked(actual).create());
  }

  @Test
  public void should_fail_if_atomicMarkableReference_contains_expected_value_and_is_marked() {
    AtomicMarkableReference<String> actual = new AtomicMarkableReference<>(expectedValue, true);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasReference(expectedValue).isNotMarked().isMarked())
                                                   .withMessage(shouldNotBeMarked(actual).create());
  }
}
