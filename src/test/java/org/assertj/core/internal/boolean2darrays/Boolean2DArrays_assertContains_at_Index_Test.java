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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.boolean2darrays;

import static java.lang.String.format;
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
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Boolean2DArrays;
import org.assertj.core.internal.Boolean2DArraysBaseTest;
import org.junit.jupiter.api.Test;


/**
* Tests for <code>{@link Boolean2DArrays#assertContains(AssertionInfo, boolean[][], boolean[], Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Maciej Wajcht
 */
public class Boolean2DArrays_assertContains_at_Index_Test extends Boolean2DArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), null, new boolean[] {true, false, false}, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), new boolean[][] {}, new boolean[] {true, false, false}, someIndex()))
                                                   .withMessage(actualIsEmpty());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContains(someInfo(), actual, new boolean[] {true, false, false}, null))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arrays.assertContains(someInfo(),
                                                                                                      actual, new boolean[] {true, false, false},
                                                                                                      atIndex(6)))
                                                              .withMessageContaining(format("Index should be between <0> and <1> (inclusive) but was:%n <6>"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);

    Throwable error = catchThrowable(() -> arrays.assertContains(info, actual, new boolean[] {true, false, false}, index));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainAtIndex(actual, new boolean[] {true, false, false}, index, new boolean[] {true, false, true}));
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index() {
    arrays.assertContains(someInfo(), actual, new boolean[] {true, false, true}, atIndex(1));
  }
}
