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
package org.assertj.tests.core.api.enumerable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class AbstractEnumerableAssert_hasSameSizeAs_with_Array_Test {

  @Test
  void should_pass_if_actual_primitive_array_has_same_size_as_other_object_array() {
    assertThat(new byte[] { 1, 2 }).hasSameSizeAs(new Byte[] { 2, 3 });
    assertThat(new int[] { 1, 2 }).hasSameSizeAs(new String[] { "1", "2" });
  }

  @Test
  void should_pass_if_actual_primitive_array_has_same_size_as_other_primitive_array() {
    assertThat(new byte[] { 1, 2 }).hasSameSizeAs(new byte[] { 2, 3 });
    assertThat(new byte[] { 1, 2 }).hasSameSizeAs(new int[] { 2, 3 });
  }

  @Test
  void should_pass_if_actual_object_array_has_same_size_as_other_object_array() {
    assertThat(new String[] { "1", "2" }).hasSameSizeAs(new Byte[] { 2, 3 });
    assertThat(new String[] { "1", "2" }).hasSameSizeAs(new String[] { "1", "2" });
  }

  @Test
  void should_pass_if_actual_object_array_has_same_size_as_other_primitive_array() {
    assertThat(new String[] { "1", "2" }).hasSameSizeAs(new byte[] { 2, 3 });
    assertThat(new String[] { "1", "2" }).hasSameSizeAs(new int[] { 2, 3 });
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    final byte[] actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(new byte[] { 2, 3 }));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_not_an_array() {
    // GIVEN
    byte[] actual = { 1, 2 };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs("a string"));
    // THEN
    then(assertionError).hasMessage("%nExpecting an array but was: \"a string\"".formatted());
  }

  @Test
  void should_fail_if_size_of_actual_has_same_as_other_array() {
    // GIVEN
    final byte[] actual = new byte[] { 1, 2 };
    final byte[] other = new byte[] { 1, 2, 3 };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, other, actual.length, other.length).create());
  }

}
