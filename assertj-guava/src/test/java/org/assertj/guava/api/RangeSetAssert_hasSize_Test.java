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
package org.assertj.guava.api;

import static com.google.common.collect.Range.closed;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;

class RangeSetAssert_hasSize_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasSize(5));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_size_does_not_match_expected() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.<Integer> builder()
                                                .add(closed(1, 10))
                                                .add(closed(20, 35))
                                                .add(closed(40, 45))
                                                .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasSize(5));
    // THEN
    then(error).isInstanceOf(AssertionError.class)
               .hasMessage(shouldHaveSize(actual, actual.asRanges().size(), 5).create());
  }

  @Test
  void should_pass_if_actual_size_matches_expected() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.<Integer> builder()
                                                .add(closed(1, 10))
                                                .add(closed(20, 35))
                                                .add(closed(40, 45))
                                                .build();
    // WHEN/THEN
    assertThat(actual).hasSize(actual.asRanges().size());
  }

}
