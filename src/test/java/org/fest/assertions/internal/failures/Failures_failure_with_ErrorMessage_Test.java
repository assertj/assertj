/*
 * Created on Oct 19, 2010
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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.error.ErrorMessageFactory;
import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.TestDescription;

import org.junit.*;

/**
 * Tests for <code>{@link Failures#failure(AssertionInfo, ErrorMessageFactory)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Failures_failure_with_ErrorMessage_Test {

  private WritableAssertionInfo info;
  private ErrorMessageFactory errorMessage;
  private Failures failures;

  @Before
  public void setUp() {
    info = new WritableAssertionInfo();
    errorMessage = mock(ErrorMessageFactory.class);
    failures = Failures.instance();
  }

  @Test
  public void should_create_use_overriding_error_message_if_it_is_specified() {
    info.overridingErrorMessage("my message");
    AssertionError failure = failures.failure(info, errorMessage);
    assertEquals("my message", failure.getMessage());
  }

  @Test
  public void should_use_ErrorMessage_when_overriding_error_message_is_not_specified() {
    Description description = new TestDescription("description");
    info.description(description);
    when(errorMessage.create(description)).thenReturn("[description] my message");
    AssertionError failure = failures.failure(info, errorMessage);
    assertEquals("[description] my message", failure.getMessage());
  }
}
