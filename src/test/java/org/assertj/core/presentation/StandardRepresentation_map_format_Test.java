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
package org.assertj.core.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link StandardRepresentation#toStringOf(Map) StandardRepresentation#toStringOf(Map)}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author gabga
 */
public class StandardRepresentation_map_format_Test extends AbstractBaseRepresentationTest {
  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  public void should_return_null_if_Map_is_null() {
    Map<?,?> map = null;
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isNull();
  }

  @Test
  public void should_return_empty_braces_if_Map_is_empty() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new HashMap<String, String>())).isEqualTo("{}");
  }

  @Test
  public void should_format_Map() {
    Map<String, Class<?>> map = new LinkedHashMap<>();
    map.put("One", String.class);
    map.put("Two", File.class);
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{\"One\"=java.lang.String, \"Two\"=java.io.File}");
  }

  @Test
  public void should_format_Map_up_to_the_maximum_allowed_elements() {
    Map<Character, Integer> map = new HashMap<>();
    map.put('C', 3);
    map.put('B', 2);
    map.put('A', 1);
    StandardRepresentation.setMaxElementsForPrinting(2);
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{'A'=1, 'B'=2, ...}");
  }

  @Test
  public void should_format_Map_containing_itself() {
    Map<String, Object> map = new HashMap<>();
    map.put("One", "First");
    map.put("Myself", map);
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{\"Myself\"=(this Map), \"One\"=\"First\"}");
  }

  @Test
  public void should_sort_Map_before_formatting() {
    Map<Character, Integer> map = new HashMap<>();
    map.put('C', 3);
    map.put('B', 2);
    map.put('A', 1);
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{'A'=1, 'B'=2, 'C'=3}");
  }

  @Test
  public void should_retain_initial_ordering_if_keys_are_not_comparable() {
    Map<Object, Integer> map = new LinkedHashMap<>();
    map.put("foo", 3);
    map.put(false, 2);
    map.put('A', 1);

    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{\"foo\"=3, false=2, 'A'=1}");
  }
}
