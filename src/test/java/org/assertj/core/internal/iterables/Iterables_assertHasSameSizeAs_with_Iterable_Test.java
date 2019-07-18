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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collection;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertHasSameSizeAs(AssertionInfo, Iterable, Iterable)}</code>.
 *
 * @author Nicolas François
 */
public class Iterables_assertHasSameSizeAs_with_Iterable_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    iterables.assertHasSameSizeAs(someInfo(), list("Yoda", "Luke"), list("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    ThrowingCallable code = () -> iterables.assertHasSameSizeAs(someInfo(), actual, list("Solo", "Leia"));
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_other_is_null() {
    // GIVEN
    Iterable<?> other = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> iterables.assertHasSameSizeAs(someInfo(), list("Yoda", "Luke"), other))
                                    .withMessage("The Iterable to compare actual size with should not be null");
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size() {
    // GIVEN
    AssertionInfo info = someInfo();
    Collection<String> actual = list("Yoda");
    Collection<String> other = list("Solo", "Luke", "Leia");
    // WHEN
    ThrowingCallable code = () -> iterables.assertHasSameSizeAs(info, actual, other);
    // THEN
    String error = shouldHaveSameSizeAs(actual, other, actual.size(), other.size()).create(null, info.representation());
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }

  @Test
  public void should_pass_if_actual_has_same_size_as_other_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(), newArrayList("Luke", "Yoda"),
                                                                       newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    actual = null;
    // WHEN
    ThrowingCallable code = () -> iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(), actual,
                                                                                                     list("Solo", "Leia"));
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_other_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Iterable<?> other = null;
    // WHEN
    ThrowingCallable code = () -> iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(),
                                                                                                     list("Yoda", "Luke"), other);
    // THEN
    assertThatNullPointerException().isThrownBy(code)
                                    .withMessage("The Iterable to compare actual size with should not be null");
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size_whatever_custom_comparison_strategy_is() {
    // GIVEN
    AssertionInfo info = someInfo();
    Collection<String> actual = list("Yoda");
    Collection<String> other = list("Solo", "Luke", "Leia");
    // WHEN
    ThrowingCallable code = () -> iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(info, actual, other);
    // THEN
    String error = shouldHaveSameSizeAs(actual, other, actual.size(), other.size()).create(null, info.representation());
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }
}
