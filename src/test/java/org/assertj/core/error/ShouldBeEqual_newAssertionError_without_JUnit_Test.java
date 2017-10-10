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
package org.assertj.core.error;

import org.assertj.core.description.Description;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.*;


/**
 * Tests for <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ShouldBeEqual_newAssertionError_without_JUnit_Test {

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  private Description description;
  private ShouldBeEqual factory;
  private ConstructorInvoker constructorInvoker;

  @Before
  public void setUp() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
    description = new TestDescription("Jedi");
    factory = (ShouldBeEqual) shouldBeEqual("Luke", "Yoda", new StandardRepresentation());
    constructorInvoker = mock(ConstructorInvoker.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    factory.constructorInvoker = constructorInvoker;
  }

  @Test
  public void should_create_AssertionFailedError_if_created_ComparisonFailure_is_null() throws Exception {
    when(createComparisonFailure()).thenReturn(null);
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    check(error);
  }

  @Test
  public void should_create_AssertionFailedError_if_error_is_thrown_when_creating_ComparisonFailure() throws Exception {
    when(createComparisonFailure()).thenThrow(new AssertionError("Thrown on purpose"));
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    check(error);
  }

  private Object createComparisonFailure() throws Exception {
    return createComparisonFailure(constructorInvoker);
  }

  private void check(AssertionError error) throws Exception {
    verify(constructorInvoker)
      .newInstance(ComparisonFailure.class.getName(), new Class<?>[] { String.class, String.class, String.class },
                   "[Jedi]", "\"Yoda\"", "\"Luke\"");
    verify(constructorInvoker)
      .newInstance(AssertionFailedError.class.getName(), new Class<?>[] { String.class, Object.class, Object.class },
                   String.format("[Jedi] %nExpecting:%n <\"Luke\">%nto be equal to:%n <\"Yoda\">%nbut was not."),
                   "Yoda", "Luke");
    assertThat(error).isNotInstanceOf(ComparisonFailure.class)
                     .isInstanceOf(AssertionFailedError.class);
    AssertionFailedError assertionFailedError = (AssertionFailedError) error;
    assertThat(assertionFailedError.getActual().getValue()).isEqualTo("Luke");
    assertThat(assertionFailedError.getExpected().getValue()).isEqualTo("Yoda");
    assertThat(error.getMessage())
        .isEqualTo(String.format("[Jedi] %nExpecting:%n <\"Luke\">%nto be equal to:%n <\"Yoda\">%nbut was not."));
  }

  private static Object createComparisonFailure(ConstructorInvoker invoker) throws Exception {
    return invoker.newInstance(ComparisonFailure.class.getName(), new Class<?>[] { String.class, String.class, String.class },
        array("[Jedi]", "\"Yoda\"", "\"Luke\""));
  }
}
