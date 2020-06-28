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
package org.assertj.core.internal.arrays2d;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Int2DArrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link Int2DArrays#assertNotEmpty(AssertionInfo, int[][])}</code>.
 *
 * @author Maciej Wajcht
 */
public class Arrays2D_assertNotEmpty_Test extends Arrays2D_BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    int[][] actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertNotEmpty(someInfo(), failures, actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource("should_fail_if_actual_is_empty_parameters")
  public void should_fail_if_actual_is_empty(Object[][] actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertNotEmpty(someInfo(), failures, actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  static Stream<String[][]> should_fail_if_actual_is_empty_parameters() {
    // some arrays have multiple rows but no elements in them
    return Stream.of(new String[][] {},
                     new String[][] { {} },
                     new String[][] { {} },
                     new String[][] { {}, {}, {} });
  }

  @Test
  public void should_pass_if_actual_is_not_empty() {
    arrays.assertNotEmpty(someInfo(), failures, new int[][] { { 1 }, { 2 } });
    arrays.assertNotEmpty(someInfo(), failures, new int[][] { { 1 }, {} });
    arrays.assertNotEmpty(someInfo(), failures, new int[][] { {}, { 2 } });
  }
}
