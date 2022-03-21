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
package org.assertj.core.internal.maps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainValue.shouldContainValue;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Maps#assertHasValueSatisfying(AssertionInfo, Map, Condition)} (AssertionInfo, Map, Condition)}</code>.
 */
class Maps_assertHasValueSatisfying_Test extends MapsBaseTest {

  private Condition<String> isGreen = new Condition<String>("green color condition") {
    @Override
    public boolean matches(String value) {
      return "green".equals(value);
    }
  };

  private Condition<Object> isBlack = new Condition<Object>("black color condition") {
    @Override
    public boolean matches(Object value) {
      return "black".equals(value);
    }
  };

  @Test
  void should_fail_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertHasValueSatisfying(someInfo(), actual, null))
                                    .withMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertHasValueSatisfying(someInfo(), null, isGreen))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_matching_condition() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> maps.assertHasValueSatisfying(info, actual, isBlack));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainValue(actual, isBlack));
  }

  @Test
  void should_pass_if_actual_contains_a_value_matching_the_given_condition() {
    maps.assertHasValueSatisfying(someInfo(), actual, isGreen);
  }
}
