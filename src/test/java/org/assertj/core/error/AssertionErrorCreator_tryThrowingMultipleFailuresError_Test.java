/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

public class AssertionErrorCreator_tryThrowingMultipleFailuresError_Test {

  private AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();

  @Test
  public void should_throw_MultipleFailuresError() {
    // GIVEN
    List<? extends AssertionError> errors = list(new AssertionError("error1"), new AssertionError("error2"));
    // WHEN
    Throwable thrown = catchThrowable(() -> assertionErrorCreator.tryThrowingMultipleFailuresError(errors));
    // THEN
    assertThat(thrown).isInstanceOf(MultipleFailuresError.class)
                      .hasMessage(format("Multiple Failures (2 failures)%n"
                                         + "\terror1%n"
                                         + "\terror2"));
    MultipleFailuresError assertionFailedError = (MultipleFailuresError) thrown;
    assertThat(assertionFailedError.getFailures()).containsExactlyElementsOf(errors);
  }

  @Test
  public void should_not_throw_MultipleFailuresError_when_failing_to_create_it() throws Exception {
    // GIVEN
    List<? extends AssertionError> errors = list(new AssertionError("error1"), new AssertionError("error2"));
    ConstructorInvoker constructorInvoker = mock(ConstructorInvoker.class);
    given(constructorInvoker.newInstance(anyString(), any(Class[].class), any(Object[].class))).willThrow(Exception.class);
    assertionErrorCreator.constructorInvoker = constructorInvoker;
    // THEN
    assertThatCode(() -> assertionErrorCreator.tryThrowingMultipleFailuresError(errors)).doesNotThrowAnyException();
  }
}
