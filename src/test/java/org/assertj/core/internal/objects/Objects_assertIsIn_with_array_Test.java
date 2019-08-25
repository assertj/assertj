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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.internal.ErrorMessages.arrayIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertIsIn(AssertionInfo, Object, Object[])}</code>.
 *
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Objects_assertIsIn_with_array_Test extends ObjectsBaseTest {

  private static String[] values;

  @BeforeAll
  public static void setUpOnce() {
    values = array("Yoda", "Leia");
  }

  @Test
  public void should_throw_error_if_array_is_null() {
    Object[] array = null;
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsIn(someInfo(), "Yoda", array))
                                    .withMessage(arrayIsNull());
  }

  @Test
  public void should_pass_if_actual_is_in_array() {
    objects.assertIsIn(someInfo(), "Yoda", values);
  }

  @Test
  public void should_pass_if_actual_is_null_and_array_contains_null() {
    objects.assertIsIn(someInfo(), null, array("Yoda", null));
  }

  @Test
  public void should_fail_if_actual_is_not_in_array() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objects.assertIsIn(info, "Luke", values));
    // THEN
    verify(failures).failure(info, shouldBeIn("Luke", asList(values)));
  }

  @Test
  public void should_fail_if_given_array_is_empty() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objects.assertIsIn(info, "Luke", emptyArray()));
    // THEN
    verify(failures).failure(info, shouldBeIn("Luke", emptyList()));
  }

  @Test
  public void should_pass_if_actual_is_in_array_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertIsIn(someInfo(), "YODA", values);
  }

  @Test
  public void should_fail_if_actual_is_not_in_array_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objectsWithCustomComparisonStrategy.assertIsIn(info, "Luke", values));
    // THEN
    verify(failures).failure(info, shouldBeIn("Luke", asList(values), customComparisonStrategy));
  }
}
