/*
 * Created on Aug 6, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.*;
import static org.fest.assertions.error.ErrorWhenNotEqualFactory.MSG_ARG_TYPES;
import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.*;

import org.junit.*;

/**
 * Tests for <code>{@link ErrorWhenNotEqualFactory#newAssertionError(String)}</code>.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenNotEqualFactory_newAssertionError_with_mocks_Test {

  private ErrorWhenNotEqualFactory errorFactory;
  private ConstructorInvoker invoker;

  @Before
  public void setUp() {
    errorFactory = new ErrorWhenNotEqualFactory("Yoda", "Luke");
    invoker = mock(ConstructorInvoker.class);
    errorFactory.constructorInvoker(invoker);
  }

  @Test
  public void should_create_AssertionError_if_created_ComparisonFailure_is_null() throws Exception {
    when(invoker.newInstance("org.junit.ComparisonFailure", MSG_ARG_TYPES, array("'Yoda'", "'Luke'"))).thenReturn(null);
    AssertionError error = errorFactory.newAssertionError("[Jedi] ");
    verify(error);
  }

  @Test
  public void should_create_AssertionError_if_error_is_thrown_when_creating_ComparisonFailure() throws Exception {
    when(invoker.newInstance("org.junit.ComparisonFailure", MSG_ARG_TYPES, array("'Yoda'", "'Luke'")))
      .thenThrow(new Exception("Thrown on purpose"));
    AssertionError error = errorFactory.newAssertionError("[Jedi] ");
    verify(error);
  }

  private void verify(AssertionError error) {
    assertFalse(error instanceof ComparisonFailure);
    assertEquals("[Jedi] expected:<'Yoda'> but was:<'Luke'>", error.getMessage());
  }
}
