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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for {@link Maps#format(Map)} and {@link Maps#format(org.assertj.core.presentation.Representation, Map)}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Maps_format_Test {

  private final StandardRepresentation standardRepresentation = new StandardRepresentation();

  @Test
  public void should_return_null_if_Map_is_null() {
    assertThat(Maps.format(standardRepresentation, null)).isNull();
    assertThat(Maps.format(null)).isNull();
  }

  @Test
  public void should_return_empty_braces_if_Map_is_empty() {
    assertThat(Maps.format(standardRepresentation, new HashMap<String, String>())).isEqualTo("{}");
    assertThat(Maps.format(new HashMap<String, String>())).isEqualTo("{}");
  }

  @Test
  public void should_format_Map() {
    Map<String, Class<?>> map = new LinkedHashMap<>();
    map.put("One", String.class);
    map.put("Two", File.class);
    assertThat(Maps.format(standardRepresentation, map)).isEqualTo("{\"One\"=java.lang.String, \"Two\"=java.io.File}");
    assertThat(Maps.format(map)).isEqualTo("{\"One\"=java.lang.String, \"Two\"=java.io.File}");
  }

  @Test
  public void should_format_Map_containing_itself() {
    Map<String, Object> map = Maps.newHashMap();
    map.put("One", "First");
    map.put("Myself", map);
    assertThat(Maps.format(standardRepresentation, map)).isEqualTo("{\"One\"=\"First\", \"Myself\"=(this Map)}");
    assertThat(Maps.format(map)).isEqualTo("{\"One\"=\"First\", \"Myself\"=(this Map)}");
  }
}
