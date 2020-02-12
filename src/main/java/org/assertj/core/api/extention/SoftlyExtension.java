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
package org.assertj.core.api.extention;

import org.assertj.core.annotations.Softly;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;

/**
 * <p>Injects an instance of {@link SoftAssertions} to any Field of type
 * {@code SoftAssertions} that has the annotation {@link Softly}
 * before each test method execution.
 * {@link SoftAssertions#assertAll()} executes after each test
 * method only when {@link Softly#assertAfter()} return {@code true}
 *</p>
 *
 * <p>
 * Inspired by Mockito @Captor annotation.
 * </p>
 **/
public class SoftlyExtension implements TestInstancePostProcessor, BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = Logger.getLogger(SoftlyExtension.class.getName());
  private static final Namespace ASSERT_J = Namespace.create("org.assertj.core");
  private static final String TEST_INSTANCE = "testInstance";
  private final List<Field> fields = new ArrayList<>();

  private Failures failures = Failures.instance();


  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
    extensionContext.getStore(ASSERT_J).put(TEST_INSTANCE, testInstance);
  }

  @Override
  public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
    Object testInstance = extensionContext.getStore(ASSERT_J).get(TEST_INSTANCE);
    LOGGER.log(Level.INFO,
      "\n==========================================================" +
        "\nGetting Getting SoftAssertion Field annotated with @Softly" +
        "\n==========================================================");

    List<Field> softlyFields = AnnotationSupport.findAnnotatedFields(testInstance.getClass(), Softly.class);

    if (softlyFields.isEmpty()) {
      LOGGER.log(Level.WARNING,
        "\n======================================" +
          "\nNo instance of soft assertions detected" +
          "\n======================================");
      return;
    }

    for (Field field : softlyFields) {
      if (field.getType() != SoftAssertions.class) {
        throw failures.failure("@Softly field must be of the type " + SoftAssertions.class.getName());
      }
      field.setAccessible(true);
      field.set(testInstance, new SoftAssertions());

      fields.add(field);
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    Object testInstance = extensionContext.getStore(ASSERT_J).get(TEST_INSTANCE);
    for (Field softlyField : fields) {
      SoftAssertions softly = (SoftAssertions) softlyField.get(testInstance);
      Softly softlyAnnotation = softlyField.getAnnotation(Softly.class);

      if (softlyAnnotation.assertAfter()) {
        softly.assertAll();
      }
    }
  }
}
