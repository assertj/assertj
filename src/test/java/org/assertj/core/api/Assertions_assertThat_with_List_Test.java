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
package org.assertj.core.api;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(List)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public class Assertions_assertThat_with_List_Test {
  private static class Person {
    @SuppressWarnings("unused")
    private String name;

    public Person(String name) {
      this.name = name;
    }
  }

  private static class Employee extends Person {
    public Employee(String name) {
      super(name);
    }
  }

  @Test
  public void should_create_Assert() {
    AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> assertions = assertThat(emptyList());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_create_Assert_generics() {
    Employee bill = new Employee("bill");
    Person billou = bill;
    Assertions.assertThat(bill).isEqualTo(billou);
    Assertions.assertThat(billou).isEqualTo(bill);
  }

  @Test
  public void should_create_Assert_with_list_extended() {
    List<String> strings0 = new ArrayList<>();
    List<? extends String> strings1 = new ArrayList<>();
    Assertions.assertThat(strings0).isEqualTo(strings1);
    Assertions.assertThat(strings1).isEqualTo(strings0);
  }

  @Test
  public void should_create_Assert_with_extends() {
    Employee bill = new Employee("bill");
    Person billou = bill;
    List<Person> list1 = newArrayList(billou);
    List<Employee> list2 = newArrayList(bill);

    Assertions.assertThat(list1).isEqualTo(list2);
    Assertions.assertThat(list2).isEqualTo(list1);
  }

  @Test
  public void should_pass_actual() {
    List<String> names = singletonList("Luke");
    assertThat(assertThat(names).actual).isSameAs(names);
  }
}
