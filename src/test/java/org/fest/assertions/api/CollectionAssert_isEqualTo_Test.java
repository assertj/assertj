/*
 * Created on Aug 3, 2010
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
 * @author Yvonne Wang
 */
public class CollectionAssert_isEqualTo_Test {

  private Objects objects;
  private CollectionAssert assertions;

  @Before public void setUp() {
    objects = mock(Objects.class);
    assertions = new CollectionAssert(emptyList());
    assertions.objects = objects;
  }

  @Test public void should_verify_that_actual_is_equal_to_expected() {
    List<String> expected = new ArrayList<String>();
    assertions.isEqualTo(expected);
    verify(objects).assertEqual(assertions.info, assertions.actual, expected);
  }

  @Test public void should_return_this() {
    CollectionAssert returned = assertions.isEqualTo(emptyList());
    assertSame(assertions, returned);
  }
}
