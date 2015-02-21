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
package org.assertj.core.api;

import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.assertj.core.internal.Maps;


/**
 * Base class for {@link MapAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class MapAssertBaseTest extends BaseTestTemplate<MapAssert<Object, Object>, Map<Object, Object>> {
  protected Maps maps;
    
  @Override
  protected MapAssert<Object, Object> create_assertions() {
    return new MapAssert<>(emptyMap());
  }
 
  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    maps = mock(Maps.class);
    assertions.maps = maps;
  }
}
