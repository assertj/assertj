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
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.TestDescription;
import org.junit.Before;
import org.junit.Test;

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
    assertThat(failure).hasMessage("my message");
  }

  @Test
  public void should_use_ErrorMessage_when_overriding_error_message_is_not_specified() {
    Description description = new TestDescription("description");
    info.description(description);
    when(errorMessage.create(description, info.representation())).thenReturn("[description] my message");
    AssertionError failure = failures.failure(info, errorMessage);
    assertThat(failure).hasMessage("[description] my message");
  }
}
