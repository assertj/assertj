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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.atomic.longadder;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LongAdderAssert_overridingErrorMessage_Test {

  @Test
  public void should_honor_custom_error_message_set_with_withFailMessage() {
    String error = "ssss";
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThat(new LongAdder()).withFailMessage(error)
        .isLessThan(-1L))
      .withMessageContaining(error);
  }

  @Test
  public void should_honor_custom_error_message_set_with_overridingErrorMessage() {
    String error = "ssss";
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThat(new LongAdder()).overridingErrorMessage(error)
        .isLessThan(-1L))
      .withMessageContaining(error);
  }
}
