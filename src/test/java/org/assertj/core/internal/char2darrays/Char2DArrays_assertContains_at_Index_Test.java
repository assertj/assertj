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
package org.assertj.core.internal.char2darrays;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someIndex;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Char2DArrays;
import org.assertj.core.internal.Char2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Char2DArrays#assertContains(AssertionInfo, char[][], char[], Index)}</code>.
 *
 * @author Maciej Wajcht
 */
public class Char2DArrays_assertContains_at_Index_Test extends Char2DArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    char[][] actual = null;
    char[] expectedElement = { 'a', 'b', 'c' };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertContains(someInfo(), actual, expectedElement,
                                                                                     someIndex()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    // GIVEN
    char[][] actual = {};
    char[] expectedElement = { 'a', 'b', 'c' };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertContains(someInfo(), actual, expectedElement,
                                                                                     someIndex()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    // GIVEN
    Index nullIndex = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContains(someInfo(), actual, new char[] { 'a', 'b', 'c' },
                                                                            nullIndex))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds() {
    // GIVEN
    Index outOfBoundsIndex = atIndex(6);
    // WHEN/THEN
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arrays.assertContains(someInfo(), actual,
                                                                                                      new char[] { 'a', 'b',
                                                                                                          'c' },
                                                                                                      outOfBoundsIndex))
                                                              .withMessageContaining(format("Index should be between <0> and <1> (inclusive) but was:%n <6>"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index() {
    // GIVEN
    Index index = atIndex(1);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertContains(someInfo(), actual,
                                                                                     new char[] { 'a', 'b', 'c' },
                                                                                     index));
    // THEN
    then(assertionError).hasMessage(shouldContainAtIndex(actual, new char[] { 'a', 'b', 'c' }, index,
                                                         new char[] { 'd', 'e', 'f' }).create());
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index() {
    arrays.assertContains(someInfo(), actual, new char[] { 'd', 'e', 'f' }, atIndex(1));
  }
}
