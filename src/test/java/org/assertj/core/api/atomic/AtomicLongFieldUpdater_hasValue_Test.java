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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.atomic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

import org.junit.jupiter.api.Test;

class AtomicLongFieldUpdater_hasValue_Test {

  @SuppressWarnings("unused")
  private static class Person {
    private String name;
    volatile long age;
  }

  private Person person = new Person();

  @Test
  void should_fail_when_atomicLongFieldUpdater_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((AtomicLongFieldUpdater<Person>) null).hasValue(25L,
                                                                                                                                person))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null_and_does_not_contain_expected_value() {
    AtomicLongFieldUpdater<Person> fieldUpdater = AtomicLongFieldUpdater.newUpdater(Person.class, "age");
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(fieldUpdater).hasValue(null, person))
                                        .withMessage("The expected value should not be <null>.");
  }

  @Test
  void should_fail_if_atomicLongFieldUpdater_does_not_contain_expected_value() {
    AtomicLongFieldUpdater<Person> fieldUpdater = AtomicLongFieldUpdater.newUpdater(Person.class, "age");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(fieldUpdater).hasValue(25L, person))
                                                   .withMessage(shouldHaveValue(fieldUpdater, person.age, 25L, person).create());
  }

  @Test
  void should_pass_if_atomicLongFieldUpdater_contains_expected_value() {
    AtomicLongFieldUpdater<Person> fieldUpdater = AtomicLongFieldUpdater.newUpdater(Person.class, "age");
    fieldUpdater.set(person, 25);
    assertThat(fieldUpdater).hasValue(25L, person);
  }

}
