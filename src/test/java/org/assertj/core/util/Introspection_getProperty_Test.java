/*
 * Created on Jun 28, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.util;

import static junit.framework.Assert.*;
import static org.assertj.core.util.introspection.Introspection.getProperty;

import java.beans.PropertyDescriptor;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.*;

public class Introspection_getProperty_Test {
  private Employee judy;

  @Before
  public void initData() {
    judy = new Employee(100000.0, 31);
  }

  @Test
  public void get_descriptor_for_property() {
    PropertyDescriptor propertyDescriptor = getProperty("age", judy);
    assertNotNull(propertyDescriptor);
    assertEquals("age", propertyDescriptor.getName());
  }

  @Test
  public void should_raise_an_error_because_of_missing_getter() {
    try {
      getProperty("salary", judy);
    } catch (IntrospectionError error) {
      assertEquals("No getter for property 'salary' in org.assertj.core.util.Employee", error.getMessage());
    }
  }

  @Test
  public void should_raise_an_error_because_of_non_public_getter() {
    try {
      getProperty("company", judy);
    } catch (IntrospectionError error) {
      assertEquals("No public getter for property 'company' in org.assertj.core.util.Employee", error.getMessage());
    }
    try {
      getProperty("firstJob", judy);
    } catch (IntrospectionError error) {
      assertEquals("No public getter for property 'firstJob' in org.assertj.core.util.Employee", error.getMessage());
    }
  }

}
