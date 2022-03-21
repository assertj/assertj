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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotContainNull.shouldNotContainNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertDoesNotContainNull(AssertionInfo, Collection)}</code>.
 * 
 * @author Joel Costigliola
 */
class Iterables_assertDoesNotContainNull_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Yoda");

  @Test
  void should_pass_if_actual_does_not_contain_null() {
    iterables.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    actual = newArrayList();
    iterables.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertDoesNotContainNull(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_null() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda", null);

    Throwable error = catchThrowable(() -> iterables.assertDoesNotContainNull(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainNull(actual));
  }

  @Test
  void should_pass_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    actual = newArrayList();
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(someInfo(),
                                                                                                                                             null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda", null);

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainNull(actual));
  }
}
