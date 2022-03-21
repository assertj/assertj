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
package org.assertj.core.internal.lists;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.test.TestData.someIndex;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Lists#assertDoesNotContain(AssertionInfo, List, Object, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Lists_assertDoesNotContain_Test extends ListsBaseTest {

  private static List<String> actual = newArrayList("Yoda", "Luke", "Leia");

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> lists.assertDoesNotContain(someInfo(), null, "Yoda",
                                                                                                someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_does_not_contain_value_at_index() {
    lists.assertDoesNotContain(someInfo(), actual, "Yoda", atIndex(1));
  }

  @Test
  void should_pass_if_actual_is_empty() {
    lists.assertDoesNotContain(someInfo(), emptyList(), "Yoda", someIndex());
  }

  @Test
  void should_throw_error_if_index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> lists.assertDoesNotContain(someInfo(), actual, "Yoda", null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_pass_if_index_is_out_of_bounds() {
    lists.assertDoesNotContain(someInfo(), actual, "Yoda", atIndex(6));
  }

  @Test
  void should_fail_if_actual_contains_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);

    Throwable error = catchThrowable(() -> lists.assertDoesNotContain(info, actual, "Yoda", index));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainAtIndex(actual, "Yoda", index));
  }

  @Test
  void should_pass_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    listsWithCaseInsensitiveComparisonStrategy.assertDoesNotContain(someInfo(), actual, "Yoda", atIndex(1));
  }

  @Test
  void should_fail_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);

    Throwable error = catchThrowable(() -> listsWithCaseInsensitiveComparisonStrategy.assertDoesNotContain(info, actual, "YODA",
                                                                                                           index));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainAtIndex(actual, "YODA", index, comparisonStrategy));
  }
}
