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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.assertj.core.presentation.Representation;
import org.junit.ComparisonFailure;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class AssertionErrorCreator_assertionError_Test {

  private AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();

  @Test
  void should_create_AssertionFailedError_using_reflection() {
    // GIVEN
    String actual = "actual";
    String expected = "expected";
    String message = "error message";
    Representation representation = mock(Representation.class);
    // WHEN
    AssertionError assertionError = assertionErrorCreator.assertionError(message, actual, expected, representation);
    // THEN
    then(assertionError).isInstanceOf(AssertionFailedError.class)
                        .hasMessage(message);
    AssertionFailedError assertionFailedError = (AssertionFailedError) assertionError;
    then(assertionFailedError.getActual().getValue()).isSameAs(actual);
    then(assertionFailedError.getExpected().getValue()).isSameAs(expected);
  }

  @Test
  void should_create_ComparisonFailure_when_AssertionFailedError_could_not_be_created() throws Exception {
    // GIVEN
    String message = "error message";
    Representation representation = mock(Representation.class);
    ConstructorInvoker constructorInvoker = mock(ConstructorInvoker.class);
    ComparisonFailure expectedFailure = new ComparisonFailure(message, "expected", "actual");
    // @format:off
    given(constructorInvoker.newInstance(eq(AssertionFailedError.class.getName()), any(Class[].class), any())).willThrow(Exception.class);
    given(constructorInvoker.newInstance(eq(ComparisonFailure.class.getName()), any(Class[].class), any())).willReturn(expectedFailure);
    // @format:on
    assertionErrorCreator.constructorInvoker = constructorInvoker;
    // WHEN
    AssertionError assertionError = assertionErrorCreator.assertionError(message, new Object(), new Object(), representation);
    // THEN
    then(assertionError).isSameAs(expectedFailure);
  }

  @Test
  void should_create_AssertionError_when_neither_AssertionFailedError_nor_ComparisonFailure_could_be_created() throws Exception {
    // GIVEN
    String message = "error message";
    ConstructorInvoker constructorInvoker = mock(ConstructorInvoker.class);
    Representation representation = mock(Representation.class);
    given(constructorInvoker.newInstance(anyString(), any(Class[].class), any())).willThrow(Exception.class);
    assertionErrorCreator.constructorInvoker = constructorInvoker;
    // WHEN
    AssertionError assertionError = assertionErrorCreator.assertionError(message, "actual", "expected", representation);
    // THEN
    then(assertionError).isNotInstanceOf(AssertionFailedError.class)
                        .hasMessage(message);
  }
}
