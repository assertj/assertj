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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;

public class MapAssert_containsOnlyKeys_with_Iterable_Test extends MapAssertBaseTest {

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    doCallRealMethod().when(maps).assertContainsOnlyKeys(getInfo(assertions), getActual(assertions), list(1, 2));
    return assertions.containsOnlyKeys(list(1, 2));
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertContainsOnlyKeys(getInfo(assertions), getActual(assertions), list(1, 2));
    verify(maps).assertContainsOnlyKeys(getInfo(assertions), getActual(assertions), "keys iterable", array(1, 2));
  }
}
