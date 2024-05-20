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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertWith;

import java.util.function.Consumer;
import org.assertj.tests.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class Assertions_assertWith_Test {

  private Jedi yoda = new Jedi("Yoda", "Green");

  @Test
  void should_pass_satisfying_single_requirement() {
    assertWith(yoda, jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green"));
  }

  @Test
  void should_pass_satisfying_multiple_requirements() {
    assertWith(yoda,
               jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green"),
               jedi -> assertThat(jedi.getName()).isEqualTo("Yoda"));
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
    assertThatIllegalArgumentException().isThrownBy(() -> assertWith(yoda, nullRequirements))
                                        .withMessage("No assertions group should be null");
  }

}
