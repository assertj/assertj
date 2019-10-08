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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.AssumptionViolatedException;

public class AssertionsUtil {

  public static AssertionError expectAssertionError(ThrowingCallable shouldRaiseAssertionError) {
    AssertionError error = catchThrowableOfType(shouldRaiseAssertionError, AssertionError.class);
    assertThat(error).as("The code under test should have raised an AssertionError").isNotNull();
    return error;
  }

  public static ThrowableAssertAlternative<AssertionError> assertThatAssertionErrorIsThrownBy(ThrowingCallable shouldRaiseAssertionError) {
    return assertThatExceptionOfType(AssertionError.class).isThrownBy(shouldRaiseAssertionError);
  }

  public static void expectAssumptionViolatedException(ThrowingCallable shouldRaiseError) {
    assertThatThrownBy(shouldRaiseError).isInstanceOf(AssumptionViolatedException.class);
  }
}
