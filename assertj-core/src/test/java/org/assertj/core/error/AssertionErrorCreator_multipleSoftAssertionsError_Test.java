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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.assertj.core.api.SoftAssertionError;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

class AssertionErrorCreator_multipleSoftAssertionsError_Test {

  private AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();

  @Test
  void should_create_AssertJMultipleFailuresError_using_reflection() {
    // GIVEN
    List<AssertionError> errors = list(new AssertionError(format("%nerror1")), new AssertionError(format("%nerror2")));
    // WHEN
    AssertionError assertionError = assertionErrorCreator.multipleSoftAssertionsError(errors);
    // THEN
    then(assertionError).isInstanceOf(AssertJMultipleFailuresError.class)
                        .hasMessage(format("%nMultiple Failures (2 failures)%n" +
                                           "-- failure 1 --%n" +
                                           "error1%n" +
                                           "-- failure 2 --%n" +
                                           "error2"));
    MultipleFailuresError assertionFailedError = (MultipleFailuresError) assertionError;
    then(assertionFailedError.getFailures()).containsExactlyElementsOf(errors);
  }

  @Test
  void should_create_SoftAssertionError_when_MultipleFailuresError_could_not_be_created() throws Exception {
    // GIVEN
    List<? extends AssertionError> errors = list(new AssertionError("error1"), new AssertionError("error2"));
    ConstructorInvoker constructorInvoker = mock(ConstructorInvoker.class);
    given(constructorInvoker.newInstance(anyString(), any(Class[].class), any(Object[].class))).willThrow(Exception.class);
    assertionErrorCreator.constructorInvoker = constructorInvoker;
    // WHEN
    AssertionError assertionError = assertionErrorCreator.multipleSoftAssertionsError(errors);
    // THEN
    then(assertionError).isNotInstanceOf(MultipleFailuresError.class)
                        .isInstanceOf(SoftAssertionError.class)
                        .hasMessage(format("%n"
                                           + "The following 2 assertions failed:%n"
                                           + "1) error1%n"
                                           + "2) error2%n"));
  }
}
