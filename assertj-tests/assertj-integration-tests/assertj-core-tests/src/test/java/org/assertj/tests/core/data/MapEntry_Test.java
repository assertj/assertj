/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.data;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 */
class MapEntry_Test {

  @Test
  void should_honor_equals_contract() {
    // WHEN/THEN
    EqualsVerifier.forClass(MapEntry.class)
                  .verify();
  }

  @Test
  void setValue_should_fail() {
    // GIVEN
    MapEntry<String, String> underTest = entry("name", "Yoda");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.setValue("Luke"));
    // THEN
    then(thrown).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void should_implement_toString() {
    // GIVEN
    MapEntry<String, String> underTest = entry("name", "Yoda");
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("\"name\"=\"Yoda\"");
  }

  @Test
  void should_implement_toString_using_standard_representation() {
    // GIVEN
    MapEntry<String, String[]> underTest = entry("name", new String[] { "Yoda" });
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("\"name\"=[\"Yoda\"]");
  }

}
