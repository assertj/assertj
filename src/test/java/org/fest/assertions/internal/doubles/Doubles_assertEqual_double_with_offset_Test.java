/*
 * Created on Oct 28, 2010
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
package org.fest.assertions.internal.doubles;

import static org.fest.assertions.api.Assertions.*;
import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.fest.assertions.test.ErrorMessages.offsetIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Offset;
import org.fest.assertions.internal.Doubles;
import org.fest.assertions.internal.DoublesBaseTest;

/**
 * Tests for <code>{@link Doubles#assertEqual(AssertionInfo, Double, double, Offset)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Doubles_assertEqual_double_with_offset_Test extends DoublesBaseTest {

  @Test
  public void should_throw_error_if_offset_is_null() {
    thrown.expectNullPointerException(offsetIsNull());
    doubles.assertEqual(someInfo(), new Double(8d), 8d, null);
  }

  @Test
  public void should_pass_if_doubles_are_equal() {
    doubles.assertEqual(someInfo(), new Double(8d), 8d, offset(1d));
  }

  @Test
  public void should_pass_if_doubles_are_equal_within_offset() {
    doubles.assertEqual(someInfo(), new Double(6d), 8d, offset(2d));
  }

  @Test
  public void should_fail_if_doubles_are_not_equal_within_offset() {
    AssertionInfo info = someInfo();
    Offset<Double> offset = offset(1d);
    try {
      doubles.assertEqual(info, new Double(6d), 8d, offset);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(6d, 8d, offset, 2d));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_second_double_is_null_but_not_the_first() {
    AssertionInfo info = someInfo();
    Offset<Double> offset = offset(1d);
    try {
      doubles.assertEqual(info, 6d, null, offset);
      failBecauseExceptionWasNotThrown(NullPointerException.class);
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("The given number should not be null");
    }
  }

  @Test
  public void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(offsetIsNull());
    doublesWithAbsValueComparisonStrategy.assertEqual(someInfo(), new Double(8d), 8d, null);
  }

  @Test
  public void should_pass_if_doubles_are_equal_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertEqual(someInfo(), new Double(8d), 8d, offset(1d));
  }

  @Test
  public void should_pass_if_doubles_are_equal_within_offset_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertEqual(someInfo(), new Double(6d), 8d, offset(2d));
  }

  @Test
  public void should_fail_if_doubles_are_not_equal_within_offset_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Offset<Double> offset = offset(1d);
    try {
      doublesWithAbsValueComparisonStrategy.assertEqual(info, new Double(6d), 8d, offset);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(6d, 8d, offset, 2d));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_second_double_is_null_but_not_the_first_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Offset<Double> offset = offset(1d);
    try {
      doublesWithAbsValueComparisonStrategy.assertEqual(info, 6d, null, offset);
      failBecauseExceptionWasNotThrown(NullPointerException.class);
    } catch (NullPointerException e) {
      assertThat(e).hasMessage("The given number should not be null");
    }
  }
}
