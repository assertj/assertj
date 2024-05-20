/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objects;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.internal.ErrorMessages.iterableIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertIsIn(AssertionInfo, Object, Iterable)}</code>.
 *
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
class Objects_assertIsIn_with_Iterable_Test extends ObjectsBaseTest {

  private static Iterable<String> values;

  @BeforeAll
  static void setUpOnce() {
    values = list("Yoda", "Leia");
  }

  @Test
  void should_throw_NullPointerException_if_Iterable_is_null() {
    // GIVEN
    Iterable<String> nullIterable = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsIn(someInfo(), "Yoda", nullIterable))
                                    .withMessage(iterableIsNull());
  }

  @Test
  void should_fail_if_given_Iterable_is_empty() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objects.assertIsIn(info, "Luke", emptyList()));
    // THEN
    verify(failures).failure(info, shouldBeIn("Luke", emptyList()));
  }

  @Test
  void should_pass_if_actual_is_in_given_Iterable() {
    objects.assertIsIn(someInfo(), "Yoda", values);
  }

  @Test
  void should_pass_if_actual_is_null_and_array_contains_null() {
    objects.assertIsIn(someInfo(), null, list("Yoda", null));
  }

  @Test
  void should_fail_if_actual_is_not_in_Iterable() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objects.assertIsIn(info, "Luke", values));
    // THEN
    verify(failures).failure(info, shouldBeIn("Luke", values));
  }

  @Test
  void should_pass_if_actual_is_in_Iterable_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertIsIn(someInfo(), "YODA", values);
  }

  @Test
  void should_fail_if_actual_is_not_in_Iterable_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objectsWithCustomComparisonStrategy.assertIsIn(info, "Luke", values));
    // THEN
    verify(failures).failure(info, shouldBeIn("Luke", values, customComparisonStrategy));
  }
}
