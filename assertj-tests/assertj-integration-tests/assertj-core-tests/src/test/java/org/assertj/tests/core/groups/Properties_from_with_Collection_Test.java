/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.groups;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.groups.Properties.extractProperty;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.tests.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class Properties_from_with_Collection_Test {

  @Test
  void should_return_values_of_property() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "Green");
    Jedi luke = new Jedi("Luke Skywalker", "Green");
    // WHEN
    List<Object> names = extractProperty("name").from(list(yoda, luke));
    // THEN
    then(names).containsExactly(yoda.getName(), luke.getName());
  }

  @Test
  void should_return_strongly_typed_values_of_property() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "Green");
    Jedi luke = new Jedi("Luke Skywalker", "Green");
    // WHEN
    List<String> names = extractProperty("name", String.class).from(list(yoda, luke));
    // THEN
    then(names).containsExactly(yoda.getName(), luke.getName());
  }
}
