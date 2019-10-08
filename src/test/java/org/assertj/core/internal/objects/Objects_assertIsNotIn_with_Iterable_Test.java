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

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldNotBeIn.shouldNotBeIn;
import static org.assertj.core.internal.ErrorMessages.iterableIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertIsNotIn(AssertionInfo, Object, Iterable)}</code>.
 *
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Nicolas François
 */
public class Objects_assertIsNotIn_with_Iterable_Test extends ObjectsBaseTest {

  private Iterable<String> values;

  @BeforeEach
  public void setUpOnce() {
    values = newArrayList("Yoda", "Leia");
  }

  @Test
  public void should_throw_error_if_Iterable_is_null() {
    // GIVEN
    Iterable<String> iterable = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsNotIn(someInfo(), "Luke", iterable))
                                    .withMessage(iterableIsNull());
  }

  @Test
  public void should_pass_if_actual_is_not_in_Iterable() {
    objects.assertIsNotIn(someInfo(), "Luke", values);
  }

  @Test
  public void should_pass_if_given_Iterable_is_empty() {
    objects.assertIsNotIn(someInfo(), "Luke", emptySet());
  }

  @Test
  public void should_pass_if_actual_is_null_and_array_does_not_contain_null() {
    objects.assertIsNotIn(someInfo(), null, values);
  }

  @Test
  public void should_fail_if_actual_is_in_Iterable() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objects.assertIsNotIn(info, "Yoda", values));
    // THEN
    verify(failures).failure(info, shouldNotBeIn("Yoda", values));
  }

  @Test
  public void should_pass_if_actual_is_not_in_Iterable_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertIsNotIn(someInfo(), "Luke", values);
  }

  @Test
  public void should_fail_if_actual_is_in_Iterable_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objectsWithCustomComparisonStrategy.assertIsNotIn(info, "YODA", values));
    // THEN
    verify(failures).failure(info, shouldNotBeIn("YODA", values, customComparisonStrategy));
  }

}
