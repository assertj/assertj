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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeMessages_registerMessage_Test {

  private TypeMessages typeMessages;

  @BeforeEach
  void setUp() {
    typeMessages = new TypeMessages();
  }

  @Test
  void should_register_message_for_specific_type() {
    // GIVEN
    Class<String> type = String.class;
    String message = "type message";
    // WHEN
    typeMessages.registerMessage(type, message);
    // THEN
    then(typeMessages.hasMessageForType(type)).isTrue();
    then(typeMessages.getMessageForType(String.class)).isEqualTo(message);
    then(typeMessages.isEmpty()).isFalse();
  }

  @Test
  void should_return_negative_results_for_type_without_message() {
    // GIVEN
    Class<String> type = String.class;
    // THEN
    then(typeMessages.hasMessageForType(type)).isFalse();
    then(typeMessages.getMessageForType(String.class)).isNull();
    then(typeMessages.isEmpty()).isTrue();
  }

  @Test
  void should_return_most_relevant_message() {
    // GIVEN
    String message = "type message";
    // WHEN
    typeMessages.registerMessage(Foo.class, message);
    // THEN
    then(typeMessages.hasMessageForType(Bar.class)).isTrue();
    then(typeMessages.getMessageForType(Bar.class)).isEqualTo(message);
  }

  private interface Foo {

  }

  private interface Bar extends Foo {

  }
}
