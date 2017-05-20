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
package org.assertj.core.internal.doubles;

import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.ErrorMessages.offsetIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Doubles#assertEqual(AssertionInfo, Double, Double, Offset)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Doubles_assertEqual_with_offset_Test extends DoublesBaseTest {

  @Test
  public void should_throw_error_if_offset_is_null() {
    thrown.expectNullPointerException(offsetIsNull());
    doubles.assertEqual(someInfo(), new Double(8d), new Double(8d), null);
  }

  @Test
  public void should_pass_if_doubles_are_equal() {
    doubles.assertEqual(someInfo(), new Double(8d), new Double(8d), offset(1d));
  }

  @Test
  public void should_pass_if_doubles_are_equal_within_offset() {
    doubles.assertEqual(someInfo(), new Double(6d), new Double(8d), offset(2d));
  }

  @Test
  public void should_fail_if_doubles_are_not_equal_within_offset() {
    AssertionInfo info = someInfo();
    Offset<Double> offset = offset(1d);
    try {
      doubles.assertEqual(info, new Double(6d), new Double(8d), offset);
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

    thrown.expectNullPointerException("The given number should not be null");

    doubles.assertEqual(info, 6d, null, offset);
  }

  @Test
  public void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(offsetIsNull());
    doublesWithAbsValueComparisonStrategy.assertEqual(someInfo(), new Double(8d), new Double(8d), null);
  }

  @Test
  public void should_pass_if_doubles_are_equal_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertEqual(someInfo(), new Double(8d), new Double(8d), offset(1d));
  }

  @Test
  public void should_pass_if_doubles_are_equal_within_offset_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertEqual(someInfo(), new Double(6d), new Double(8d), offset(2d));
  }

  @Test
  public void should_fail_if_doubles_are_not_equal_within_offset_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Offset<Double> offset = offset(1d);
    try {
      doublesWithAbsValueComparisonStrategy.assertEqual(info, new Double(6d), new Double(8d), offset);
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

    thrown.expectNullPointerException("The given number should not be null");

    doublesWithAbsValueComparisonStrategy.assertEqual(info, 6d, null, offset);
  }
}
