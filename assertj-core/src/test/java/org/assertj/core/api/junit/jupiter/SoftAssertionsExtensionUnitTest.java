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
package org.assertj.core.api.junit.jupiter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

/**
 * Unit tests for {@link SoftAssertionsExtension}.
 *
 * @author Sam Brannen
 * @see SoftAssertionsExtensionIntegrationTest
 * @see BDDSoftAssertionsExtensionIntegrationTest
 * @since 3.13
 */
@DisplayName("JUnit Jupiter Soft Assertions extension")
class SoftAssertionsExtensionUnitTest {

  private final SoftAssertionsExtension extension = new SoftAssertionsExtension();
  private final ParameterContext parameterContext = mock(ParameterContext.class);
  private final ExtensionContext extensionContext = mock(ExtensionContext.class);

  @Test
  void supports_soft_assertions() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("softAssertions", SoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    boolean supportsParameter = extension.supportsParameter(parameterContext, extensionContext);
    // THEN
    assertThat(supportsParameter).isTrue();
  }

  @Test
  void supports_bdd_soft_assertions() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("bddSoftAssertions", BDDSoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    boolean supportsParameter = extension.supportsParameter(parameterContext, extensionContext);
    // THEN
    assertThat(supportsParameter).isTrue();
  }

  @Test
  void supports_custom_soft_assertions() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("customSoftAssertions", MySoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    boolean supportsParameter = extension.supportsParameter(parameterContext, extensionContext);
    // THEN
    assertThat(supportsParameter).isTrue();
  }

  @Test
  void does_not_support_string() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("string", String.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    boolean supportsParameter = extension.supportsParameter(parameterContext, extensionContext);
    // THEN
    assertThat(supportsParameter).isFalse();
  }

  @Test
  void does_not_support_abstract_soft_assertions() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("abstractCustomSoftAssertions", MyAbstractSoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    Throwable exception = catchThrowable(() -> extension.supportsParameter(parameterContext, extensionContext));
    // THEN
    assertThat(exception).isInstanceOf(ParameterResolutionException.class)
                         .hasMessageStartingWith("Configuration error: the resolved SoftAssertionsProvider implementation [%s] is abstract and cannot be instantiated",
                                                 executable);
  }

  @Test
  void does_not_support_soft_assertions_with_no_default_constructor() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("noDefaultConstructorCustomSoftAssertions",
                                                    MyNoDefaultConstructorSoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    Throwable exception = catchThrowable(() -> extension.supportsParameter(parameterContext, extensionContext));
    // THEN
    assertThat(exception).isInstanceOf(ParameterResolutionException.class)
                         .hasMessageStartingWith("Configuration error: the resolved SoftAssertionsProvider implementation [%s] has no default constructor and cannot be instantiated",
                                                 executable);
  }

  @Test
  void does_not_support_constructor() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getDeclaredConstructor(SoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    Throwable exception = catchThrowable(() -> extension.supportsParameter(parameterContext, extensionContext));
    // THEN
    assertThat(exception).isInstanceOf(ParameterResolutionException.class)
                         .hasMessageStartingWith("Configuration error: cannot resolve SoftAssertionsProvider instances for");
  }

  @Test
  void does_not_support_lifecycle_method() throws Exception {
    // GIVEN
    Executable executable = MyTests.class.getMethod("beforeEach", SoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    Throwable exception = catchThrowable(() -> extension.supportsParameter(parameterContext, extensionContext));
    // THEN
    assertThat(exception).isInstanceOf(ParameterResolutionException.class)
                         .hasMessageStartingWith("Configuration error: cannot resolve SoftAssertionsProvider instances for")
                         .hasMessageContaining("beforeEach");
  }

  private static abstract class MyAbstractSoftAssertions implements SoftAssertionsProvider {
  }

  private static class MyNoDefaultConstructorSoftAssertions extends AbstractSoftAssertions {
    @SuppressWarnings("unused")
    public MyNoDefaultConstructorSoftAssertions(String arg) {}
  }

  private static class MySoftAssertions extends AbstractSoftAssertions {
  }

  // -------------------------------------------------------------------------

  @SuppressWarnings("unused")
  private static class MyTests {

    public MyTests(SoftAssertions softly) {}

    @BeforeEach
    public void beforeEach(SoftAssertions softly) {}

    @Test
    public void softAssertions(SoftAssertions softly) {}

    @Test
    public void bddSoftAssertions(BDDSoftAssertions softly) {}

    @Test
    public void customSoftAssertions(MySoftAssertions softly) {}

    @Test
    public void abstractCustomSoftAssertions(MyAbstractSoftAssertions softly) {}

    @Test
    public void noDefaultConstructorCustomSoftAssertions(MyNoDefaultConstructorSoftAssertions softly) {}

    @Test
    public void string(String text) {}
  }

}
