/*
 * Created on Sep 10, 2010
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
package org.fest.assertions.formatting;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link MapToStringRule#canHandle(Object)}</code>.
 *
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class) public class MapToStringRule_canHandle_with_Maps_Test {

  private final Class<? extends Map<?, ?>> type;

  @Parameters public static List<Object[]> parameters() {
    return asList(new Object[][] { { Map.class }, { ConcurrentMap.class }, { SortedMap.class } });
  }

  public MapToStringRule_canHandle_with_Maps_Test(Class<? extends Map<?, ?>> type) {
    this.type = type;
  }

  private static MapToStringRule rule;

  @BeforeClass public static void setUpOnce() {
    rule = new MapToStringRule();
  }

  @Test public void should_return_true_if_object_is_Map() {
    assertTrue(rule.canHandle(mock(type)));
  }
}
