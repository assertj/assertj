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

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.test.TestData.someIndex;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsEmpty;
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
 * Tests for <code>{@link Lists#assertContains(AssertionInfo, List, Object, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Lists_assertContains_Test extends ListsBaseTest {

  private static List<String> actual = newArrayList("Yoda", "Luke", "Leia");

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> lists.assertContains(someInfo(), null, "Yoda", someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> lists.assertContains(someInfo(), emptyList(), "Yoda",
                                                                                          someIndex()))
                                                   .withMessage(actualIsEmpty());
  }

  @Test
  void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> lists.assertContains(someInfo(), actual, "Yoda", null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_throw_error_if_Index_is_out_of_bounds() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> lists.assertContains(someInfo(), actual,
                                                                                                     "Yoda",
                                                                                                     atIndex(6)))
                                                              .withMessageContaining(format("Index should be between <0> and <2> (inclusive) but was:%n <6>"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);

    Throwable error = catchThrowable(() -> lists.assertContains(info, actual, "Han", index));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainAtIndex(actual, "Han", index, "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_value_at_index() {
    lists.assertContains(someInfo(), actual, "Luke", atIndex(1));
  }

  @Test
  void should_pass_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    listsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, "Luke", atIndex(1));
    listsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, "luke", atIndex(1));
    listsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, "LUKE", atIndex(1));
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);

    Throwable error = catchThrowable(() -> listsWithCaseInsensitiveComparisonStrategy.assertContains(info, actual, "Han", index));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainAtIndex(actual, "Han", index, "Luke", comparisonStrategy));
  }

}
