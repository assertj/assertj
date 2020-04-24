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
package org.assertj.guava.api;

import static com.google.common.collect.ImmutableRangeSet.of;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.TreeRangeSet.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.RangeSet;

/**
  * Tests for <code>{@link RangeSetAssert#isNullOrEmpty()}</code>.
  *
  * @author Ilya Koshaleu
  */
@DisplayName("RangeSetAssert isNullOrEmpty")
class RangeSetAssert_isNullOrEmpty_Test {

  @Test
  void should_pass_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // THEN
    assertThat(actual).isNullOrEmpty();
  }

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).isNullOrEmpty();
  }

  @Test
  void should_fail_if_actual_is_non_null_and_non_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).isNullOrEmpty());
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldBeNullOrEmpty(actual).create());
  }
}
