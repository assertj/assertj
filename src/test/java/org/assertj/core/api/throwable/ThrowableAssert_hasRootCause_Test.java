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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssertBaseTest;
import org.junit.jupiter.api.Test;

public class ThrowableAssert_hasRootCause_Test extends ThrowableAssertBaseTest {

  private Throwable npe = new NullPointerException();

  @Override
  protected ThrowableAssert invoke_api_method() {
    return assertions.hasRootCause(npe);
  }

  @Override
  protected void verify_internal_effects() {
    verify(throwables).assertHasRootCause(getInfo(assertions), getActual(assertions), npe);
  }

  @Test
  public void should_fail_if_actual_and_expected_root_cause_have_different_types() {
    // GIVEN
    Throwable rootCause = new IllegalStateException();
    Throwable cause = new RuntimeException(rootCause);
    final Throwable throwable = new IllegalArgumentException(cause);

    // WHEN
    AssertionError actual = expectAssertionError(() -> assertThat(throwable).hasRootCause(new NullPointerException()));

    // THEN
    assertThat(actual)
      .hasMessage(format("%n"
                         + "Expecting a root cause with type:%n"
                         + "  <\"java.lang.NullPointerException\">%n"
                         + "but type was:%n"
                         + "  <\"java.lang.IllegalStateException\">."));
  }
}
