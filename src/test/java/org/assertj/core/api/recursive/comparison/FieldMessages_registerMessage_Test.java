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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDSoftAssertions.thenSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldMessages_registerMessage_Test {

  private FieldMessages fieldMessages;

  @BeforeEach
  void setUp() {
    fieldMessages = new FieldMessages();
  }

  @Test
  void should_register_message_for_a_given_fieldLocation() {
    // GIVEN
    String fieldLocation = "foo";
    String message = "some message";
    // WHEN
    fieldMessages.registerMessage(fieldLocation, message);
    // THEN
    thenSoftly(softly -> {
      softly.then(fieldMessages.hasMessageForField(fieldLocation)).isTrue();
      softly.then(fieldMessages.getMessageForField(fieldLocation)).isEqualTo(message);
      softly.then(fieldMessages.isEmpty()).isFalse();
    });
  }

  @Test
  void should_return_negative_values_for_field_location_without_message() {
    // GIVEN
    String fieldLocation = "foo";
    // THEN
    thenSoftly(softly -> {
      softly.then(fieldMessages.hasMessageForField(fieldLocation)).isFalse();
      softly.then(fieldMessages.getMessageForField(fieldLocation)).isNull();
      softly.then(fieldMessages.isEmpty()).isTrue();
    });
  }
}
