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
package org.assertj.core.internal.iterables;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collection;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertHasSameSizeAs(AssertionInfo, Iterable, Object[])}</code>.
 * 
 * @author Nicolas François
 */
class Iterables_assertHasSameSizeAs_with_Array_Test extends IterablesBaseTest {

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    iterables.assertHasSameSizeAs(someInfo(), newArrayList("Yoda", "Luke"), array("Solo", "Leia"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertHasSameSizeAs(someInfo(), null,
                                                                                                   newArrayList("Solo", "Leia")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Iterable<?> other = null;
      iterables.assertHasSameSizeAs(someInfo(), newArrayList("Yoda", "Luke"), other);
    }).withMessage("The Iterable to compare actual size with should not be null");
  }

  @Test
  void should_fail_if_actual_size_is_not_equal_to_other_size() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    String[] other = array("Solo", "Luke", "Leia");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertHasSameSizeAs(info, actual, other))
                                                   .withMessage(format(shouldHaveSameSizeAs(actual, other, actual.size(),
                                                                                            other.length).create(null,
                                                                                                                 info.representation())));
  }

  @Test
  void should_pass_if_actual_has_same_size_as_other_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(), newArrayList("Luke", "Yoda"),
                                                                       array("Solo", "Leia"));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(),
                                                                                                                                        null,
                                                                                                                                        array("Solo",
                                                                                                                                              "Leia")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      Iterable<?> other = null;
      iterables.assertHasSameSizeAs(someInfo(), newArrayList("Yoda", "Luke"), other);
    }).withMessage("The Iterable to compare actual size with should not be null");
  }

  @Test
  void should_fail_if_actual_size_is_not_equal_to_other_size_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    String[] other = array("Solo", "Luke", "Leia");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(info,
                                                                                                                                        actual,
                                                                                                                                        other))
                                                   .withMessage(shouldHaveSameSizeAs(actual, other, actual.size(),
                                                                                     other.length)
                                                                                                  .create(null,
                                                                                                          info.representation()));
  }
}
