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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.core.test.Name.name;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacterAssert;
import org.assertj.core.data.TolkienCharacterAssertFactory;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;

public class IterableAssert_filteredOnNull_Test extends IterableAssert_filtered_baseTest {

  @Test
  public void should_filter_iterable_under_test_on_null_property_values() {
    assertThat(employees).filteredOnNull("name").containsOnly(noname);
  }

  @Test
  public void should_filter_iterable_under_test_on_null_nested_property_values() {
    assertThat(employees).filteredOnNull("name.last").containsOnly(yoda, obiwan, noname);
  }

  @Test
  public void should_fail_if_on_of_the_iterable_element_does_not_have_given_property_or_field() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> assertThat(employees).filteredOnNull("secret"))
                                                       .withMessageContaining("Can't find any field or property with name 'secret'");
  }

  @Test
  public void shoul_honor_AssertFactory_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbitsWithoutNames();
    TolkienCharacterAssertFactory tolkienCharacterAssertFactory = new TolkienCharacterAssertFactory();
    // THEN
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnNull("name")
                                                      .first()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnNull("name")
                                                      .last()
                                                      .hasAge(35);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOnNull("name")
                                                      .element(0)
                                                      .hasAge(33);
  }

  @Test
  public void shoul_honor_ClassBased_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbitsWithoutNames();
    // THEN
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnNull("name")
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnNull("name")
                                                     .last()
                                                     .hasAge(35);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOnNull("name")
                                                     .element(0)
                                                     .hasAge(33);
  }

  @Test
  public void should_keep_assertion_state() {
    // GIVEN
    Iterable<Name> namesWithNullLast = asList(name("John", null), name("Jane", "Doe"));
    // WHEN
    IterableAssert<Name> assertion = assertThat(namesWithNullLast).as("test description")
                                                                  .withFailMessage("error message")
                                                                  .withRepresentation(UNICODE_REPRESENTATION)
                                                                  .usingElementComparator(alwaysEqual())
                                                                  .filteredOnNull("last")
                                                                  .hasSize(1)
                                                                  .contains(name("Can be", "anybody"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }

  protected static Iterable<TolkienCharacter> hobbitsWithoutNames() {
    TolkienCharacter frodo = TolkienCharacter.of(null, 33, HOBBIT);
    TolkienCharacter sam = TolkienCharacter.of(null, 35, HOBBIT);
    return asList(frodo, sam);
  }
}
