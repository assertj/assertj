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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.description.Description;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.TestDescription;
import org.junit.ComparisonFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for
 * <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Filip Hrisafov
 */
class ShouldBeEqual_newAssertionError_without_JUnit_and_OTA4J_Test {

  private Description description;
  private ShouldBeEqual factory;
  private ConstructorInvoker constructorInvoker;

  @BeforeEach
  public void setUp() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
    description = new TestDescription("Jedi");
    factory = (ShouldBeEqual) shouldBeEqual("Luke", "Yoda", STANDARD_REPRESENTATION);
    constructorInvoker = mock(ConstructorInvoker.class);
    factory.constructorInvoker = constructorInvoker;
  }

  @Test
  void should_create_AssertionError_if_created_ComparisonFailure_and_AssertionFailedError_is_null() throws Exception {
    // GIVEN
    given(constructorInvoker.newInstance(anyString(), any(Class[].class), any(Object[].class))).willReturn(null);
    // WHEN
    AssertionError error = factory.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    check(error);
  }

  @Test
  void should_create_AssertionError_if_error_is_thrown_when_creating_ComparisonFailure() throws Exception {
    // GIVEN
    given(constructorInvoker.newInstance(anyString(), any(Class[].class),
                                         any(Object[].class))).willThrow(new AssertionError("Thrown on purpose"));
    // WHEN
    AssertionError error = factory.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    check(error);
  }

  private void check(AssertionError error) throws Exception {
    verify(constructorInvoker, times(2)).newInstance(AssertionFailedError.class.getName(),
                                                     array(String.class, Object.class, Object.class),
                                                     format("[Jedi] %n" +
                                                            "expected: \"Yoda\"%n" +
                                                            " but was: \"Luke\""),
                                                     STANDARD_REPRESENTATION.toStringOf("Yoda"),
                                                     STANDARD_REPRESENTATION.toStringOf("Luke"));
    verify(constructorInvoker).newInstance(ComparisonFailure.class.getName(),
                                           new Class<?>[] { String.class, String.class, String.class },
                                           "[Jedi]",
                                           STANDARD_REPRESENTATION.toStringOf("Yoda"),
                                           STANDARD_REPRESENTATION.toStringOf("Luke"));
    assertThat(error).isNotInstanceOfAny(ComparisonFailure.class, AssertionFailedError.class)
                     .hasMessage(format("[Jedi] %n" +
                                        "expected: \"Yoda\"%n" +
                                        " but was: \"Luke\""));
  }
}
