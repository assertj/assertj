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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for <code>{@link MapAssert#containsOnlyKeys(Iterable)}</code> with {@link Path} parameter.
 *
 * @author Stefano Cordio
 */
@DisplayName("MapAssert containsOnlyKeys(Path)")
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
