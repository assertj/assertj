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

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainEntry.shouldContainEntry;
import static org.assertj.core.test.Maps.mapOf;

/**
 * Tests for <code>{@link ShouldContainEntry#create(Description)}</code>.
 */
public class ShouldContainEntry_create_Test {

  @Test
  public void should_create_error_message_with_entry_condition() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainEntry(map, new TestCondition<>("test condition"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting%n <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%n"
                                                  + "to have an entry satisfying%n <test condition>"));
  }

  @Test
  public void should_create_error_message_with_key_and_value_conditions() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainEntry(map, new TestCondition<>("test key condition"),
                                                     new TestCondition<>("test value condition"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting%n <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%n"
                                                  + "to have an entry satisfying%nkey condition%n <test key condition>%n"
                                                  + "and value condition%n <test value condition>"));
  }
}
