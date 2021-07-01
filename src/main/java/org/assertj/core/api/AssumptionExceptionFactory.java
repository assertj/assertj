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

import static java.lang.String.format;
import static org.assertj.core.configuration.PreferredAssumptionException.AUTO_DETECT;
import static org.assertj.core.configuration.PreferredAssumptionException.JUNIT4;
import static org.assertj.core.configuration.PreferredAssumptionException.OPENTEST4J_JUNIT5;
import static org.assertj.core.configuration.PreferredAssumptionException.TEST_NG;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.configuration.Configuration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.configuration.PreferredAssumptionException;
import org.assertj.core.util.VisibleForTesting;

/**
 * Responsible of building the exception to throw for failing assumptions.
 * @since 3.21.0
 */
public class AssumptionExceptionFactory {

  private static PreferredAssumptionException preferredAssumptionException = Configuration.PREFERRED_ASSUMPTION_EXCEPTION;

  static RuntimeException assumptionNotMet(AssertionError assertionError) throws ReflectiveOperationException {
    Class<?> assumptionExceptionClass = selectAssumptionExceptionClass();
    return buildAssumptionException(assumptionExceptionClass, assertionError);
  }
  
  @VisibleForTesting
  public static PreferredAssumptionException getPreferredAssumptionException() {
    return preferredAssumptionException;
  }

  static void setPreferredAssumptionException(PreferredAssumptionException preferredAssumptionException) {
    ConfigurationProvider.loadRegisteredConfiguration();
    Objects.requireNonNull(preferredAssumptionException, "preferredAssumptionException must not be null");
    AssumptionExceptionFactory.preferredAssumptionException = preferredAssumptionException;
  }

  private static Class<?> selectAssumptionExceptionClass() {
    if (preferredAssumptionException == AUTO_DETECT) return autoDetectAssumptionExceptionClass();
    return Optional.ofNullable(getAssumptionExceptionClass(preferredAssumptionException))
                   .orElseThrow(() -> new IllegalStateException(format("Failed to load %s class, make sure it is available in the classpath.",
                                                                       preferredAssumptionException.getAssumptionExceptionClassName())));
  }

  private static Class<?> autoDetectAssumptionExceptionClass() {
    return Stream.of(TEST_NG, JUNIT4, OPENTEST4J_JUNIT5)
                 .map(AssumptionExceptionFactory::getAssumptionExceptionClass)
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElseThrow(() -> new IllegalStateException("Assumptions require TestNG, JUnit or opentest4j on the classpath"));
  }

  private static Class<?> getAssumptionExceptionClass(PreferredAssumptionException preferredAssumptionException) {
    try {
      return Class.forName(preferredAssumptionException.getAssumptionExceptionClassName());
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
