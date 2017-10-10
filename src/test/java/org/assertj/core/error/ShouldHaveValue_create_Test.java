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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.assertj.core.internal.TestDescription;
import org.junit.Before;
import org.junit.Test;

public class ShouldHaveValue_create_Test {

  private static final TestDescription TEST_DESCRIPTION = new TestDescription("TEST");
  private Person joe;

  @Before
  public void setup() {
    joe = new Person();
    joe.name = "Joe";
    joe.age = 33;
    joe.account = 123456789L;
  }

  @Test
  public void should_create_error_message_for_AtomicIntegerFieldUpdater() {
    // GIVEN
    AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
    // WHEN
    String message = shouldHaveValue(updater, 33, 20, joe).create(TEST_DESCRIPTION, CONFIGURATION_PROVIDER.representation());
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting <AtomicIntegerFieldUpdater> to have value:%n" +
                                         "  <20>%n" +
                                         "but had:%n" +
                                         "  <33>%n" +
                                         "to update target object:%n" +
                                         "  <Person [name=Joe, age=33, account=123456789]>"));
  }

  @Test
  public void should_create_error_message_for_AtomicLongFieldUpdater() {
    // GIVEN
    AtomicLongFieldUpdater<Person> updater = AtomicLongFieldUpdater.newUpdater(Person.class, "account");
    // WHEN
    String message = shouldHaveValue(updater, 123456789L, 0L, joe).create(TEST_DESCRIPTION, CONFIGURATION_PROVIDER.representation());
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting <AtomicLongFieldUpdater> to have value:%n" +
                                         "  <0L>%n" +
                                         "but had:%n" +
                                         "  <123456789L>%n" +
                                         "to update target object:%n" +
                                         "  <Person [name=Joe, age=33, account=123456789]>"));
  }

  @Test
  public void should_create_error_message_for_AtomicReferenceFieldUpdater() {
    // GIVEN
    AtomicReferenceFieldUpdater<Person, String> updater = newUpdater(Person.class, String.class, "name");
    // WHEN
    String message = shouldHaveValue(updater, "Joe", "Jack", joe).create(TEST_DESCRIPTION, CONFIGURATION_PROVIDER.representation());
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting <AtomicReferenceFieldUpdater> to have value:%n" +
                                         "  <\"Jack\">%n" +
                                         "but had:%n" +
                                         "  <\"Joe\">%n" +
                                         "to update target object:%n" +
                                         "  <Person [name=Joe, age=33, account=123456789]>"));
  }

  private static class Person {
    volatile String name;
    volatile int age;
    volatile long account;

    @Override
    public String toString() {
      return format("Person [name=%s, age=%s, account=%s]", name, age, account);
    }
  }

}
