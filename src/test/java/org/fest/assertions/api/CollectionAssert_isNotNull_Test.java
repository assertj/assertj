/*
 * Created on Sep 16, 2010
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

import org.fest.assertions.internal.Objects;
import org.junit.*;

/**
 * Tests for <code>{@link CollectionAssert#isNotNull()}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class CollectionAssert_isNotNull_Test {

  private Objects objects;
  private CollectionAssert assertions;

  @Before public void setUp() {
    objects = mock(Objects.class);
    assertions = new CollectionAssert(emptyList());
    assertions.updateObjects(objects);
  }

  @Test public void should_verify_that_actual_value_is_null() {
    assertions.isNotNull();
    verify(objects).assertNotNull(assertions.info, assertions.actual);
  }

  @Test public void should_return_this() {
    CollectionAssert returned = assertions.isNotNull();
    assertSame(assertions, returned);
  }
}
