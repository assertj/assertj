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
package org.assertj.core.internal.floats;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInfinite.shouldBeInfinite;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.FloatsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Floats_assertIsInfinite_Test extends FloatsBaseTest {

  @ParameterizedTest
  @ValueSource(floats = {
      Float.POSITIVE_INFINITY,
      Float.NEGATIVE_INFINITY
  })
  void should_succeed_when_actual_is_infinite(float actual) {
    floats.assertIsInfinite(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(floats = {
      Float.MAX_VALUE,
      Float.MIN_NORMAL,
      Float.MIN_VALUE,
      Float.NaN,
      0.0f,
      1.0f,
      -1.0f,
  })
  void should_fail_when_actual_is_not_infinite(float actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsInfinite(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeInfinite(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Float actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsInfinite(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
