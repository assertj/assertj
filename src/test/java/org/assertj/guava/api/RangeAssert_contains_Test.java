/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Range;

/**
 * @author Marcin Kwaczyński
 */
public class RangeAssert_contains_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Range<Integer> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).contains(1);
  }

  @Test
  public void should_fail_if_expected_values_are_null() {
    // given
    Range<Integer> actual = Range.closedOpen(1, 10);
    // expect
    expectException(IllegalArgumentException.class, "The values to look for should not be null");
    // when
    assertThat(actual).contains((Integer[]) null);
  }

  @Test
  public void should_fail_if_expected_values_group_is_empty_and_actual_is_not() {
    // given
    Range<Integer> actual = Range.openClosed(1, 2);
    // expect
    expectException(IllegalArgumentException.class, "The values to look for should not be empty");
    // when
    assertThat(actual).contains();
  }

  @Test
  public void should_pass_if_both_actual_and_expected_values_are_empty() {
    // given
    Range<Integer> actual = Range.openClosed(1, 1);
    // when
    assertThat(actual).contains();
    // then
    // pass
  }

  @Test
  public void should_fail_when_range_does_not_contain_expected_values() {
    // given
    final Range<Integer> actual = Range.closedOpen(1, 10);
    // expect
    expectException(AssertionError.class, "%nExpecting:%n" +
                                                 " <[1‥10)>%n" +
                                                 "to contain:%n" +
                                                 " <[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]>%n" +
                                                 "but could not find:%n" +
                                                 " <[10]>%n");
    // when
    assertThat(actual).contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  @Test
  public void should_pass_if_range_contains_values() throws Exception {
    // given
    final Range<Integer> actual = Range.closed(1, 10);
    // when
    assertThat(actual).contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    // then
    // pass
  }
}
