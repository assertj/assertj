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
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Int2DArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Int2DArrays#assertNullOrEmpty(AssertionInfo, int[][])}</code>.
 *
 * @author Maciej Wajcht
 */
public class Arrays2D_assertNullOrEmpty_Test extends Arrays2D_BaseTest {

  @Test
  public void should_fail_if_array_is_not_null_and_is_not_empty() {
    // GIVEN
    int[][] actual = { { 1, 2 }, { 6, 8 } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertNullOrEmpty(someInfo(), failures, actual));
    // THEN
    then(assertionError).hasMessage(shouldBeNullOrEmpty(actual).create());
  }

  @Test
  public void should_pass_if_array_is_null() {
    arrays.assertNullOrEmpty(someInfo(), failures, null);
  }

  @Test
  public void should_pass_if_array_is_empty() {
    arrays.assertNullOrEmpty(someInfo(), failures, new int[][] {});
    arrays.assertNullOrEmpty(someInfo(), failures, new int[][] { {} });
    arrays.assertNullOrEmpty(someInfo(), failures, new int[][] { {}, {}, {} });
  }
}
