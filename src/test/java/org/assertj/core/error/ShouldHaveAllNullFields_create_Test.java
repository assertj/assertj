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
package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveAllNullFields.shouldHaveAllNullFields;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;

/**
 * Tests for
 * <code>{@link ShouldHaveAllNullFields#shouldHaveAllNullFields(Object, List, List)}</code>
 *
 * @author Vladimir Chernikov
 */
class ShouldHaveAllNullFields_create_Test {

  @Test
  void should_create_error_message_for_actual_with_one_field_but_without_ignored_fields() {
    String message = shouldHaveAllNullFields(
      new Person(""), list("name"), emptyList()
    ).create(new TestDescription("TEST"));

    assertThat(message).isEqualTo(String.format("[TEST] %n"
      + "Expecting%n"
      + "  <Person[name='']>%n"
      + "to only have null property or field, but <\"name\"> was not null.%n"
      + "Check was performed on all fields/properties."
    ));
  }

  @Test
  void should_create_error_message_for_actual_with_one_field_and_with_ignored_field() {
    String message = shouldHaveAllNullFields(
      new Person(""), list("someAnotherField"), list("name")
    ).create(new TestDescription("TEST"));

    assertThat(message).isEqualTo(String.format("[TEST] %n"
      + "Expecting%n"
      + "  <Person[name='']>%n"
      + "to only have null property or field, but <\"someAnotherField\"> was not null.%n"
      + "Check was performed on all fields/properties except: <[\"name\"]>."
    ));
  }

  @Test
  void should_create_error_message_for_actual_with_several_fields_but_without_ignored_fields() {
    String message = shouldHaveAllNullFields(
      new Jedi("Joda", "green"), list("name", "lightSaberColor"), emptyList()
    ).create(new TestDescription("TEST"));

    assertThat(message).isEqualTo(String.format("[TEST] %n"
      + "Expecting%n"
      + "  <Joda the Jedi>%n"
      + "to only have null properties or fields but these were not null:%n"
      + " <[\"name\", \"lightSaberColor\"]> are null.%n"
      + "Check was performed on all fields/properties."
    ));
  }

  @Test
  void should_create_error_message_for_actual_with_several_fields_and_with_ignored_field() {
    String message = shouldHaveAllNullFields(
      new Jedi("Joda", "green"), list("lightSaberColor"), list("name")
    ).create(new TestDescription("TEST"));

    assertThat(message).isEqualTo(String.format("[TEST] %n"
      + "Expecting%n"
      + "  <Joda the Jedi>%n"
      + "to only have null property or field, but <\"lightSaberColor\"> was not null.%n"
      + "Check was performed on all fields/properties except: <[\"name\"]>."
    ));
  }
}
