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

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.assertj.core.test.Maps;

import java.util.Map;

import static org.assertj.core.data.MapEntry.entry;
import static org.mockito.Mockito.*;

/**
 * Tests for <code>{@link org.assertj.core.api.MapAssert#hasSameSizeAs(java.util.Map)}</code>.
 *
 * @author Adam Ruka
 */
public class MapAssert_hasSameSizeAs_with_Map_Test extends MapAssertBaseTest {
  private final Map<?, ?> other = Maps.mapOf(entry("name", "Yoda"), entry("job", "Jedi Master"));

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.hasSameSizeAs(other);
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertHasSameSizeAs(getInfo(assertions), getActual(assertions), other);
  }
}
