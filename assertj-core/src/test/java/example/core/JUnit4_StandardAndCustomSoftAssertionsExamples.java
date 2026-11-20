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

import static example.data.Race.HOBBIT;

import org.junit.Rule;
import org.junit.Test;

import example.data.TolkienCharacter;

// tag::user_guide[]
public class JUnit4_StandardAndCustomSoftAssertionsExamples {
  // end::user_guide[]
  private final TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);

  // tag::user_guide[]
  @Rule
  public final JUnitFantasySoftAssertions softly = new JUnitFantasySoftAssertions();

  @Test
  public void successful_junit_soft_custom_assertion_example() {
    softly.assertThat(frodo).hasName("Frodo")
                            .hasAge(33);
    softly.assertThat(frodo.age).isEqualTo(33);
  }
}
// end::user_guide[]
// @formatter:on
