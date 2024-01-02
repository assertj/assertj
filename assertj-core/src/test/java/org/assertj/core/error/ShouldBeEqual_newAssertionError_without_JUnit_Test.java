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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.assertj.core.description.Description;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.ComparisonFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
class ShouldBeEqual_newAssertionError_without_JUnit_Test {

  private Description description;
  private ShouldBeEqual factory;
  private ConstructorInvoker constructorInvoker;

  @BeforeEach
  public void setUp() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
    description = new TestDescription("Jedi");
    factory = (ShouldBeEqual) shouldBeEqual("Luke", "Yoda", new StandardRepresentation());
    constructorInvoker = mock(ConstructorInvoker.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    factory.constructorInvoker = constructorInvoker;
  }

  @Test
  void should_create_AssertionFailedError_if_created_ComparisonFailure_is_null() throws Exception {
    when(createComparisonFailure()).thenReturn(null);
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    check(error);
  }

  @Test
  void should_create_AssertionFailedError_if_error_is_thrown_when_creating_ComparisonFailure() throws Exception {
    when(createComparisonFailure()).thenThrow(new AssertionError("Thrown on purpose"));
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    check(error);
  }

  private Object createComparisonFailure() throws Exception {
    return createComparisonFailure(constructorInvoker);
  }

  private void check(AssertionError error) throws Exception {
    verify(constructorInvoker).newInstance(AssertionFailedError.class.getName(),
                                           new Class<?>[] { String.class, Object.class, Object.class },
                                           format("[Jedi] %n" +
                                                  "expected: \"Yoda\"%n" +
                                                  " but was: \"Luke\""),
                                           STANDARD_REPRESENTATION.toStringOf("Yoda"),
                                           STANDARD_REPRESENTATION.toStringOf("Luke"));
    assertThat(error).isNotInstanceOf(ComparisonFailure.class)
                     .isInstanceOf(AssertionFailedError.class);
    AssertionFailedError assertionFailedError = (AssertionFailedError) error;
    assertThat(assertionFailedError.getActual().getValue()).isEqualTo(STANDARD_REPRESENTATION.toStringOf("Luke"));
    assertThat(assertionFailedError.getExpected().getValue()).isEqualTo(STANDARD_REPRESENTATION.toStringOf("Yoda"));
    assertThat(error).hasMessage(format("[Jedi] %n" +
                                        "expected: \"Yoda\"%n" +
                                        " but was: \"Luke\""));
  }

  private static Object createComparisonFailure(ConstructorInvoker invoker) throws Exception {
    return invoker.newInstance(ComparisonFailure.class.getName(),
                               new Class<?>[] { String.class, String.class, String.class },
                               "[Jedi]", STANDARD_REPRESENTATION.toStringOf("Yoda"), STANDARD_REPRESENTATION.toStringOf("Luke"));
  }
}
