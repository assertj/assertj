/*
 * Created on Oct 17, 2010
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

import static org.fest.assertions.api.AssertInternals.*;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.Assert;
import org.fest.assertions.internal.Objects;
import org.junit.*;

/**
 * Test case for implementations of <code>{@link Assert#isNull()}</code>.
 *
 * @author Alex Ruiz
 */
public abstract class Assert_isNull_TestCase {

  private Objects objects;
  private Assert<?> assertions;

  @Before public void setUp() {
    objects = mock(Objects.class);
    assertions = createAssertToTest(objects);
  }

  @SuppressWarnings("hiding")
  abstract Assert<?> createAssertToTest(Objects objects);

  @Test public void should_verify_that_actual_value_is_null() {
    assertions.isNull();
    verify(objects).assertNull(infoIn(assertions), actualIn(assertions));
  }
}
