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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import example.data.Race;
import example.data.TolkienCharacter;

public class DescribingAssertionsExample {

  @Test
  void describing_assertions() {
    try {
      // tag::user_guide[]
      TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, Race.HOBBIT);

      // failing assertion, remember to call as() before the assertion!
      assertThat(frodo.getAge()).as("check %s's age", frodo.getName())
                                .isEqualTo(100);
      // end::user_guide[]
    } catch (AssertionError error) {
      // do nothing
    }
  }

}
