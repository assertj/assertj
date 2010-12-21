/*
 * Created on Dec 21, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import static java.util.Collections.emptyMap;
import static junit.framework.Assert.assertSame;
import static org.fest.assertions.data.MapEntry.entry;
import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.*;

import org.fest.assertions.data.MapEntry;
import org.fest.assertions.internal.Maps;
import org.junit.*;

/**
 * Tests for <code>{@link MapAssert#doesNotContain(MapEntry...)}</code>.
 *
 * @author Alex Ruiz
 */
public class MapAssert_doesNotContain_Test {


  private Maps maps;
  private MapAssert assertions;

  @Before public void setUp() {
    maps = mock(Maps.class);
    assertions = new MapAssert(emptyMap());
    assertions.maps = maps;
  }

  @Test public void should_verify_that_actual_does_not_contain_given_values() {
    assertions.doesNotContain(entry("key1", "value1"), entry("key2", "value2"));
    MapEntry[] entries = array(entry("key1", "value1"), entry("key2", "value2"));
    verify(maps).assertDoesNotContain(assertions.info, assertions.actual, entries);
  }

  @Test public void should_return_this() {
    MapAssert returned = assertions.doesNotContain(entry("key1", "value1"));
    assertSame(assertions, returned);
  }
}
