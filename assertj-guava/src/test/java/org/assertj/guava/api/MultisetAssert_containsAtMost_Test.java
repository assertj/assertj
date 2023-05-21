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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class MultisetAssert_containsAtMost_Test {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Multiset<String> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAtMost(1, "test"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_expected_is_negative() {
    // GIVEN
    Multiset<String> actual = HashMultiset.create();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAtMost(-1, "test"));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("The maximum count should not be negative.");
  }

  @Test
  public void should_pass_if_actual_contains_value_fewer_times_than_expected() {
    // GIVEN
    Multiset<String> actual = HashMultiset.create();
    actual.add("test", 2);
    // when
    assertThat(actual).containsAtMost(3, "test");
    // THEN pass
  }

  @Test
  public void should_pass_if_actual_contains_value_number_of_times_expected() {
    // GIVEN
    Multiset<String> actual = HashMultiset.create();
    actual.add("test", 2);
    // when
    assertThat(actual).containsAtMost(2, "test");
    // THEN pass
  }

  @Test
  public void should_fail_if_actual_contains_value_more_times_than_expected() {
    // GIVEN
    Multiset<String> actual = HashMultiset.create();
    actual.add("test", 2);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAtMost(1, "test"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(format("%nExpecting:%n" +
                                            "  [\"test\", \"test\"]%n" +
                                            "to contain:%n" +
                                            "  \"test\"%n" +
                                            "at most 1 times but was found 2 times."));
  }
}
