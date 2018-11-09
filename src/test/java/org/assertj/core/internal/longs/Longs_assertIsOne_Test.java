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
package org.assertj.core.internal.longs;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsOne(AssertionInfo, Comparable)}</code>.
 *
 * @author Drummond Dawson
 */
public class Longs_assertIsOne_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_one() {
    longs.assertIsZero(someInfo(), 0L);
  }

  @Test
  public void should_fail_since_actual_is_not_one() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsOne(someInfo(), 0L))
                                                   .withMessage(format("%nExpecting:%n <1L>%nto be equal to:%n " +
                                                     "<0L>%nbut was not."));
  }

  @Test
  public void should_succeed_since_actual_is_not_one_whatever_custom_comparison_strategy_is() {
    longsWithAbsValueComparisonStrategy.assertIsOne(someInfo(), 1L);
  }

  @Test
  public void should_fail_since_actual_is_one_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsOne(someInfo(), 0L))
                                                   .withMessage(format("%nExpecting:%n <1L>%nto be equal to:%n " +
                                                     "<0L>%nbut was not."));
  }

}
