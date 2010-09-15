/*
 * Created on Sep 14, 2010
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

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.internal.Objects;
import org.junit.*;

/**
 * Tests for <code>{@link CollectionAssert#isEqualTo(Collection)}</code>.
 *
 * @author Alex Ruiz
 */
public class CollectionAssert_isNotEqualTo_Test {

  private static List<String> actual;

  private Objects objects;
  private CollectionAssert assertions;

  @BeforeClass public static void setUpOnce() {
    actual = emptyList();
  }

  @Before public void setUp() {
    objects = mock(Objects.class);
    assertions = new CollectionAssert(actual, objects);
  }

  @Test public void should_verify_that_expected_value_is_not_equal_to_actual_value() {
    List<String> expected = new ArrayList<String>();
    assertions.isNotEqualTo(expected);
    verify(objects).assertNotEqual(assertions.info, actual, expected);
  }

  @Test public void should_return_this() {
    CollectionAssert returned = assertions.isNotEqualTo(emptyList());
    assertSame(assertions, returned);
  }
}
