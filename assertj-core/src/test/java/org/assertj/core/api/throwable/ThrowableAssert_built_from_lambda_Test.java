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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class ThrowableAssert_built_from_lambda_Test {

  @Test
  void should_build_ThrowableAssert_with_runtime_exception_thrown_by_lambda() {
    assertThatThrownBy(() -> {
      throw new IllegalArgumentException("something was wrong");
    }).isInstanceOf(IllegalArgumentException.class)
      .hasMessage("something was wrong");
  }

  @Test
  void should_build_ThrowableAssert_with_checked_exception_thrown_by_lambda() {
    Jedi yoda = new Jedi("Yoda", "Green");
    // @format:off
    assertThatThrownBy(() -> { throw new Exception(yoda + " is no Sith"); })
      .isInstanceOf(Exception.class)
      .hasMessage(yoda + " is no Sith");
    // @format:on
  }

  @Test
  void should_fail_if_nothing_is_thrown_by_lambda() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatThrownBy(() -> {}))
                                                   .withMessage(format("%nExpecting code to raise a throwable."));
  }

}
