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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.introspection.Introspection.getProperty;

import java.beans.PropertyDescriptor;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Before;
import org.junit.Test;

public class Introspection_getProperty_Test {
  private Employee judy;

  @Before
  public void initData() {
    judy = new Employee(100000.0, 31);
  }

  @Test
  public void get_descriptor_for_property() {
    PropertyDescriptor propertyDescriptor = getProperty("age", judy);
    assertThat(propertyDescriptor).isNotNull();
    assertThat(propertyDescriptor.getName()).isEqualTo("age");
  }

  @Test
  public void get_descriptor_for_property_from_interface_default_method() {
    PropertyDescriptor propertyDescriptor = getProperty("degree", judy);
    assertThat(propertyDescriptor).isNotNull();
    assertThat(propertyDescriptor.getName()).isEqualTo("degree");
    assertThat(propertyDescriptor.getPropertyType()).isEqualTo(String.class);
  }

  @Test
  public void should_raise_an_error_because_of_missing_getter() {
    assertThatThrownBy(() -> getProperty("salary", judy)).isInstanceOf(IntrospectionError.class)
                                                         .hasMessage("No getter for property 'salary' in org.assertj.core.util.Employee");
  }

  @Test
  public void should_raise_an_error_because_of_non_public_getter() {
    assertThatThrownBy(() -> getProperty("firstJob", judy)).isInstanceOf(IntrospectionError.class)
                                                           .hasMessage("No public getter for property 'firstJob' in org.assertj.core.util.Employee");
    assertThatThrownBy(() -> getProperty("company", judy)).isInstanceOf(IntrospectionError.class)
                                                          .hasMessage("No public getter for property 'company' in org.assertj.core.util.Employee");
  }

}
