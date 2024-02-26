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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.throwable;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ThrowableTypeAssert_isNotThrownBy_Test {

  @Test
  void should_not_fail_if_nothing_is_thrown_by_callable_code() {
    assertThatExceptionOfType(Throwable.class).isNotThrownBy(() -> {});
  }

  @Test
  void should_not_fail_if_expected_exception_is_not_thrown_by_callable_code() {
    assertThatExceptionOfType(IllegalArgumentException.class).isNotThrownBy(() -> {
      throw new IllegalStateException();
    });
  }

  @Test
  void should_fail_if_expected_exception_is_thrown_by_callable_code() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      assertThatExceptionOfType(IllegalArgumentException.class).isNotThrownBy(() -> {
        throw new IllegalArgumentException();
      });
    }).withMessageContaining("not to be an instance of: java.lang.IllegalArgumentException");
  }
}
