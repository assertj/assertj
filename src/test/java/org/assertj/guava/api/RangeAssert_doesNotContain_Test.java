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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

/**
 * @author Marcin Kwaczyński
 */
public class RangeAssert_doesNotContain_Test {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Range<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContain(1));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_when_range_contains_unexpected_value() {
    // GIVEN
    final Range<Integer> actual = Range.closed(1, 10);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContain(9, 10, 11));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(format("%nExpecting%n" +
                                            "  [1..10]%n" +
                                            "not to contain%n" +
                                            "  [9, 10, 11]%n" +
                                            "but found%n" +
                                            "  [9, 10]%n"));
  }

  @Test
  public void should_pass_if_range_does_not_contain_values() throws Exception {
    // GIVEN
    final Range<Integer> actual = Range.closedOpen(1, 10);
    // THEN
    assertThat(actual).doesNotContain(10);
  }
}
