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
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacterAssert;
import org.assertj.core.data.TolkienCharacterAssertFactory;
import org.junit.Test;

public class ListAssert_filteredOn_using_filterOperator_with_navigation_Test extends ListAssert_filteredOn_BaseTest {

  // these tests validate any FilterOperator with strongly typed navigation assertions
  // no need to write tests for all FilterOperators

  @Test
  public void shoul_honor_AssertFactory_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    TolkienCharacterAssertFactory tolkienCharacterAssertFactory = new TolkienCharacterAssertFactory();
    // THEN
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", in("Frodo"))
                                                      .first()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", in("Frodo"))
                                                      .last()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", in("Frodo"))
                                                      .element(0)
                                                      .hasAge(33);
  }

  @Test
  public void shoul_honor_ClassBased_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    // THEN
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", in("Frodo"))
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", in("Frodo"))
                                                     .last()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", in("Frodo"))
                                                     .element(0)
                                                     .hasAge(33);
  }

}
