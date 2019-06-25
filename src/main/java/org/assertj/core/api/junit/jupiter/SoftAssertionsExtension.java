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

import static java.lang.String.format;
import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.function.Consumer;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.annotation.Testable;

/**
 * Extension for JUnit Jupiter that provides support for injecting an instance
 * of {@link SoftAssertions} or {@link BDDSoftAssertions} into test methods.
 *
 * <h3>Applicability</h3>
 *
 * <p>In this context, the term "test method" refers to any method annotated with
 * {@code @Test}, {@code @RepeatedTest}, {@code @ParameterizedTest},
 * {@code @TestFactory}, or {@code @TestTemplate}.<br>
 * This extension does not inject {@code SoftAssertions} or {@code BDDSoftAssertions} arguments into test
 * constructors or lifecycle methods.
 *
 * <h3>Scope</h3>
 *
 * <p>The scope of the {@code SoftAssertions} or {@code BDDSoftAssertions} instance
 * managed by this extension begins when a parameter of type {@code SoftAssertions}
 * or {@code BDDSoftAssertions} is resolved for a test method.<br>
 * The scope of the instance ends after the test method has been executed, this is when
 * {@code assertAll()} will be invoked on the instance to verify that no soft assertions failed.
 *
 * <h3>Example with {@code SoftAssertions}</h3>
 *
 * <pre><code class='java'> {@literal @}ExtendWith(SoftAssertionsExtension.class)
 * class ExampleTestCase {
 *
 *    {@literal @}Test
 *    void multipleFailures(SoftAssertions softly) {
 *       softly.assertThat(2 * 3).isEqualTo(0);
 *       softly.assertThat(Arrays.asList(1, 2)).containsOnly(1);
 *       softly.assertThat(1 + 1).isEqualTo(2);
 *    }
 * }</code></pre>
 *
 * <h3>Example with {@code BDDSoftAssertions}</h3>
 *
 * <pre><code class='java'> {@literal @}ExtendWith(SoftAssertionsExtension.class)
 * class ExampleTestCase {
 *
 *    {@literal @}Test
 *    void multipleFailures(BDDSoftAssertions softly) {
 *       softly.then(2 * 3).isEqualTo(0);
 *       softly.then(Arrays.asList(1, 2)).containsOnly(1);
 *       softly.then(1 + 1).isEqualTo(2);
 *    }
 * }</code></pre>
 *
 * @author Sam Brannen
 * @since 3.13
 */
public class SoftAssertionsExtension implements ParameterResolver, AfterTestExecutionCallback {

  private static final Namespace SOFT_ASSERTIONS_EXTENSION_NAMESPACE = Namespace.create(SoftAssertionsExtension.class);

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    // Abort if parameter type is unsupported.
    if (isUnsupportedParameterType(parameterContext.getParameter())) return false;

    Executable executable = parameterContext.getDeclaringExecutable();
    // @Testable is used as a meta-annotation on @Test, @TestFactory, @TestTemplate, etc.
    boolean isTestableMethod = executable instanceof Method && isAnnotated(executable, Testable.class);
    if (!isTestableMethod) {
      throw new ParameterResolutionException(format("Configuration error: cannot resolve SoftAssertions or BDDSoftAssertions for [%s]. Only test methods are supported.",
                                                    executable));
    }
    return true;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    // The parameter type is guaranteed to be either SoftAssertions or BDDSoftAssertions
    return getStore(extensionContext).getOrComputeIfAbsent(parameterContext.getParameter().getType());
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) {
    assertAll(extensionContext, SoftAssertions.class, SoftAssertions::assertAll);
    assertAll(extensionContext, BDDSoftAssertions.class, BDDSoftAssertions::assertAll);
  }

  /*
   * Invoke {@code assertAll} on the object of specified {@code type}, if the
   * supplied {@code ExtensionContext} contains the object keyed by its type.
   */
  private static <T> void assertAll(ExtensionContext extensionContext, Class<T> type, Consumer<T> assertAll) {
    Optional.ofNullable(getStore(extensionContext).remove(type, type)).ifPresent(assertAll);
  }

  private static boolean isUnsupportedParameterType(Parameter parameter) {
    Class<?> type = parameter.getType();
    return type != SoftAssertions.class && type != BDDSoftAssertions.class;
  }

  private static Store getStore(ExtensionContext extensionContext) {
    return extensionContext.getStore(SOFT_ASSERTIONS_EXTENSION_NAMESPACE);
  }

}
