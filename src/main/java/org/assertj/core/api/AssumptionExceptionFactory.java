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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Objects;
import java.util.stream.Stream;

class AssumptionExceptionFactory {

  static RuntimeException assumptionNotMet(AssertionError assertionError) throws ReflectiveOperationException {
    Class<?> assumptionExceptionClass = selectAssumptionExceptionClass();
    return buildAssumptionException(assumptionExceptionClass, assertionError);
  }

  private static Class<?> selectAssumptionExceptionClass() {
    return Stream.of("org.testng.SkipException", "org.junit.AssumptionViolatedException", "org.opentest4j.TestAbortedException")
                 .map(AssumptionExceptionFactory::getClass)
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElseThrow(() -> new IllegalStateException("Assumptions require TestNG, JUnit or opentest4j on the classpath"));
  }

  private static Class<?> getClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  private static RuntimeException buildAssumptionException(Class<?> assumptionExceptionClass,
                                                           AssertionError assertionError) throws ReflectiveOperationException {
    return (RuntimeException) assumptionExceptionClass.getConstructor(String.class, Throwable.class)
                                                      .newInstance("assumption was not met due to: "
                                                                   + assertionError.getMessage(), assertionError);
  }

}
