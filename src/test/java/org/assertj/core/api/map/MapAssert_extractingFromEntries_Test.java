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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapAssert_extractingFromEntries_Test {

  private Map<Object, Object> map;

  @BeforeEach
  void setup() {
    map = mapOf(entry("name", "bepson"), entry("age", 10));
  }

  @Test
  void should_pass_when_given_no_extractors() {
    assertThat(map).extractingFromEntries()
                   .contains(tuple(), tuple());
  }

  @Test
  void should_pass_assertions_on_values_extracted_from_given_extractors() {
    assertThat(map).extractingFromEntries(e -> e.getKey().toString().toUpperCase(), Map.Entry::getValue)
                   .contains(tuple("NAME", "bepson"),
                             tuple("AGE", 10));
  }

  @Test
  void should_pass_assertions_on_values_extracted_from_one_given_extractor() {
    assertThat(map).extractingFromEntries(Map.Entry::getKey)
                   .contains("name", "age");
  }

  @Test
  void should_keep_existing_description_when_extracting_multiple_values() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).as("check name and age")
                                                                     .extractingFromEntries(Map.Entry::getKey,
                                                                                            Map.Entry::getValue)
                                                                     .isEmpty());
    // THEN
    assertThat(error).hasMessageContaining("[check name and age]");
  }

  @Test
  void should_keep_existing_description_when_extracting_one_value() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).as("check name")
                                                                     .extractingFromEntries(Map.Entry::getKey)
                                                                     .isEmpty());
    // THEN
    assertThat(error).hasMessageContaining("[check name]");
  }

  @Test
  void should_fail_if_actual_is_null_when_extracting_multiple_values() {
    // GIVEN
    map = null;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(map).extractingFromEntries(Map.Entry::getKey, Map.Entry::getValue));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_when_extracting_one_value() {
    // GIVEN
    map = null;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(map).extractingFromEntries(Map.Entry::getKey));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }
}
