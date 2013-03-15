/*
 * Created on Jan 2, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assert4j.core.assertions.internal.objects;

import static org.assert4j.core.assertions.error.ShouldBeIn.shouldBeIn;
import static org.assert4j.core.assertions.test.ErrorMessages.*;
import static org.assert4j.core.assertions.test.ObjectArrays.emptyArray;
import static org.assert4j.core.assertions.test.TestData.someInfo;
import static org.assert4j.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assert4j.core.util.Arrays.array;
import static org.assert4j.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assert4j.core.assertions.core.AssertionInfo;
import org.assert4j.core.assertions.internal.Objects;
import org.assert4j.core.assertions.internal.ObjectsBaseTest;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for <code>{@link Objects#assertIsIn(AssertionInfo, Object, Object[])}</code>.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Objects_assertIsIn_with_array_Test extends ObjectsBaseTest {

  private static String[] values;

  @BeforeClass
  public static void setUpOnce() {
    values = array("Yoda", "Leia");
  }

  @Test
  public void should_throw_error_if_array_is_null() {
    thrown.expectNullPointerException(arrayIsNull());
    Object[] array = null;
    objects.assertIsIn(someInfo(), "Yoda", array);
  }

  @Test
  public void should_throw_error_if_array_is_empty() {
    thrown.expectIllegalArgumentException(arrayIsEmpty());
    objects.assertIsIn(someInfo(), "Yoda", emptyArray());
  }

  @Test
  public void should_pass_if_actual_is_in_array() {
    objects.assertIsIn(someInfo(), "Yoda", values);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertIsIn(someInfo(), null, values);
  }

  @Test
  public void should_fail_if_actual_is_not_in_array() {
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
  public void should_pass_if_actual_is_in_array_according_to_custom_comparison_strategy() {
    objectsWithCustomComparisonStrategy.assertIsIn(someInfo(), "YODA", values);
  }

  @Test
  public void should_fail_if_actual_is_not_in_array_according_to_custom_comparison_strategy() {
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
