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
 * Copyright 2012-2013 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Range;

/**
 * @author Marcin Kwaczyński
 */
public class RangeAssert_hasOpenedUpperBound_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Range<Integer> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).hasOpenedUpperBound();
  }

  @Test
  public void should_fail_when_range_has_closed_upper_bound() {
    // given
    final Range<Integer> actual = Range.openClosed(1, 10);
    // expect
    expectException(AssertionError.class, "\nExpecting:\n<(1‥10]>\nto be opened in the upper bound\nbut is closed");
    // when
    assertThat(actual).hasOpenedUpperBound();
  }

  @Test
  public void should_pass_if_range_has_opened_upper_bound() throws Exception {
    // given
    final Range<Integer> actual = Range.closedOpen(1, 10);

    // when
    assertThat(actual).hasOpenedUpperBound();

    // then
    // pass
  }
}
