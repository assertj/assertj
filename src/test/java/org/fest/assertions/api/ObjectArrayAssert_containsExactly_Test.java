/*
 * Created on Jun 17, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.ObjectArrayFactory.emptyArray;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.fest.assertions.internal.Objects;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrayAssert#containsExactly(Object...)}</code>.
 * 
 * @author Nicolas François
 */
public class ObjectArrayAssert_containsExactly_Test {

  private Objects objects;
  private ObjectArrayAssert<Object> assertions;

  @Before
  public void setUp() {
    objects = mock(Objects.class);
    assertions = new ObjectArrayAssert<Object>(emptyArray());
    assertions.objects = objects;
  }

  @Test
  public void should_verify_that_actual_contains_given_values_exactly() {
    assertions.containsExactly("Yoda", "Luke");
    verify(objects).assertEqual(assertions.info, assertions.actual, list("Yoda", "Luke"));
  }

  @Test
  public void should_return_this() {
    ObjectArrayAssert<Object> returned = assertions.containsExactly("Luke");
    assertSame(assertions, returned);
  }
}
