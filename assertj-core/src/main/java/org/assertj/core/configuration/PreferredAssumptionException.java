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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.configuration;

import static java.lang.String.format;

import java.util.Optional;
import java.util.stream.Stream;

public enum PreferredAssumptionException {

  /**
   * {@code org.testng.SkipException} - works with TestNG
   */
  TEST_NG("org.testng.SkipException"),
  /**
   * {@code org.junit.AssumptionViolatedException} - works with JUnit 4
   */
  JUNIT4("org.junit.AssumptionViolatedException"),
  /**
   * {@code org.opentest4j.TestAbortedException} - works with JUnit 5 
   */
  JUNIT5("org.opentest4j.TestAbortedException"),
  /**
   * AssertJ will try to build the exception to throw in this order:
   * <ol>
   * <li>{@code org.testng.SkipException} for TestNG (if available in the classpath)</li>
   * <li>{@code org.junit.AssumptionViolatedException} for JUnit 4 (if available in the classpath)</li>
   * <li>{@code org.opentest4j.TestAbortedException} for JUnit 5</li>
   * </ol> 
   * If none are available, AssertJ throws an {@link IllegalStateException}.
   */
  AUTO_DETECT(null) {

    @Override
    public Class<?> getAssumptionExceptionClass() {
      return autoDetectAssumptionExceptionClass();
    }

    private Class<?> autoDetectAssumptionExceptionClass() {
      return Stream.of(TEST_NG, JUNIT4, JUNIT5)
                   .map(PreferredAssumptionException::loadAssumptionExceptionClass)
                   .flatMap(optional -> optional.map(Stream::of).orElse(Stream.empty()))
                   .findFirst()
                   .orElseThrow(() -> new IllegalStateException("Assumptions require TestNG, JUnit 4 or opentest4j on the classpath"));
    }

    @Override
    public String toString() {
      return format("%s(%s)", name(),
                    "try in order org.testng.SkipException, org.junit.AssumptionViolatedException and org.opentest4j.TestAbortedException");
    }

  };

  private final String assumptionExceptionClassName;

  PreferredAssumptionException(String assumptionExceptionClassName) {
    this.assumptionExceptionClassName = assumptionExceptionClassName;
  }

  public Class<?> getAssumptionExceptionClass() {
    return loadAssumptionExceptionClass().orElseThrow(this::assumptionExceptionClassNotFound);
  }

  private Optional<Class<?>> loadAssumptionExceptionClass() {
    try {
      return Optional.of(Class.forName(assumptionExceptionClassName));
    } catch (ClassNotFoundException e) {
      return Optional.empty();
    }
  }

  private IllegalStateException assumptionExceptionClassNotFound() {
    return new IllegalStateException(format("Failed to load %s class, make sure it is available in the classpath.",
                                            assumptionExceptionClassName));
  }

  @Override
  public String toString() {
    return format("%s(%s)", name(), assumptionExceptionClassName);
  }

}
