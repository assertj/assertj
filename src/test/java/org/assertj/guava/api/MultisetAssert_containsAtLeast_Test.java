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
package org.assertj.guava.api;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

/**
 * @author Max Daniline
 */
public class MultisetAssert_containsAtLeast_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Multiset<String> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).containsAtLeast(1, "test");
  }

  @Test
  public void should_fail_if_expected_is_negative() {
    // given
    Multiset<String> actual = HashMultiset.create();
    // expect
    expectException(IllegalArgumentException.class, "The minimum count should not be negative.");
    // when
    assertThat(actual).containsAtLeast(-1, "test");
  }

  @Test
   public void should_fail_if_actual_contains_value_fewer_times_than_expected() {
    // given
    Multiset<String> actual = HashMultiset.create();
    actual.add("test", 2);
    // expect
    expectException(AssertionError.class, "%nExpecting:%n" +
                                          "  <[\"test\", \"test\"]>%n" +
                                          "to contain:%n" +
                                          "  <\"test\">%n" +
                                          "at least 3 times but was found 2 times.");
    // when
    assertThat(actual).containsAtLeast(3, "test");
  }

  @Test
  public void should_pass_if_actual_contains_value_number_of_times_expected() {
    // given
    Multiset<String> actual = HashMultiset.create();
    actual.add("test", 2);
    // when
    assertThat(actual).containsAtLeast(2, "test");
    // then pass
  }

  @Test
  public void should_pass_if_actual_contains_value_more_times_than_expected() {
    // given
    Multiset<String> actual = HashMultiset.create();
    actual.add("test", 2);
    // when
    assertThat(actual).containsAtLeast(1, "test");
    // then pass
  }
}
