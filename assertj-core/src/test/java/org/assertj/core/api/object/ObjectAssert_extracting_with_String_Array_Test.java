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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import java.math.BigDecimal;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectAssert_extracting_with_String_Array_Test implements NavigationMethodBaseTest<ObjectAssert<Employee>> {

  private Employee luke;
  private Employee leia;

  @BeforeEach
  void setup() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    luke.setAttribute("side", "light");
    leia = new Employee(2L, new Name("Leia", "Skywalker"), 26);
    luke.setRelation("brother", null);
    luke.setRelation("sister", leia);
    leia.setRelation("brother", luke);
  }

  @Test
  void should_allow_assertions_on_array_of_properties_extracted_from_given_object_by_name() {
    // WHEN/THEN
    assertThat(luke).extracting("id", "name")
                    .hasSize(2)
                    .doesNotContainNull();
  }

  @Test
  void should_allow_assertions_on_array_of_nested_properties_extracted_from_given_object_by_name() {
    // WHEN/THEN
    assertThat(luke).extracting("name.first", "name.last")
                    .hasSize(2)
                    .containsExactly("Luke", "Skywalker");
  }

  @Test
  void should_allow_assertion_on_mixed_properties_or_fields_with_nested_map_values() {
    // WHEN/THEN
    assertThat(luke).extracting("id", "name.last", "attributes.side", "relations.sister", "relations.sister.relations.brother.id")
                    .containsExactly(2L, "Skywalker", "light", leia, 2L);
  }

  @Test
  void should_follow_map_get_behavior_for_key_with_null_value() {
    // WHEN/THEN
    assertThat(luke).extracting("relations.brother", "relations.sister", "relations.sister.name.first")
                    .containsExactly(null, leia, "Leia");
  }

  @Test
  void should_throw_IntrospectionError_if_nested_map_key_does_not_exist() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(luke).extracting("relations.unknown", "relations.sister"));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_use_property_field_names_as_description_when_extracting_tuples_list() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(luke).extracting("name.first", "name.last")
                                                                      .isEmpty());
    // THEN
    then(error).hasMessageContaining("[Extracted: name.first, name.last]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_tuples_list() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(luke).as("check luke first and last name")
                                                                      .extracting("name.first", "name.last")
                                                                      .isEmpty());
    // THEN
    then(error).hasMessageContaining("[check luke first and last name]");
  }

  @Test
  void should_allow_to_specify_type_comparator_after_using_extracting_with_multiple_parameters_on_object() {
    // GIVEN
    Person obiwan = new Person("Obi-Wan");
    obiwan.setHeight(new BigDecimal("1.820"));
    // WHEN/THEN
    assertThat(obiwan).extracting("name", "height")
                      .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                      .containsExactly("Obi-Wan", new BigDecimal("1.82"));
  }

  @Override
  public ObjectAssert<Employee> getAssertion() {
    return new ObjectAssert<>(luke);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ObjectAssert<Employee> assertion) {
    return assertion.extracting("id", "name");
  }

  @SuppressWarnings("unused")
  private static class Person {

    private final String name;
    private BigDecimal height;

    public Person(String name) {
      this.name = name;
    }

    public void setHeight(BigDecimal height) {
      this.height = height;
    }
  }

}
