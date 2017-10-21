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
package org.assertj.core.api.object;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ObjectAssert_flatExtracting_Test {

  private Map<String, List<String>> mapOfList;

  @Before
  public void beforeEachTest() {
    mapOfList = new LinkedHashMap<>();
    mapOfList.put("name", asList("Dave", "Jeff"));
    mapOfList.put("job", asList("Plumber", "Builder"));
    mapOfList.put("city", asList("Dover", "Boston", "Paris"));
  }

  @Test
  public void should_allow_assertions_on_flattened_values_extracted_from_given_map_keys() {
    assertThat(mapOfList).flatExtracting("name", "job", "city")
                         .containsExactly("Dave", "Jeff", "Plumber", "Builder", "Dover", "Boston", "Paris");
    // order of values is the order of key then key values
    assertThat(mapOfList).flatExtracting("city", "job", "name")
                         .containsExactly("Dover", "Boston", "Paris", "Plumber", "Builder", "Dave", "Jeff");
  }

  @Test
  public void should_extract_null_from_unknown_key() {
    assertThat(mapOfList).flatExtracting("name", "id", "city")
                         .containsExactly("Dave", "Jeff", null, "Dover", "Boston", "Paris");
    assertThat(mapOfList).flatExtracting("foo", "bar")
                         .containsOnlyNulls();
  }

}
