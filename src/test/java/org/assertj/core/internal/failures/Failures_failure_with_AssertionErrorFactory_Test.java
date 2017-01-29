/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.failures;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorFactory;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.TestDescription;
import org.junit.Before;
import org.junit.Test;

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
    assertThat(failure).hasMessage("my message");
  }

  @Test
  public void should_use_AssertionErrorFactory_when_overriding_error_message_is_not_specified() {
    MyOwnAssertionError expectedError = new MyOwnAssertionError("[description] my message");
    Description description = new TestDescription("description");
    info.description(description);
    when(errorFactory.newAssertionError(description, info.representation())).thenReturn(expectedError);
    AssertionError failure = failures.failure(info, errorFactory);
    assertThat(failure).isSameAs(expectedError);
  }

  private static class MyOwnAssertionError extends AssertionError {
    private static final long serialVersionUID = 1L;

    MyOwnAssertionError(String message) {
      super(message);
    }
  }
}
