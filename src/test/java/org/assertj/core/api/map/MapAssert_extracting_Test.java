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
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class MapAssert_extracting_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_allow_assertions_on_values_extracted_from_given_map_keys() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "kawhi");
    map.put("age", 25);

    assertThat(map).extracting("name", "age")
                   .contains("kawhi", 25);

    assertThat(map).extracting(m -> m.get("name"), m -> m.get("age"))
                   .contains("kawhi", 25);
  }

  @Test
  public void should_allow_assertions_on_values_extracted_from_given_extractors() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "kawhi");
    map.put("age", 25);

    assertThat(map).extracting(m -> m.get("name"), m -> m.get("age"))
                   .contains("kawhi", 25);
  }

  @Test
  public void should_extract_null_from_unknown_key() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "kawhi");
    map.put("age", 25);

    assertThat(map).extracting("name", "id")
                   .contains("kawhi", (Object) null);
  }

  @Test
  public void should_use_key_names_as_description() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "kawhi");
    map.put("age", 25);

    thrown.expectAssertionErrorWithMessageContaining("[Extracted: name, age]");

    assertThat(map).extracting("name", "age").isEmpty();
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_values_list() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "kawhi");
    map.put("age", 25);

    thrown.expectAssertionErrorWithMessageContaining("[check name and age]");

    assertThat(map).as("check name and age").extracting("name", "age").isEmpty();
  }
}
