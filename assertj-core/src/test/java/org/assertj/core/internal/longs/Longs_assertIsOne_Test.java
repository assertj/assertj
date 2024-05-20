/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.longs;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.testkit.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsOne(AssertionInfo, Comparable)}</code>.
 *
 * @author Drummond Dawson
 */
class Longs_assertIsOne_Test extends LongsBaseTest {

  @Test
  void should_succeed_since_actual_is_one() {
    longs.assertIsOne(someInfo(), 1L);
  }

  @Test
  void should_fail_since_actual_is_not_one() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsOne(someInfo(), 0L))
                                                   .withMessage(shouldBeEqualMessage("0L", "1L"));
  }

  @Test
  void should_succeed_since_actual_is_not_one_whatever_custom_comparison_strategy_is() {
    longsWithAbsValueComparisonStrategy.assertIsOne(someInfo(), 1L);
  }

  @Test
  void should_fail_since_actual_is_one_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsOne(someInfo(),
                                                                                                                     0L))
                                                   .withMessage(shouldBeEqualMessage("0L", "1L"));
  }

}
