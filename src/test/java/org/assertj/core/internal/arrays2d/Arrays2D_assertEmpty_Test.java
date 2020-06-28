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
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Arrays2D;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Arrays2D#assertEmpty(AssertionInfo, Failures, Object)}</code>.
 *
 * @author Maciej Wajcht
 */
public class Arrays2D_assertEmpty_Test extends Arrays2D_BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    int[][] actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertEmpty(someInfo(), failures, actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  public void should_fail_if_actual_is_not_empty() {
    // GIVEN
    int[][] actual = { { 4 }, { 6, 8 } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertEmpty(someInfo(), failures, actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEmpty(actual).create());

  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertEmpty(someInfo(), failures, new int[][] {});
    arrays.assertEmpty(someInfo(), failures, new int[][] { {} });
    arrays.assertEmpty(someInfo(), failures, new int[][] { {}, {}, {} });
  }
}
