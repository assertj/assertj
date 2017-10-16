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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.junit.Test;
import static org.assertj.core.util.Lists.newArrayList;

/**
 * Tests for <code>{@link MapAssert#flatExtracting(String...)}</code>.
 * 
 * @author Daniel Weber
 */
public class MapAssert_flatExtracting_Test {

  @Test
  public void should_allow_assertions_on_flattened_values_extracted_from_given_map_keys() {
    Map<String, List<String>> map  = createTestData();
    assertThat(map).flatExtracting("name","job","city")
                   .contains("Dave","Jeff","Plumber","Builder","Dover","Boston");
  }
  
  private Map<String, List<String>> createTestData(){
    Map<String, List<String>> testMap  = new HashMap<>();
    testMap.put("name", Arrays.asList("Dave", "Jeff"));
    testMap.put("job",  Arrays.asList("Plumber", "Builder"));
    testMap.put("city", Arrays.asList("Dover", "Boston"));
    return testMap;
  }
  
  @Test
  public void should_extract_null_from_unknown_key() {
    Map<String, List<String>> map  = createTestData();
    assertThat(map).flatExtracting("name", "id")
                   .contains("Jeff", (Object) null);
  }

}
