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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.setAllowExtractingPrivateFields;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeEach;

public class IterableAssert_filtered_baseTest {

  protected Employee yoda;
  protected Employee obiwan;
  protected Employee luke;
  protected Employee noname;
  protected List<Employee> employees;

  @BeforeEach
  public void setUp() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    obiwan = new Employee(2L, new Name("Obi"), 800);
    luke = new Employee(3L, new Name("Luke", "Skywalker"), 26);
    noname = new Employee(4L, null, 10);
    employees = newArrayList(yoda, luke, obiwan, noname);
    setAllowExtractingPrivateFields(true);
  }

  public IterableAssert_filtered_baseTest() {
    super();
  }

  protected static Iterable<TolkienCharacter> hobbits() {
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    TolkienCharacter sam = TolkienCharacter.of("Sam", 35, HOBBIT);
    return asList(frodo, sam);
  }

}