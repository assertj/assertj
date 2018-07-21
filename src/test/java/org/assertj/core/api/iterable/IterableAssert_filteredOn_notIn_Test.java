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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.notIn;
import static org.assertj.core.api.Assertions.setAllowExtractingPrivateFields;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;

public class IterableAssert_filteredOn_notIn_Test extends IterableAssert_filtered_baseTest {

  @Test
  public void should_apply_notIn_filter() {
    assertThat(employees).filteredOn("age", notIn(800, 10)).containsOnly(luke);
    assertThat(employees).filteredOn("age", notIn(800)).containsOnly(luke, noname);
  }

  @Test
  public void should_filter_iterable_under_test_on_property_not_backed_by_a_field_values() {
    assertThat(employees).filteredOn("adult", notIn(false)).containsOnly(yoda, obiwan, luke);
    assertThat(employees).filteredOn("adult", notIn(true)).containsOnly(noname);
    assertThat(employees).filteredOn("adult", notIn(true, false)).isEmpty();
  }

  @Test
  public void should_filter_iterable_under_test_on_public_field_values() {
    assertThat(employees).filteredOn("id", notIn(2L, 3L, 4L)).containsOnly(yoda);
  }

  @Test
  public void should_filter_iterable_under_test_on_private_field_values() {
    assertThat(employees).filteredOn("city", notIn("Paris")).containsOnly(yoda, obiwan, luke, noname);
    assertThat(employees).filteredOn("city", notIn("New York")).isEmpty();
    assertThat(employees).filteredOn("city", notIn("New York", "Paris")).isEmpty();
  }

  @Test
  public void should_fail_if_filter_is_on_private_field_and_reading_private_field_is_disabled() {
    setAllowExtractingPrivateFields(false);
    try {
      assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> {
        assertThat(employees).filteredOn("city", notIn("New York")).isEmpty();
      });
    } finally {
      setAllowExtractingPrivateFields(true);
    }
  }

  @Test
  public void should_filter_iterator_under_test_on_property_values() {
    assertThat(employees.iterator()).filteredOn("age", notIn(800)).containsOnly(luke, noname);
  }

  @Test
  public void should_filter_stream_under_test_on_property_values() {
    assertThat(employees.stream()).filteredOn("age", notIn(800))
                                  .containsOnly(luke, noname);
  }

  @Test
  public void should_filter_iterable_under_test_on_nested_property_values() {
    assertThat(employees).filteredOn("name.first", notIn("Luke")).containsOnly(yoda, obiwan, noname);
  }

  @Test
  public void should_filter_iterable_under_test_on_nested_mixed_property_and_field_values() {
    assertThat(employees).filteredOn("name.last", notIn("Skywalker")).containsOnly(yoda, obiwan, noname);
    assertThat(employees).filteredOn("name.last", notIn("Skywalker", null)).isEmpty();
    assertThat(employees).filteredOn("name.last", notIn("Vader")).containsOnly(yoda, obiwan, noname, luke);
  }

  @Test
  public void should_fail_if_given_property_or_field_name_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn(null, notIn(800)))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

  @Test
  public void should_fail_if_given_property_or_field_name_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn("", notIn(800)))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

  @Test
  public void should_fail_if_on_of_the_iterable_element_does_not_have_given_property_or_field() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> assertThat(employees).filteredOn("secret",
                                                                                                          notIn("???")))
                                                       .withMessageContaining("Can't find any field or property with name 'secret'");
  }

}
