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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_fieldMessages_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setUp() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_register_custom_message_for_fields() {
    // GIVEN
    String message = "field message";
    String fieldLocation1 = "field_1";
    String fieldLocation2 = "field_2";
    // WHEN
    recursiveComparisonConfiguration.registerErrorMessageForFields(message, fieldLocation1, fieldLocation2);
    // THEN
    then(recursiveComparisonConfiguration.getMessageForField(fieldLocation1)).isEqualTo(message);
    then(recursiveComparisonConfiguration.getMessageForField(fieldLocation2)).isEqualTo(message);
  }

}
