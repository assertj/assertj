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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.map;

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

@DisplayName("MapAssert containsOnlyKeys(Iterable)")
class MapAssert_containsOnlyKeys_with_Iterable_Test extends MapAssertBaseTest {

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.containsOnlyKeys(list(1, 2));
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertContainsOnlyKeys(getInfo(assertions), getActual(assertions), list(1, 2));
  }

  @Test
  void should_work_with_iterable_of_subclass_of_key_type() {
    // GIVEN
    Map<Number, String> map = mapOf(entry(1, "int"), entry(2, "int"));
    // THEN
    List<Integer> ints = list(1, 2); // not a List<Number>
    assertThat(map).containsOnlyKeys(ints);
  }

  @Nested
  @DisplayName("given Path parameter")
  class MapAssert_containsOnlyKeys_with_Path_Test extends MapAssertBaseTest {

    private final Path path = Paths.get("file");

    @Override
    protected MapAssert<Object, Object> invoke_api_method() {
      return assertions.containsOnlyKeys(path);
    }

    @Override
    protected void verify_internal_effects() {
      // Path parameter should be treated as a single key and not as an Iterable, therefore the target for verification is
      // assertContainsOnlyKeys(AssertionInfo, Map<K, V>, K...) instead of
      // assertContainsOnlyKeys(AssertionInfo, Map<K, V>, Iterable<? extends K>).
      // Casting the Path parameter to Object allows to invoke the overloaded method expecting vararg keys.
      verify(maps).assertContainsOnlyKeys(getInfo(assertions), getActual(assertions), (Object) path);
    }

  }

}
