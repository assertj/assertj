/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import static example.core.MyProjectAssertions.assertThat;
import static example.data.Race.HOBBIT;
// tag::import[]
import static org.assertj.core.api.Assertions.assertThat;
// end::import[]

import org.junit.jupiter.api.Test;

import example.data.TolkienCharacter;

// not run by the build (class name does not match *Test)
class MyProjectAssertionsExample {

  private final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);

  // tag::user_guide[]
  @Test
  public void successful_custom_assertion_example() {
    // assertThat(TolkienCharacter) comes from my.project.MyProjectAssertions.assertThat
    assertThat(frodo).hasName("Frodo");

    // assertThat(String) comes from org.assertj.core.api.Assertions.assertThat
    assertThat("frodo").contains("do");
  }
  // end::user_guide[]
}
// @formatter:on
