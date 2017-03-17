/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.setAllowExtractingPrivateFields;

import org.junit.Test;

public class AtomicReferenceArrayAssert_filteredOn_not_Test extends AtomicReferenceArrayAssert_filtered_baseTest {

  @Test
  public void should_apply_not_filter() {
    assertThat(employees).filteredOn("age", not(800)).containsOnly(luke, noname);
  }

  @Test
  public void should_filter_object_array_under_test_on_property_not_backed_by_a_field_values() {
    assertThat(employees).filteredOn("adult", not(false)).containsOnly(yoda, obiwan, luke);
    assertThat(employees).filteredOn("adult", not(true)).containsOnly(noname);
  }

  @Test
  public void should_filter_object_array_under_test_on_public_field_values() {
    assertThat(employees).filteredOn("id", not(1L)).containsOnly(noname, obiwan, luke);
  }

  @Test
  public void should_filter_object_array_under_test_on_private_field_values() {
    assertThat(employees).filteredOn("city", not("New York")).isEmpty();
    assertThat(employees).filteredOn("city", not("Paris")).containsOnly(yoda, obiwan, luke, noname);
  }

  @Test
  public void should_fail_if_filter_is_on_private_field_and_reading_private_field_is_disabled() {
    thrown.expectIntrospectionError();
    setAllowExtractingPrivateFields(false);
    try {
      assertThat(employees).filteredOn("city", not("New York"));
    } finally {
      setAllowExtractingPrivateFields(true);
    }
  }

  @Test
  public void should_filter_object_array_under_test_on_nested_property_values() {
    assertThat(employees).filteredOn("name.first", not("Luke")).containsOnly(yoda, obiwan, noname);
  }

  @Test
  public void should_filter_object_array_under_test_on_nested_mixed_property_and_field_values() {
    assertThat(employees).filteredOn("name.last", not("Vader")).containsOnly(luke, yoda, obiwan, noname);
    assertThat(employees).filteredOn("name.last", not("Skywalker")).containsOnly(yoda, obiwan, noname);
  }

  @Test
  public void should_fail_if_given_property_or_field_name_is_null() {
    thrown.expectIllegalArgumentException("The property/field name to filter on should not be null or empty");
    assertThat(employees).filteredOn(null, not(800));
  }

  @Test
  public void should_fail_if_given_property_or_field_name_is_empty() {
    thrown.expectIllegalArgumentException("The property/field name to filter on should not be null or empty");
    assertThat(employees).filteredOn("", not(800));
  }

  @Test
  public void should_fail_if_given_expected_value_is_null() {
    thrown.expectIllegalArgumentException(format("The expected value should not be null.%n"
                                               + "If you were trying to filter on a null value, please use filteredOnNull(String propertyOrFieldName) instead"));
    assertThat(employees).filteredOn("name", null);
  }

  @Test
  public void should_fail_if_on_of_the_object_array_element_does_not_have_given_property_or_field() {
    thrown.expectIntrospectionErrorWithMessageContaining("Can't find any field or property with name 'secret'");
    assertThat(employees).filteredOn("secret", not("???"));
  }
}
