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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class Assertions_assertThatList_Test {

  private static class Person {
    Person(String name) {}
  }

  private static class Employee extends Person {
    public Employee(String name) {
      super(name);
    }
  }

  @Test
  void should_create_Assert() {
    // GIVEN
    AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> assertions = assertThatList(emptyList());
    // WHEN/THEN
    then(assertions).isNotNull();
  }

  @Test
  void should_create_Assert_with_list_extended() {
    // GIVEN
    List<String> strings0 = new ArrayList<>();
    List<? extends String> strings1 = new ArrayList<>();
    // WHEN/THEN
    assertThatList(strings0).isEqualTo(strings1);
    assertThatList(strings1).isEqualTo(strings0);
  }

  @Test
  void should_create_Assert_with_extends() {
    // GIVEN
    Employee bill = new Employee("bill");
    Person billou = bill;
    List<Person> list1 = list(billou);
    List<Employee> list2 = list(bill);
    // WHEN/THEN
    assertThatList(list1).isEqualTo(list2);
    assertThatList(list2).isEqualTo(list1);
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    List<String> names = singletonList("Luke");
    // WHEN/THEN
    then(assertThatList(names).actual).isSameAs(names);
  }
}
