/*
 * Created on Aug 7, 2010
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
package org.fest.assertions.internal;

import static org.fest.assertions.internal.DescriptionFormatter.format;
import static org.fest.assertions.test.ExpectedExceptions.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.fest.assertions.core.*;
import org.fest.assertions.error.AssertionErrorFactory;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for <code>{@link Failures#fail(AssertionInfo, AssertionErrorFactory)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@PrepareForTest(DescriptionFormatter.class)
@RunWith(PowerMockRunner.class)
public class Failures_fail_with_AssertionInfo_Test {

  @Rule
  public ExpectedException thrown = none();

  private WritableAssertionInfo assertionInfo;
  private AssertionErrorFactory errorFactory;

  @Before
  public void setUp() {
    assertionInfo = new WritableAssertionInfo();
    mockStatic(DescriptionFormatter.class);
    errorFactory = mock(AssertionErrorFactory.class);
  }

  @Test
  public void should_create_own_AssertionError_and_when_overriding_error_message_is_specified() {
    expectAssertionErrorWithMessage(thrown, "my message");
    assertionInfo.overridingErrorMessage("my message");
    Failures.fail(assertionInfo, errorFactory);
  }

  @Test
  public void should_use_AssertionErrorFactory_when_overriding_error_message_is_not_specified() {
    MyOwnAssertionError expectedError = new MyOwnAssertionError("[description] my message");
    expectError(thrown, expectedError);
    assertionInfo.description(new TestDescription("description"));
    String formattedDescription = "[description] ";
    when(format(assertionInfo.description())).thenReturn(formattedDescription);
    when(errorFactory.newAssertionError(formattedDescription)).thenReturn(expectedError);
    Failures.fail(assertionInfo, errorFactory);
  }

  private static class MyOwnAssertionError extends AssertionError {
    private static final long serialVersionUID = 1L;

    MyOwnAssertionError(String message) {
      super(message);
    }
  }
}
