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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#extracting(String)}</code> and <code>{@link ObjectAssert#extracting(String[])}</code>.
 */
public class ObjectAssert_extracting_Test {

  private Employee luke;

  @BeforeEach
  public void setup() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  public void should_allow_assertions_on_property_extracted_from_given_object_by_name() {
    assertThat(luke).extracting("id")
                    .isNotNull();
    assertThat(luke).extracting("name.first")
                    .isEqualTo("Luke");
  }

  @Test
  public void should_allow_assertions_on_array_of_properties_extracted_from_given_object_by_name() {
    assertThat(luke).extracting("id", "name")
                    .hasSize(2)
                    .doesNotContainNull();
    assertThat(luke).extracting("name.first", "name.last")
                    .hasSize(2)
                    .containsExactly("Luke", "Skywalker");
  }

  @Test
  public void should_use_property_field_name_as_description_when_extracting_single_property() {
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(luke).extracting("name.first")
                                                                                     .isNull())
                                                   .withMessageContaining("[Extracted: name.first]");
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_tuples_list() {
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(luke).extracting("name.first",
                                                                                                 "name.last")
                                                                                     .isEmpty())
                                                   .withMessageContaining("[Extracted: name.first, name.last]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_single_property() {
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(luke).as("check luke first name")
                                                                                     .extracting("name.first")
                                                                                     .isNull())
                                                   .withMessageContaining("[check luke first name]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_tuples_list() {
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(luke).as("check luke first and last name")
                                                                                     .extracting("name.first", "name.last")
                                                                                     .isEmpty())
                                                   .withMessageContaining("[check luke first and last name]");
  }

  @Test
  public void should_allow_to_specify_type_comparator_after_using_extracting_with_single_parameter_on_object() {
    Person obiwan = new Person("Obi-Wan");
    obiwan.setHeight(new BigDecimal("1.820"));

    Comparator<Object> heightComparator = (o1, o2) -> {
      if (o1 instanceof BigDecimal) return BIG_DECIMAL_COMPARATOR.compare((BigDecimal) o1, (BigDecimal) o2);
      throw new IllegalStateException("only supported for BigDecimal");
    };
    assertThat(obiwan).extracting("height")
                      .usingComparator(heightComparator)
                      .isEqualTo(new BigDecimal("1.82"));
  }

  @Test
  public void should_allow_to_specify_type_comparator_after_using_extracting_with_multiple_parameters_on_object() {
    Person obiwan = new Person("Obi-Wan");
    obiwan.setHeight(new BigDecimal("1.820"));

    assertThat(obiwan).extracting("name", "height")
                      .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                      .containsExactly("Obi-Wan", new BigDecimal("1.82"));
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
