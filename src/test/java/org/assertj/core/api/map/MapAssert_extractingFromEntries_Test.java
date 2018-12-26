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
package org.assertj.core.api.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class MapAssert_extractingFromEntries_Test {

  private Map<Object, Object> map;

  @BeforeEach
  void setup() {
    map = new HashMap<>();
    map.put("name", "bepson");
    map.put("age", 10);
  }

  @Test
  void should_pass_assertions_on_values_extracted_from_given_extractors() {

    assertThat(map).extractingFromEntries()
                   .contains(tuple(),
                             tuple());

    assertThat(map).extractingFromEntries(Map.Entry::getKey)
                   .contains("name", "age");

    assertThat(map).extractingFromEntries(e -> e.getKey().toString().toUpperCase(), Map.Entry::getValue)
                   .contains(tuple("NAME", "bepson"),
                             tuple("AGE", 10));
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_values_list() {

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(map).as("check name")
                                                                                    .extractingFromEntries(
                                                                                      Map.Entry::getKey)
                                                                                    .isEmpty())
                                                   .withMessageContaining("[check name]");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(map).as("check name and age")
                                                                                    .extractingFromEntries(
                                                                                      Map.Entry::getKey,
                                                                                      Map.Entry::getValue)
                                                                                    .isEmpty())
                                                   .withMessageContaining("[check name and age]");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    map = null;

    // WHEN
    Throwable error = catchThrowable(
      () -> assertThat(map).extractingFromEntries(Map.Entry::getKey));
    // THEN
    assertThat(error).hasMessage(actualIsNull());

    // WHEN
    error = catchThrowable(
      () -> assertThat(map).extractingFromEntries(Map.Entry::getKey, Map.Entry::getValue));
    // THEN
    assertThat(error).hasMessage(actualIsNull());

  }
}
