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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StandardRepresentation#toStringOf(Map) StandardRepresentation#toStringOf(Map)}.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author gabga
 */
class StandardRepresentation_map_format_Test extends AbstractBaseRepresentationTest {
  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  void should_return_null_if_Map_is_null() {
    Map<?, ?> map = null;
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isNull();
  }

  @Test
  void should_return_empty_braces_if_Map_is_empty() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new HashMap<>())).isEqualTo("{}");
  }

  @Test
  void should_format_Map() {
    // GIVEN
    Map<String, Class<?>> map = new LinkedHashMap<>();
    map.put("One", String.class);
    map.put("Two", File.class);
    // WHEN
    String mapRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapRepresentation).isEqualTo("{\"One\"=java.lang.String, \"Two\"=java.io.File}");
  }

  @Test
  void should_format_Map_up_to_the_maximum_allowed_elements() {
    // GIVEN
    Map<Character, Integer> map = new HashMap<>();
    map.put('C', 3);
    map.put('B', 2);
    map.put('A', 1);
    StandardRepresentation.setMaxElementsForPrinting(2);
    // WHEN
    String mapRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapRepresentation).isEqualTo("{'A'=1, 'B'=2, ...}");
  }

  @Test
  void should_format_Map_containing_itself() {
    // GIVEN
    Map<String, Object> map = new HashMap<>();
    map.put("One", "First");
    map.put("Myself", map);
    // WHEN
    String mapRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapRepresentation).isEqualTo("{\"Myself\"=(this Map), \"One\"=\"First\"}");
  }

  @Test
  void should_sort_Map_before_formatting() {
    // GIVEN
    Map<Character, Integer> map = new HashMap<>();
    map.put('C', 3);
    map.put('B', 2);
    map.put('A', 1);
    // WHEN
    String mapRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapRepresentation).isEqualTo("{'A'=1, 'B'=2, 'C'=3}");
  }

  @Test
  void should_retain_initial_ordering_if_keys_are_not_comparable() {
    // GIVEN
    Map<Object, Integer> map = new LinkedHashMap<>();
    map.put("foo", 3);
    map.put(false, 2);
    map.put('A', 1);
    // WHEN
    String mapRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapRepresentation).isEqualTo("{\"foo\"=3, false=2, 'A'=1}");
  }

  @Test
  void should_formal_null_in_the_entry_set() {
    // GIVEN
    Map<Integer, Integer> map = new AbstractMap<Integer, Integer>() {
      @Override
      public Set<Entry<Integer, Integer>> entrySet() {
        Set<Entry<Integer, Integer>> entries = new HashSet<>();
        entries.add(null);
        return entries;
      }
    };
    // WHEN
    String mapRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapRepresentation).isEqualTo("{null}");
  }
}
