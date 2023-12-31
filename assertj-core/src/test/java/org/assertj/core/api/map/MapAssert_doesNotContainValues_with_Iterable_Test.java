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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.map;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.MapAssert#doesNotContainValues(Iterable)}</code>.
 *
 * @author Ilya Koshaleu
 */
class MapAssert_doesNotContainValues_with_Iterable_Test extends MapAssertBaseTest {

  final List<String> values = list("value1");

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.doesNotContainValues(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertDoesNotContainValues(getInfo(assertions), getActual(assertions), values);
  }

  @Test
  void should_work_with_iterable_of_subclass_of_value_type() {
    // GIVEN
    Map<String, Number> map = mapOf(entry("one", 1), entry("two", 2));
    // THEN
    List<Integer> ints = list(3, 4); // not a List<Number>
    assertThat(map).doesNotContainValues(ints);
  }

  @Nested
  @DisplayName("given Path parameter")
  class MapAssert_doesNotContainValues_with_Path_Test extends MapAssertBaseTest {

    private final Path path = Paths.get("file");

    @Override
    protected MapAssert<Object, Object> invoke_api_method() {
      return assertions.doesNotContainValues(path);
    }

    @Override
    protected void verify_internal_effects() {
      verify(maps).assertDoesNotContainValues(getInfo(assertions), getActual(assertions), singleton(path));
    }
  }
}
