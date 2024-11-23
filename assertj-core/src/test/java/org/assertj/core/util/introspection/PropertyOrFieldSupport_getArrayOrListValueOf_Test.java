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
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.testkit.Office;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author DAniel Giribet Farre
 */
public class PropertyOrFieldSupport_getArrayOrListValueOf_Test {

  private final PropertyOrFieldSupport underTest = PropertyOrFieldSupport.EXTRACTION;
  private final Employee yoda = new Employee(1L, new Name("Yoda"), 800);
  private final Employee luke = new Employee(3L, new Name("Luke", "Skywalker"), 24);
  private final Office office = new Office();

  @BeforeEach
  void setup() {
    office.addEmployees(newArrayList(yoda, luke));
  }

  @Test
  void should_throw_error_when_index_is_empty() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.getArrayOrListValue("employees[]", office));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_throw_error_when_index_is_not_a_number() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.getArrayOrListValue("employees[foo]", office));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_throw_error_when_index_is_out_of_bounds() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.getArrayOrListValue("employees[2]", office));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_throw_error_when_index_is_negative() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.getArrayOrListValue("employees[-1]", office));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

}
