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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class MapAssert_extractingFromEntries_Test {

  private static final Object NAME = "name";
  private Map<Object, Object> map;

  @BeforeEach
  public void setup() {
    map = new HashMap<>();
    map.put(NAME, "kawhi");
    map.put("age", 25);
  }

  @Test
  public void should_allow_assertions_on_values_extracted_from_given_extractors() {
    assertThat(map).extractingFromEntries(e -> e.getKey().toString().toUpperCase(), e -> e.getValue())
                   .contains(tuple("NAME", "kawhi"),
                             tuple("AGE", 25));
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_values_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(map).as("check name and age")
                                                                                    .extracting(NAME, "age")
                                                                                    .isEmpty())
                                                   .withMessageContaining("[check name and age]");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    map = null;
    // WHEN
    Throwable error = catchThrowable(
      () -> assertThat(map).extractingFromEntries(e -> e.getKey(), e -> e.getValue()));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

}
