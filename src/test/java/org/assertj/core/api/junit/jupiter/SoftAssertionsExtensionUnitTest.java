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

package org.assertj.core.api.junit.jupiter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

/**
 * Unit tests for {@link SoftAssertionsExtension}.
 *
 * @author Sam Brannen
 * @since 3.13
 * @see SoftAssertionsExtensionIntegrationTest
 * @see BDDSoftAssertionsExtensionIntegrationTest
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName(value = "JUnit 5 Soft Assertions extension")
class SoftAssertionsExtensionUnitTest {

  private final SoftAssertionsExtension extension = new SoftAssertionsExtension();
  private final ParameterContext parameterContext = mock(ParameterContext.class);
  private final ExtensionContext extensionContext = mock(ExtensionContext.class);

  @Test
  void supports_soft_assertions() throws Exception {
    // GIVEN
    Executable executable = TestCase.class.getMethod("softAssertions", SoftAssertions.class);
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
    Executable executable = TestCase.class.getMethod("bddSoftAssertions", BDDSoftAssertions.class);
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
    Executable executable = TestCase.class.getMethod("string", String.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    boolean supportsParameter = extension.supportsParameter(parameterContext, extensionContext);
    // THEN
    assertThat(supportsParameter).isFalse();
  }

  @Test
  void does_not_support_constructor() throws Exception {
    // GIVEN
    Executable executable = TestCase.class.getDeclaredConstructor(SoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    Throwable exception = catchThrowable(() -> extension.supportsParameter(parameterContext, extensionContext));
    // THEN
    assertThat(exception).isInstanceOf(ParameterResolutionException.class)
                         .hasMessageStartingWith("Configuration error: cannot resolve SoftAssertions or BDDSoftAssertions for");
  }

  @Test
  void does_not_support_lifecycle_method() throws Exception {
    // GIVEN
    Executable executable = TestCase.class.getMethod("beforeEach", SoftAssertions.class);
    Parameter parameter = executable.getParameters()[0];
    given(parameterContext.getParameter()).willReturn(parameter);
    given(parameterContext.getDeclaringExecutable()).willReturn(executable);
    // WHEN
    Throwable exception = catchThrowable(() -> extension.supportsParameter(parameterContext, extensionContext));
    // THEN
    assertThat(exception).isInstanceOf(ParameterResolutionException.class)
                         .hasMessageStartingWith("Configuration error: cannot resolve SoftAssertions or BDDSoftAssertions for");
  }

  // -------------------------------------------------------------------------

  @SuppressWarnings("unused")
  private static class TestCase {

    public TestCase(SoftAssertions softly) {}

    @BeforeEach
    public void beforeEach(SoftAssertions softly) {}

    @Test
    public void softAssertions(SoftAssertions softly) {}

    @Test
    public void bddSoftAssertions(BDDSoftAssertions softly) {}

    @Test
    public void string(String text) {}
  }

}
