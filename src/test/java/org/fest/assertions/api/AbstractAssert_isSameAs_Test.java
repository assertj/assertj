/*
 * Created on Oct 20, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Objects;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractAssert#isSameAs(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class AbstractAssert_isSameAs_Test {

  private Objects objects;
  private ConcreteAssert assertions;

  @Before public void setUp() {
    objects = mock(Objects.class);
    assertions = new ConcreteAssert(6L);
    assertions.objects = objects;
  }

  @Test public void should_verify_that_actual_value_is_same_as_expected_value() {
    Long expected = 8L;
    assertions.isSameAs(expected);
    verify(objects).assertSame(assertions.info, assertions.actual, expected);
  }

  @Test public void should_return_this() {
    ConcreteAssert returned = assertions.isSameAs(8L);
    assertSame(assertions, returned);
  }
}