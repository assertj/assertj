/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.error.AssertionErrorCreator;
import org.opentest4j.TestAbortedException;

public class AssertionsUtil {

  public static AssertionError expectAssertionError(ThrowingCallable shouldRaiseAssertionError) {
    AssertionError error = catchThrowableOfType(AssertionError.class, shouldRaiseAssertionError);
    assertThat(error).as("The code under test should have raised an AssertionError").isNotNull();
    return error;
  }

  public static ThrowableAssertAlternative<AssertionError> assertThatAssertionErrorIsThrownBy(ThrowingCallable shouldRaiseAssertionError) {
    return assertThatExceptionOfType(AssertionError.class).isThrownBy(shouldRaiseAssertionError);
  }

  public static void expectAssumptionNotMetException(ThrowingCallable shouldRaiseError) {
    assertThatThrownBy(shouldRaiseError).isInstanceOf(TestAbortedException.class);
  }

  /// Returns the message of the [AssertionError] reported when `actual` is not equal to `expected`.
  ///
  /// @param info the assertion information, its representation is used to format the values
  /// @param actual the actual value
  /// @param expected the expected value
  /// @return the expected error message
  public static String shouldBeEqualMessage(AssertionInfo info, Object actual, Object expected) {
    return shouldBeEqualMessage(info, actual, expected, StandardComparisonStrategy.instance());
  }

  /// Returns the message of the [AssertionError] reported when `actual` is not equal to `expected` according to the
  /// given comparison strategy.
  ///
  /// @param info the assertion information, its representation is used to format the values
  /// @param actual the actual value
  /// @param expected the expected value
  /// @param comparisonStrategy the comparison strategy used to compare the values
  /// @return the expected error message
  public static String shouldBeEqualMessage(AssertionInfo info, Object actual, Object expected,
                                            ComparisonStrategy comparisonStrategy) {
    var shouldBeEqual = shouldBeEqual(actual, expected, comparisonStrategy, info.representation());
    return AssertionErrorCreator.instance().assertionError(info, shouldBeEqual).getMessage();
  }

}
