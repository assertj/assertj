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
package org.assertj.core.groups;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.PropertySupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Properties#from(Collection)}</code>.
 * 
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
public class Properties_from_with_Collection_Test {

  private Employee yoda;
  private List<Employee> employees;
  private PropertySupport propertySupport;
  private String propertyName;
  private Properties<Integer> properties;

  @BeforeEach
  public void setUp() {
    yoda = new Employee(6000L, new Name("Yoda"), 800);
    employees = newArrayList(yoda);
    propertySupport = mock(PropertySupport.class);
    propertyName = "age";
    properties = new Properties<>(propertyName, Integer.class);
    properties.propertySupport = propertySupport;
  }

  @Test
  public void should_return_values_of_property() {
    List<Integer> ages = newArrayList();
    ages.add(yoda.getAge());
    when(propertySupport.propertyValues(propertyName, Integer.class, employees)).thenReturn(ages);
    assertThat(properties.from(employees)).isSameAs(ages);
  }
}
