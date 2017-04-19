/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static java.util.Collections.emptyList;
import static org.assertj.core.error.ShouldBeIn.shouldBeIn;
import static org.assertj.core.internal.ErrorMessages.iterableIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ErrorMessages;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link Objects#assertIsIn(AssertionInfo, Object, Iterable)}</code>.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class Objects_assertIsIn_with_Iterable_Test extends ObjectsBaseTest {

  private static Iterable<String> values;

  @BeforeClass
  public static void setUpOnce() {
    values = newArrayList("Yoda", "Leia");
  }

  @Test
  public void should_throw_error_if_Iterable_is_null() {
    thrown.expectNullPointerException(iterableIsNull());
    Iterable<String> c = null;
    objects.assertIsIn(someInfo(), "Yoda", c);
  }

  @Test
  public void should_throw_error_if_Iterable_is_empty() {
    thrown.expectIllegalArgumentException(ErrorMessages.iterableIsEmpty());
    objects.assertIsIn(someInfo(), "Yoda", emptyList());
  }

  @Test
  public void should_pass_if_actual_is_in_Iterable() {
    objects.assertIsIn(someInfo(), "Yoda", values);
  }

  @Test
  public void should_pass_if_actual_is_null_and_array_contains_null() {
    objects.assertIsIn(someInfo(), null, newArrayList("Yoda", null));
  }

  @Test
  public void should_fail_if_actual_is_not_in_Iterable() {
    AssertionInfo info = someInfo();
    try {
      objects.assertIsIn(info, "Luke", values);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeIn("Luke", values));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_is_in_Iterable_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertIsIn(someInfo(), "YODA", values);
  }

  @Test
  public void should_fail_if_actual_is_not_in_Iterable_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      objectsWithCustomComparisonStrategy.assertIsIn(info, "Luke", values);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeIn("Luke", values, customComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
