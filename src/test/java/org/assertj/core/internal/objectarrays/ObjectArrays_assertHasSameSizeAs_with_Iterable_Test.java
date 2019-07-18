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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertHasSameSizeAs(AssertionInfo, Object[], Iterable)}</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ObjectArrays_assertHasSameSizeAs_with_Iterable_Test extends ObjectArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    ThrowingCallable code = () -> arrays.assertHasSameSizeAs(someInfo(), actual, list("Solo", "Leia"));
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_other_is_null() {
    // GIVEN
    Iterable<?> other = null;
    // WHEN
    ThrowingCallable code = () -> arrays.assertHasSameSizeAs(someInfo(), actual, other);
    // THEN
    assertThatNullPointerException().isThrownBy(code)
                                    .withMessage("The Iterable to compare actual size with should not be null");
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size() {
    // GIVEN
    AssertionInfo info = someInfo();
    List<String> other = list("Solo", "Leia");
    // WHEN
    ThrowingCallable code = () -> arrays.assertHasSameSizeAs(info, actual, other);
    // THEN
    String error = shouldHaveSameSizeAs(actual, other, actual.length, other.size()).create(null, info.representation());
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }

  @Test
  public void should_pass_if_actual_has_same_size_as_other() {
    arrays.assertHasSameSizeAs(someInfo(), array("Solo", "Leia"), list("Solo", "Leia"));
  }
}
