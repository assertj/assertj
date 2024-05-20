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
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.testkit.TolkienCharacter;
import org.assertj.core.testkit.TolkienCharacterAssert;
import org.assertj.core.testkit.TolkienCharacterAssertFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListAssert filteredOn function with navigation")
class ListAssert_filteredOn_function_with_navigation_Test extends ListAssert_filteredOn_BaseTest {

  @Test
  void should_honor_AssertFactory_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    TolkienCharacterAssertFactory tolkienCharacterAssertFactory = new TolkienCharacterAssertFactory();
    // WHEN/THEN
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(TolkienCharacter::getName, "Frodo")
                                                      .first()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(TolkienCharacter::getName, "Frodo")
                                                      .last()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(TolkienCharacter::getName, "Frodo")
                                                      .element(0)
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(TolkienCharacter::getName, "Frodo")
                                                      .elements(0)
                                                      .first()
                                                      .hasAge(33);
  }

  @Test
  void should_honor_class_based_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    // WHEN/THEN
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(TolkienCharacter::getName, "Frodo")
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(TolkienCharacter::getName, "Frodo")
                                                     .last()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(TolkienCharacter::getName, "Frodo")
                                                     .element(0)
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(TolkienCharacter::getName, "Frodo")
                                                     .elements(0)
                                                     .first()
                                                     .hasAge(33);
  }

}
