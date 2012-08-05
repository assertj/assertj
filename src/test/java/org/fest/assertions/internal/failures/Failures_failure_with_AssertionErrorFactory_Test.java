/*
 * Created on Aug 7, 2010
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
package org.fest.assertions.internal.failures;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.error.AssertionErrorFactory;
import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.TestDescription;

import org.junit.*;

/**
 * Tests for <code>{@link Failures#failure(AssertionInfo, AssertionErrorFactory)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Failures_failure_with_AssertionErrorFactory_Test {

  private WritableAssertionInfo info;
  private AssertionErrorFactory errorFactory;
  private Failures failures;

  @Before
  public void setUp() {
    info = new WritableAssertionInfo();
    errorFactory = mock(AssertionErrorFactory.class);
    failures = Failures.instance();
  }

  @Test
  public void should_create_own_AssertionError_when_overriding_error_message_is_specified() {
    info.overridingErrorMessage("my message");
    AssertionError failure = failures.failure(info, errorFactory);
    assertEquals("my message", failure.getMessage());
  }

  @Test
  public void should_use_AssertionErrorFactory_when_overriding_error_message_is_not_specified() {
    MyOwnAssertionError expectedError = new MyOwnAssertionError("[description] my message");
    Description description = new TestDescription("description");
    info.description(description);
    when(errorFactory.newAssertionError(description)).thenReturn(expectedError);
    AssertionError failure = failures.failure(info, errorFactory);
    assertSame(expectedError, failure);
  }

  private static class MyOwnAssertionError extends AssertionError {
    private static final long serialVersionUID = 1L;

    MyOwnAssertionError(String message) {
      super(message);
    }
  }
}
