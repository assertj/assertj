/*
 * Created on Aug 6, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.*;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.util.Arrays.array;

import static org.mockito.Mockito.*;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ShouldBeEqual_newAssertionError_without_JUnit_Test {

  private Description description;
  private ShouldBeEqual factory;
  private ConstructorInvoker constructorInvoker;

  @Before
  public void setUp() {
    description = new TestDescription("Jedi");
    factory = (ShouldBeEqual) shouldBeEqual("Luke", "Yoda", new StandardRepresentation());
    constructorInvoker = mock(ConstructorInvoker.class);
    factory.constructorInvoker = constructorInvoker;
  }

  @Test
  public void should_create_AssertionError_if_created_ComparisonFailure_is_null() throws Exception {
    when(createComparisonFailure()).thenReturn(null);
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    check(error);
  }

  @Test
  public void should_create_AssertionError_if_error_is_thrown_when_creating_ComparisonFailure() throws Exception {
    when(createComparisonFailure()).thenThrow(new AssertionError("Thrown on purpose"));
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    check(error);
  }

  private Object createComparisonFailure() throws Exception {
    return createComparisonFailure(constructorInvoker);
  }

  private void check(AssertionError error) throws Exception {
    createComparisonFailure(verify(constructorInvoker));
    assertFalse(error instanceof ComparisonFailure);
    assertEquals("[Jedi] \nExpecting:\n <\"Yoda\">\nto be equal to:\n <\"Luke\">\nbut was not.", error.getMessage());
  }

  private static Object createComparisonFailure(ConstructorInvoker invoker) throws Exception {
    return invoker.newInstance(ComparisonFailure.class.getName(), new Class<?>[] { String.class, String.class, String.class },
        array("[Jedi]", "\"Yoda\"", "\"Luke\""));
  }
}
