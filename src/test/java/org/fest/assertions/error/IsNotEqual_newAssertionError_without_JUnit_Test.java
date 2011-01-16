/*
 * Created on Aug 6, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.IsNotEqual.*;
import static org.fest.assertions.test.Exceptions.assertionFailingOnPurpose;
import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.*;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.*;

/**
 * Tests for <code>{@link IsNotEqual#newAssertionError(Description)}</code>.
 *
 * @author Alex Ruiz
 */
public class IsNotEqual_newAssertionError_without_JUnit_Test {

  private Description description;
  private IsNotEqual errorFactory;
  private ConstructorInvoker invoker;

  @Before public void setUp() {
    description = new TestDescription("Jedi");
    errorFactory = (IsNotEqual) isNotEqual("Luke", "Yoda");
    errorFactory.constructorInvoker = mock(ConstructorInvoker.class);
    invoker = errorFactory.constructorInvoker;
  }

  @Test public void should_create_AssertionError_if_created_ComparisonFailure_is_null() throws Exception {
    when(createComparisonFailure()).thenReturn(null);
    AssertionError error = errorFactory.newAssertionError(description);
    verify(error);
  }

  @Test public void should_create_AssertionError_if_error_is_thrown_when_creating_ComparisonFailure() throws Exception {
    when(createComparisonFailure()).thenThrow(assertionFailingOnPurpose());
    AssertionError error = errorFactory.newAssertionError(description);
    verify(error);
  }

  private Object createComparisonFailure() throws Exception {
    return invoker.newInstance("org.junit.ComparisonFailure", MSG_ARG_TYPES, array("'Yoda'", "'Luke'"));
  }

  private void verify(AssertionError error) {
    assertEquals(AssertionError.class, error.getClass());
    assertEquals("[Jedi] expected:<'Yoda'> but was:<'Luke'>", error.getMessage());
  }
}
