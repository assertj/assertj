/*
 * Created on Dec 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.map;

import static org.fest.assertions.data.MapEntry.entry;
import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.verify;

import org.fest.assertions.api.MapAssert;
import org.fest.assertions.api.MapAssertBaseTest;
import org.fest.assertions.data.MapEntry;

/**
 * Tests for <code>{@link MapAssert#contains(MapEntry...)}</code>.
 * 
 * @author Alex Ruiz
 * @author Nicolas François
 */
public class MapAssert_contains_Test extends MapAssertBaseTest {

  final MapEntry[] entries = array(entry("key1", "value1"), entry("key2", "value2"));

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.contains(entry("key1", "value1"), entry("key2", "value2"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertContains(getInfo(assertions), getActual(assertions), entries);
  }
}
