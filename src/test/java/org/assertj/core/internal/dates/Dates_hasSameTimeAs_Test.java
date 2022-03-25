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
package org.assertj.core.internal.dates;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldHaveSameTime.shouldHaveSameTime;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Dates#hasSameTimeAs(AssertionInfo, Date, Date)}</code>.
 *
 * @author Natália Struharová
 */
public class Dates_hasSameTimeAs_Test extends DatesBaseTest {

  @Test
  void should_pass_if_actual_has_same_time() {
    // GIVEN
    Date expected = new Date();
    expected.setTime(actual.getTime());

    // WHEN/THEN
    dates.hasSameTimeAs(someInfo(), actual, expected);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;

    // THEN/WHEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.hasSameTimeAs(someInfo(), null, new Date()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    Date expected = null;

    // THEN/WHEN
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> dates.hasSameTimeAs(someInfo(), actual, expected))
                                                         .withMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_has_not_same_time() {
    // GIVEN
    Date expected = new Date();
    expected.setTime(actual.getTime() + 1);

    // WHEN
    Throwable error = catchThrowable(() -> dates.hasSameTimeAs(someInfo(), actual, expected));

    // THEN
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(someInfo(), shouldHaveSameTime(actual, expected));
  }

}
