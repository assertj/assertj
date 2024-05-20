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
import java.util.Comparator;
import java.util.Optional;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectAssert_extracting_with_String_Test implements NavigationMethodBaseTest<ObjectAssert<Employee>> {

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
  void should_allow_assertions_on_property_extracted_from_given_object_by_name() {
    // WHEN/THEN
    assertThat(luke).extracting("id")
                    .isNotNull();
  }

  @Test
  void should_allow_assertions_on_nested_property_extracted_from_given_object_by_name() {
    // WHEN/THEN
    assertThat(luke).extracting("name.first")
                    .isEqualTo("Luke");
  }

  @Test
  void should_allow_assertions_on_nested_optional_value() {
    // GIVEN
    Person chewbacca = new Person("Chewbacca");
    chewbacca.setNickname(Optional.of("Chewie"));
    // WHEN/THEN
    assertThat(chewbacca).extracting("nickname.value")
                         .isEqualTo("Chewie");
  }

  @Test
  void should_use_property_field_name_as_description_when_extracting_single_property() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(luke).extracting("name.first").isNull());
    // THEN
    then(assertionError).hasMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_single_property() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(luke).as("check luke first name")
                                                                               .extracting("name.first")
                                                                               .isNull());
    // THEN
    then(assertionError).hasMessageContaining("[check luke first name]");
  }

  @Test
  void should_allow_to_specify_type_comparator_after_using_extracting_with_single_parameter_on_object() {
    // GIVEN
    Person obiwan = new Person("Obi-Wan");
    obiwan.setHeight(new BigDecimal("1.820"));
    Comparator<Object> heightComparator = (o1, o2) -> {
      if (o1 instanceof BigDecimal) return BIG_DECIMAL_COMPARATOR.compare((BigDecimal) o1, (BigDecimal) o2);
      throw new IllegalStateException("only supported for BigDecimal");
    };
    // WHEN/THEN
    assertThat(obiwan).extracting("height")
                      .usingComparator(heightComparator)
                      .isEqualTo(new BigDecimal("1.82"));
  }

  @Test
  void should_throw_IntrospectionError_if_given_field_name_cannot_be_read() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(luke).extracting("foo"));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessageContaining("Can't find any field or property with name 'foo'.");
  }

  @Override
  public ObjectAssert<Employee> getAssertion() {
    return new ObjectAssert<>(luke);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ObjectAssert<Employee> assertion) {
    return assertion.extracting("name.first");
  }

  @SuppressWarnings("unused")
  private static class Person {

    private final String name;
    private Optional<String> nickname = Optional.empty();
    private BigDecimal height;

    public Person(String name) {
      this.name = name;
    }

    public void setNickname(Optional<String> nickname) {
      this.nickname = nickname;
    }

    public void setHeight(BigDecimal height) {
      this.height = height;
    }
  }

}
