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
package org.assertj.core.internal.throwables;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

public class Throwables_assertHasNoSuppressedExceptions_Test extends ThrowablesBaseTest {

  @Test
  public void should_pass_if_throwable_has_no_suppressed_exceptions() {
    throwables.assertHasNoSuppressedExceptions(someInfo(), new Throwable());
  }

  @Test
  public void should_fail_if_throwable_has_suppressed_exceptions() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Throwable actual = new Throwable();
      actual.addSuppressed(new IllegalArgumentException("Suppressed Message"));
      throwables.assertHasNoSuppressedExceptions(someInfo(), actual);
      throwables.assertHasNoSuppressedExceptions(someInfo(), actual);
    }).withMessage(format("%nExpecting no suppressed exceptions but found: <[java.lang.IllegalArgumentException: Suppressed Message]>"));
  }
}
