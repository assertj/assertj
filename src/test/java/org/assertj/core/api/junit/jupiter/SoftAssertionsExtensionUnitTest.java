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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
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
class SoftAssertionsExtensionUnitTest {

	private final SoftAssertionsExtension extension = new SoftAssertionsExtension();
	private final ParameterContext parameterContext = mock(ParameterContext.class);
	private final ExtensionContext extensionContext = mock(ExtensionContext.class);

	@Test
	void supportsSoftAssertions() throws Exception {
		Executable executable = TestCase.class.getMethod("softAssertions", SoftAssertions.class);
		Parameter parameter = executable.getParameters()[0];
		when(parameterContext.getParameter()).thenReturn(parameter);
		when(parameterContext.getDeclaringExecutable()).thenReturn(executable);
		assertThat(extension.supportsParameter(parameterContext, extensionContext)).isTrue();
	}

	@Test
	void supportsBddSoftAssertions() throws Exception {
		Executable executable = TestCase.class.getMethod("bddSoftAssertions", BDDSoftAssertions.class);
		Parameter parameter = executable.getParameters()[0];
		when(parameterContext.getParameter()).thenReturn(parameter);
		when(parameterContext.getDeclaringExecutable()).thenReturn(executable);
		assertThat(extension.supportsParameter(parameterContext, extensionContext)).isTrue();
	}

	@Test
	void doesNotSupportString() throws Exception {
		Executable executable = TestCase.class.getMethod("string", String.class);
		Parameter parameter = executable.getParameters()[0];
		when(parameterContext.getParameter()).thenReturn(parameter);
		when(parameterContext.getDeclaringExecutable()).thenReturn(executable);
		assertThat(extension.supportsParameter(parameterContext, extensionContext)).isFalse();
	}

	@Test
	void doesNotSupportConstructor() throws Exception {
		Executable executable = TestCase.class.getDeclaredConstructor(SoftAssertions.class);
		Parameter parameter = executable.getParameters()[0];
		when(parameterContext.getParameter()).thenReturn(parameter);
		when(parameterContext.getDeclaringExecutable()).thenReturn(executable);
		assertThatExceptionOfType(ParameterResolutionException.class)
			.isThrownBy(() -> extension.supportsParameter(parameterContext, extensionContext))
			.withMessageStartingWith("Configuration error: cannot resolve SoftAssertions or BDDSoftAssertions for");
	}

	@Test
	void doesNotSupportLifecycleMethod() throws Exception {
		Executable executable = TestCase.class.getMethod("beforeEach", SoftAssertions.class);
		Parameter parameter = executable.getParameters()[0];
		when(parameterContext.getParameter()).thenReturn(parameter);
		when(parameterContext.getDeclaringExecutable()).thenReturn(executable);
		assertThatExceptionOfType(ParameterResolutionException.class)
			.isThrownBy(() -> extension.supportsParameter(parameterContext, extensionContext))
			.withMessageStartingWith("Configuration error: cannot resolve SoftAssertions or BDDSoftAssertions for");
	}

	// -------------------------------------------------------------------------

	private static class TestCase {

		@SuppressWarnings("unused")
		public TestCase(SoftAssertions softly) {
		}

		@BeforeEach
		public void beforeEach(SoftAssertions softly) {
		}

		@Test
		public void softAssertions(SoftAssertions softly) {
		}

		@Test
		public void bddSoftAssertions(BDDSoftAssertions softly) {
		}

		@Test
		public void string(String text) {
		}
	}

}
