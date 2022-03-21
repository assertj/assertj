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
package org.assertj.core.internal.doublearrays;

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.jupiter.api.Test;

class DoubleArrays_assertHasSameSizeAs_with_Iterable_Test extends DoubleArraysBaseTest {

  private final List<String> other = list("Solo", "Leia", "Luke");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    ThrowingCallable code = () -> arrays.assertHasSameSizeAs(someInfo(), actual, other);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    AssertionInfo info = someInfo();
    List<String> other = newArrayList("Solo", "Leia");
    // WHEN
    ThrowingCallable code = () -> arrays.assertHasSameSizeAs(info, actual, other);
    // THEN
    String error = shouldHaveSameSizeAs(actual, other, actual.length, other.size()).create(null, info.representation());
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSameSizeAs(someInfo(), actual, other);
  }
}
