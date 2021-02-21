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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertWith;

import java.util.function.Consumer;

import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.Test;

class Assertions_assertWith_Test {

  private Jedi yoda = new Jedi("Yoda", "Green");

  @Test
  void delegates_to_satisfies_object_assertion() {
    assertWith(yoda, jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green"));
  }

  @Test
  void should_allow_strongly_typed_assertion() {
    assertWith("foo", string -> assertThat(string).startsWith("f"));
  }

  @Test
  void should_fail_when_consumer_is_null() {
    // GIVEN
    Consumer<Jedi> nullRequirements = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertWith(yoda, nullRequirements))
                                    .withMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

}
