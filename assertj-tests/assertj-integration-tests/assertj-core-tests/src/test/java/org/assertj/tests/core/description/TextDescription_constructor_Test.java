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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.description;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
class TextDescription_constructor_Test {

  @Test
  void should_set_value() {
    // GIVEN
    String value = randomText();
    // WHEN
    TextDescription description = new TextDescription(value);
    // THEN
    then(description).extracting("value").isEqualTo(value);
  }

  private static String randomText() {
    return randomUUID().toString();
  }

  @Test
  void should_return_empty_description_if_value_is_null() {
    // WHEN
    TextDescription description = new TextDescription(null);
    // THEN
    then(description).extracting("value").isEqualTo("");
  }

}
