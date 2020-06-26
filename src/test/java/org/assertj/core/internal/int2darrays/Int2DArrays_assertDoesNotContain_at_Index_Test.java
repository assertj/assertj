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
package org.assertj.core.internal.int2darrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.test.TestData.someIndex;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Int2DArrays;
import org.assertj.core.internal.Int2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Int2DArrays#assertDoesNotContain(AssertionInfo, int[][], int[], Index)}</code>.
 *
 * @author Maciej Wajcht
 */
public class Int2DArrays_assertDoesNotContain_at_Index_Test extends Int2DArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    int[][] actual = null;
    int[] expectedElement = { 0, 2, 4 };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertDoesNotContain(someInfo(), actual, expectedElement,
                                                                                           someIndex()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_Index() {
    arrays.assertDoesNotContain(someInfo(), actual, new int[] { 0, 2, 4 }, atIndex(1));
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotContain(someInfo(), new int[][] {}, new int[] { 0, 2, 4 }, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    // GIVEN
    Index nullIndex = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContain(someInfo(), actual, new int[] { 0, 2, 4 },
                                                                                  nullIndex))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_pass_if_Index_is_out_of_bounds() {
    arrays.assertDoesNotContain(someInfo(), actual, new int[] { 0, 2, 4 }, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index() {
    // GIVEN
    Index index = atIndex(0);
    int[] expectedElement = { 0, 2, 4 };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertDoesNotContain(someInfo(), actual, expectedElement,
                                                                                           index));
    // THEN
    then(assertionError).hasMessage(shouldNotContainAtIndex(actual, new int[] { 0, 2, 4 }, index).create());
  }
}
