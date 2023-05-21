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
package org.assertj.guava.api;

import static com.google.common.collect.Range.closed;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;

/**
  * @author Ilya Koshaleu
  */
class RangeSetAssert_isNullOrEmpty_Test {

  @Test
  void should_pass_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN/THEN
    assertThat(actual).isNullOrEmpty();
  }

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    // THEN
    assertThat(actual).isNullOrEmpty();
  }

  @Test
  void should_fail_if_actual_is_not_null_and_not_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(1, 10));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isNullOrEmpty());
    // WHEN/THEN
    then(error).hasMessage(shouldBeNullOrEmpty(actual).create());
  }

}
