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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author Nicolas François
 * @author Joel Costigliola
 */
class ObjectArrays_assertHasSameSizeAs_with_Array_Test extends ObjectArraysBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasSameSizeAs(INFO, null, array("Leia", "Luke")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null() {
    // GIVEN
    String[] other = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasSameSizeAs(INFO, actual, other));
    // THEN
    then(error).hasMessage("%nExpecting an array but was: null".formatted());
  }

  @Test
  void should_fail_if_actual_size_is_not_equal_to_other_size() {
    // GIVEN
    String[] other = array("Solo", "Leia");
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasSameSizeAs(INFO, actual, other));
    // THEN
    then(error).hasMessage(shouldHaveSameSizeAs(actual, other, actual.length, 2).create());
  }

  @Test
  void should_pass_if_actual_has_same_size_as_other() {
    arrays.assertHasSameSizeAs(INFO, array("Solo", "Leia"), array("Solo", "Leia"));
  }
}
