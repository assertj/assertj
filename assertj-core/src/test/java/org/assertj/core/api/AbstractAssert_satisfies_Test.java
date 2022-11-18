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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class AbstractAssert_satisfies_Test {

  @Test
  void should_run_consumers_only_once() {
    // GIVEN
    AbstractStringAssert<?> assertion = assertThat("actualValue");

    AtomicInteger invocationCount = new AtomicInteger();
    Consumer<String> failingConsumer = s -> {
      invocationCount.incrementAndGet();
      assertThat(s).isEqualTo("anotherValue");
    };

    // WHEN
    assertThatThrownBy(() -> assertion.satisfies(failingConsumer))
      .hasBeenThrown();

    // THEN
    then(invocationCount).hasValue(1);
  }
}
