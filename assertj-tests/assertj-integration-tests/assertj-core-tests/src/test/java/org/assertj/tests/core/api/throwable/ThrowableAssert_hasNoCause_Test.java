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
package org.assertj.tests.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/// @author Joel Costigliola
class ThrowableAssert_hasNoCause_Test {

  @Test
  void should_pass_if_actual_has_no_cause() {
    assertThat(new NullPointerException("Throwable message")).hasNoCause();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoCause());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_has_a_cause() {
    // GIVEN
    Throwable actual = new Throwable(new NullPointerException());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoCause());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoCause(actual).create());
  }

}
