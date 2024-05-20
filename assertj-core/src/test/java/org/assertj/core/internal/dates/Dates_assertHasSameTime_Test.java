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
package org.assertj.core.internal.dates;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameTime.shouldHaveSameTime;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Date;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Dates#assertHasSameTime(AssertionInfo, Date, Date)}</code>.
 *
 * @author Natália Struharová
 */
class Dates_assertHasSameTime_Test extends DatesBaseTest {

  @Test
  void should_pass_if_actual_has_same_time() {
    // GIVEN
    Date expected = new Date(actual.getTime());
    // WHEN/THEN
    dates.assertHasSameTime(INFO, actual, expected);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> dates.assertHasSameTime(INFO, null, new Date()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    Date expected = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> dates.assertHasSameTime(INFO, actual, expected))
                                    .withMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_has_not_same_time() {
    // GIVEN
    Date expected = new Date(actual.getTime() + 1);
    // WHEN
    expectAssertionError(() -> dates.assertHasSameTime(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldHaveSameTime(actual, expected));
  }

}
