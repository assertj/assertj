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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.extention;

import org.assertj.core.annotations.Softly;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;

/**
 * <p>
 * Injects an instance of {@link SoftAssertions} to any Field of type
 * {@code SoftAssertions} that has the annotation {@link Softly @softly}
 * before each test method execution and invokes
 * {@link SoftAssertions#assertAll() assert all} after the test method execution.
 * </p>
 * <p>
 * This extension will throw a {@link Failures#failure(String) failure}
 * when more that one filed is annotated with {@link Softly @softly}.
 * </p>
 * <p>
 * A nested test class can provide a {@code SoftAssertions}
 * field annotated with  {@link Softly @softly}  when it extends
 * {@link SoftlyExtension this} extension or it can use
 * the outer class field.
 * </p>
 * <br>
 * <strong>Limitations</strong>
 * <ol>
 *   <li>Cannot be used with test context that have {@link Lifecycle#PER_CLASS PER_CLASS} life cycle</li>
 *   <li>May exhibit unpredictable behaviour in concurrent test execution</li>
 * </ol>
 * <p>
 * In order to mitigate the lifecycle issue a {@link Failures#failure(String) failure}
 * will be thrown when the extension is used in test contexts that has
 * {@link Lifecycle#PER_CLASS PER_CLASS} life cycle.
 * </p>
 * <br>
 * <p>
 * Inspired by Mockito's {@code @Captor} annotation.
 * </p>
 *
 * @author Arthur Mita
 **/
public class SoftlyExtension implements TestInstancePostProcessor, BeforeEachCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = Logger.getLogger(SoftlyExtension.class.getName());
  private static final Namespace ASSERTJ_CORE = Namespace.create("org.assertj.core");
  private static final String TEST_INSTANCE = "testInstance";
  private final Set<SoftAssertions> softlies = new HashSet<>();

  private Failures failures = Failures.instance();

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
    extensionContext.getStore(ASSERTJ_CORE).put(TEST_INSTANCE, testInstance);
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Optional<ExtensionContext> currentContext = Optional.of(extensionContext);

    while (currentContext.isPresent()) {
      ExtensionContext context = currentContext.get();
      Object testInstance = context.getStore(ASSERTJ_CORE).remove(TEST_INSTANCE);

      if (testInstance != null) {

        List<Field> softlyFields = AnnotationSupport.findAnnotatedFields(testInstance.getClass(), Softly.class);

        if (!softlyFields.isEmpty()) {

          if (softlyFields.size() > 1) {
            throw failures.failure("Only one field of type " + SoftAssertions.class.getName() + " can have the annotation @Softly");
          }

          if (context.getTestInstanceLifecycle().isPresent() && context.getTestInstanceLifecycle().get() == PER_CLASS) {
            throw failures.failure("@Softly annotation not permitted in test class with life cycle PER_CLASS");
          }

          Field softlyField = softlyFields.get(0);
          if (softlyField.getType() != SoftAssertions.class) {
            throw failures.failure("@Softly field must be of the type " + SoftAssertions.class.getName());
          }
          softlyField.setAccessible(true);
          softlyField.set(testInstance, new SoftAssertions());

          this.softlies.add((SoftAssertions) softlyField.get(testInstance));
        }
      }
      currentContext = context.getParent();
    }

    if (this.softlies.isEmpty()) {
      LOGGER.log(Level.WARNING,
        "\n======================================" +
          "\nNo instance of soft assertions detected" +
          "\n======================================");
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    this.softlies.forEach(SoftAssertions::assertAll);
  }
}
