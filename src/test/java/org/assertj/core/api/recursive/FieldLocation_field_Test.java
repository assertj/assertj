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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import org.junit.jupiter.api.Test;

class FieldLocation_field_Test {

  @Test
  void should_build_child_field_location() {
    // GIVEN
    FieldLocation parentFieldLocation = new FieldLocation(list("foo"));
    // WHEN
    FieldLocation childFieldLocation = parentFieldLocation.field("bar");
    // THEN
    assertThat(childFieldLocation.getDecomposedPath()).isEqualTo(list("foo", "bar"));
    assertThat(childFieldLocation.getPathToUseInRules()).isEqualTo("foo.bar");
    assertThat(childFieldLocation.getFieldName()).isEqualTo("bar");
  }

  @Test
  void should_build_field_path_for_array() {
    // GIVEN
    FieldLocation parentFieldLocation = new FieldLocation(list("person", "[0]", "children", "[2]"));
    // WHEN
    FieldLocation childFieldLocation = parentFieldLocation.field("name");
    // THEN
    assertThat(childFieldLocation.getDecomposedPath()).isEqualTo(list("person", "[0]", "children", "[2]", "name"));
    assertThat(childFieldLocation.getPathToUseInRules()).isEqualTo("person.children.name");
    assertThat(childFieldLocation.getFieldName()).isEqualTo("name");
  }
}
