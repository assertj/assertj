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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class Preconditions_checkArgument_Test {

  @Test
  void should_throw_illegalargumentexception_if_expression_is_false() {
    assertThatIllegalArgumentException().isThrownBy(() -> Preconditions.checkArgument(false, "Invalid parameter %s",
                                                                                      "foo"))
                                        .withMessage("Invalid parameter foo");
  }

  @Test
  void should_not_throw_if_expression_is_true() {
    Preconditions.checkArgument(true, "Invalid parameter %s", "foo");
  }

  @Test
  void should_call_message_supplier_once_if_expression_is_false() {
    AtomicInteger callCount = new AtomicInteger();
    String str = "Super secret message!!!";
    Supplier<String> msg = () -> {
      int nCalls = callCount.incrementAndGet();
      assertThat(nCalls).isEqualTo(1);
      return str;
    };
    assertThatIllegalArgumentException().isThrownBy(() -> Preconditions.checkArgument(false, msg)).withMessage(str);
    assertThat(callCount.get()).isEqualTo(1);
  }

  @Test
  void should_never_call_message_supplier_if_expression_is_true() {
    Preconditions.checkArgument(true, () -> {throw new AssertionError();});
  }
}
