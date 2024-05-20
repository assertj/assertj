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
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Throwables.getStackTrace;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;
import org.junit.jupiter.api.Test;

class ThrowableAssert_hasCause_Test extends ThrowableAssertBaseTest {

  private final Throwable npe = new NullPointerException();

  @Override
  protected ThrowableAssert<Throwable> invoke_api_method() {
    return assertions.hasCause(npe);
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasCause(getInfo(assertions), getActual(assertions), npe);
  }

  @Test
  void should_fail_if_actual_and_expected_cause_have_different_types() {
    // GIVEN
    Throwable actual = new IllegalArgumentException(new IllegalStateException());
    // WHEN/THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasCause(new NullPointerException()))
                                                   .withMessage(format("%n" +
                                                                       "Expecting a cause with type:%n" +
                                                                       "  \"java.lang.NullPointerException\"%n" +
                                                                       "but type was:%n" +
                                                                       "  \"java.lang.IllegalStateException\".%n%n" +
                                                                       "Throwable that failed the check:%n"
                                                                       + escapePercent(getStackTrace(actual))));
  }
}
