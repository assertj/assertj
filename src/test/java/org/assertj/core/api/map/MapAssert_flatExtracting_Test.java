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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link MapAssert#flatExtracting(String...)}</code>.
 * 
 * @author Daniel Weber
 */
public class MapAssert_flatExtracting_Test {

  private Map<String, Object> map;

  @Before
  public void beforeEachTest() {
    String[] names = array("Dave", "Jeff");
    LinkedHashSet<String> jobs = newLinkedHashSet("Plumber", "Builder");
    Iterable<String> cities = asList("Dover", "Boston", "Paris");
    int[] ranks = { 1, 2, 3 };
    map = new LinkedHashMap<>();
    map.put("name", names);
    map.put("job", jobs);
    map.put("city", cities);
    map.put("rank", ranks);
  }

  @Test
  public void should_allow_assertions_on_flattened_values_extracted_from_given_map_keys() {
    assertThat(map).flatExtracting("name", "job", "city", "rank")
                   .containsExactly("Dave", "Jeff", "Plumber", "Builder", "Dover", "Boston", "Paris", 1, 2, 3);
    // order of values is the order of key then key values
    assertThat(map).flatExtracting("city", "job", "name")
                   .containsExactly("Dover", "Boston", "Paris", "Plumber", "Builder", "Dave", "Jeff");
  }

  @Test
  public void should_extract_null_from_unknown_key() {
    assertThat(map).flatExtracting("name", "id", "city")
                   .containsExactly("Dave", "Jeff", null, "Dover", "Boston", "Paris");
    assertThat(map).flatExtracting("foo", "bar")
                   .containsOnlyNulls();
  }

  @Test
  public void should_extract_but_not_flatten_non_collection_values() {
    map.put("year", 2017);
    assertThat(map).flatExtracting("name", "job", "year")
                   .containsExactly("Dave", "Jeff", "Plumber", "Builder", 2017);
  }

}
