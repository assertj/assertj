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
package org.assertj.core.api.iterable;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.assertj.core.test.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

public class IterableAssert_allSatisfy_Test extends IterableAssertBaseTest {

  private Consumer<Object> restrictions;

  Employee yoda = new Employee(1L, new Name("Yoda"), 800);
  Employee obiwan = new Employee(2L, new Name("Obi"), 800);
  List<Employee> employees = newArrayList(yoda, obiwan);

  Person yodaDTO = new Person("Yoda");
  Person obiwanDTO = new Person("Obi");
  Person hanDTO = new Person("Han");

  List<Person> lessPersons = newArrayList(yodaDTO, obiwanDTO);
  List<Person> samePersons = newArrayList(yodaDTO, obiwanDTO);
  List<Person> morePersons = newArrayList(yodaDTO, obiwanDTO);

  @Before
  public void beforeOnce() {
    restrictions = o -> assertThat(o).isNotNull();
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.allSatisfy(restrictions);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllSatisfy(getInfo(assertions), getActual(assertions), restrictions);
  }

  @Test
  public void should_compare_to_other() {
    assertThat(employees).allSatisfy(samePersons, (employee, person) ->
            assertThat(employee.getName().getFirst()).isEqualTo(person.getName()));
  }

  @Test
  public void should_fail_when_actual_is_null() {
    assertThat((List<Employee>) null).allSatisfy(samePersons, (employee, person) ->
            assertThat(employee.getName().getFirst()).isEqualTo(person.getName()));
  }

  @Test
  public void should_throw_NPE_when_other_is_null() {
    assertThat(employees).allSatisfy((List<Person>) null, (employee, person) ->
            assertThat(employee.getName().getFirst()).isEqualTo(person.getName()));
  }

  @Test
  public void should_throw_NPE_when_requirements_are_null() {
    assertThat(employees).allSatisfy(samePersons, null);
  }

  @Test
  public void should_fail_when_other_is_less() {
    assertThat(employees).allSatisfy(lessPersons, (employee, person) ->
            assertThat(employee.getName().getFirst()).isEqualTo(person.getName()));
  }

  @Test
  public void should_fail_when_other_is_more() {
    assertThat(employees).allSatisfy(morePersons, (employee, person) ->
            assertThat(employee.getName().getFirst()).isEqualTo(person.getName()));
  }

}
