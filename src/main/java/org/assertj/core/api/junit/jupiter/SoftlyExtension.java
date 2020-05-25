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
package org.assertj.core.api.junit.jupiter;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.support.HierarchyTraversalMode;

/**
 * Extension for JUnit Jupiter that provides support for injecting an instance of {@link SoftAssertions} into a class test {@code SoftAssertions} field.
 * <p>
 * The injection occurs before each test method execution, after each test {@link SoftAssertions#assertAll() assertAll()} is invoked to evaluate all test assertions.
 * <p>
 * A nested test class can provide a {@code SoftAssertions} field when it extends {@code this} extension or can inherit
 * the parent's {@code Soft assertions field}
 * <p>
 * This extension throws an {@code IllegalStateException} if:
 * <ul>
 *   <li>the test class lifecycle is {@link Lifecycle#PER_CLASS} (see explanation below).</li>
 *   <li>multiple {@code SoftAssertions} fields are found</li>
 *   <li>no {@code SoftAssertions} field is found</li>
 * </ul>
 * <p>
 * Detecting multiple {@code SoftAssertions} fields is a best effort at the time of this writing, some cases are not detected.
 * <p>
 * <strong>Limitations:</strong>
 * <ol>
 *   <li>Cannot be used with test context that have {@link Lifecycle#PER_CLASS PER_CLASS} life cycle as the same {@code SoftAssertions} would be reused between tests.</li>
 *   <li>May exhibit unpredictable behaviour in concurrent test execution</li>
 * </ol>
 * <p>
 * If you hit such limitations, consider using {@link SoftAssertionsExtension} instead.
 * <p>
 * Example:
 * <pre><code> {@literal @}ExtendWith(SoftlyExtension.class)
 * public class SoftlyExtensionExample {
 *
 *   // initialized by the SoftlyExtension extension
 *   private SoftAssertions soft;
 *
 *   {@literal @}Test
 *   public void chained_soft_assertions_example() {
 *     String name = "Michael Jordan - Bulls";
 *     soft.assertThat(name)
 *         .startsWith("Mi")
 *         .contains("Bulls");
 *     // no need to call softly.assertAll(), this is done by the extension
 *   }
 *
 *   // nested classes test work too
 *   {@literal @}Nested
 *   class NestedExample {
 *
 *     {@literal @}Test
 *     public void football_assertions_example() {
 *       String kylian = "Kylian Mbappé";
 *       soft.assertThat(kylian)
 *           .startsWith("Ky")
 *           .contains("bap");
 *       // no need to call softly.assertAll(), this is done by the extension
 *     }
 *   }
 * } </code></pre>
 * @author Arthur Mita
 **/
@Beta
public class SoftlyExtension implements AfterTestExecutionCallback, TestInstancePostProcessor {

  private static final Namespace SOFTLY_EXTENSION_NAMESPACE = Namespace.create(SoftlyExtension.class);

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
    if (isPerClassLifeCycle(extensionContext)) {
      throw new IllegalStateException("A SoftAssertions field is not permitted in test classes with PER_CLASS life cycle as the instance would be collecting all class tests errors (instead of per test errors). "
                                      + "Consider using {@link SoftAssertionsExtension} instead which does not have such limitation.");
    }
    // this is called multiple times depending on the test hierarchy
    // we store the field against the ExtensionContext where we found it
    initSoftAssertionsField(testInstance).ifPresent(softAssertions -> getStore(extensionContext).put(SoftlyExtension.class,
                                                                                                     softAssertions));
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    SoftAssertions softAssertions = getStore(extensionContext).remove(SoftlyExtension.class, SoftAssertions.class);
    Optional<ExtensionContext> currentContext = Optional.of(extensionContext);
    // try to find SoftAssertions in the hierarchy of ExtensionContexts starting with the current one.
    while (softAssertions == null && getParent(currentContext).isPresent()) {
      softAssertions = getParent(currentContext).map(context -> getStore(context).remove(SoftlyExtension.class,
                                                                                         SoftAssertions.class))
                                                .orElse(null);
      currentContext = getParent(currentContext);
    }
    if (softAssertions == null) throw new IllegalStateException("No SoftlyExtension field found");
    softAssertions.assertAll();
  }

  private static Optional<ExtensionContext> getParent(Optional<ExtensionContext> currentContext) {
    return currentContext.flatMap(ExtensionContext::getParent);
  }

  private static boolean isPerClassLifeCycle(ExtensionContext methodExtensionContext) {
    return methodExtensionContext.getTestInstanceLifecycle()
                                 .map(lifecycle -> lifecycle == PER_CLASS)
                                 .orElse(false);
  }

  private static Optional<SoftAssertions> initSoftAssertionsField(Object testInstance) throws IllegalAccessException {
    // find SoftAssertions fields in the test class hierarchy
    Collection<Field> softAssertionsFields = findFields(testInstance.getClass(),
                                                        field -> field.getType() == SoftAssertions.class,
                                                        HierarchyTraversalMode.BOTTOM_UP);
    if (softAssertionsFields.isEmpty()) return Optional.empty();
    checkTooManySoftAssertionsFields(softAssertionsFields);

    Field softAssertionsField = softAssertionsFields.iterator().next();
    softAssertionsField.setAccessible(true);
    SoftAssertions softAssertions = new SoftAssertions();
    softAssertionsField.set(testInstance, softAssertions);
    return Optional.of(softAssertions);
  }

  private static void checkTooManySoftAssertionsFields(Collection<Field> softAssertionsFields) {
    if (softAssertionsFields.size() > 1)
      throw new IllegalStateException("Only one field of type " + SoftAssertions.class.getName() + " should be defined but found "
                                      + softAssertionsFields.size() + " : " + softAssertionsFields);
  }

  private static Store getStore(ExtensionContext extensionContext) {
    return extensionContext.getStore(SOFTLY_EXTENSION_NAMESPACE);
  }
}
