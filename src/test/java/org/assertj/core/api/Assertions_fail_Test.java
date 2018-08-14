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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class Assertions_fail_Test {

  @Test
  public void should_include_message_when_failing() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions.fail("Failed :("))
                                                   .withMessage("Failed :(");
  }
  
  @Test
  public void should_include_message_with_parameters_when_failing() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions.fail("Failed %s", ":("))
                                                   .withMessage("Failed :(");
  }
  
  @Test
  public void should_include_message_with_cause_when_failing() {
    String message = "Some Throwable";
    Throwable cause = new Throwable();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions.fail(message, cause))
                                                   .withMessage(message).withCause(cause);
  }

  @Test
  public void should_return_a_value() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      int i = Optional.<Integer>empty().orElseGet(() -> Assertions.fail("Failed :("));
    }).withMessage("Failed :(");
  }
}
