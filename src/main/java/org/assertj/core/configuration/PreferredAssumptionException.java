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
package org.assertj.core.configuration;

import static java.lang.String.format;

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
  OPENTEST4J_JUNIT5("org.opentest4j.TestAbortedException"),
  /**
   * AssertJ will try to build the exception to throw in this order:
   * <ol>
   * <li>{@code org.testng.SkipException} for TestNG (if available in the classpath)</li>
   * <li>{@code org.junit.AssumptionViolatedException} for JUnit4 (if available in the classpath)</li>
   * <li>{@code org.opentest4j.TestAbortedException} for JUnit5</li>
   * </ol> 
   * If none are available, AssertJ throw an {@link IllegalStateException}.
   */
  AUTO_DETECT("try in order org.testng.SkipException, org.junit.AssumptionViolatedException and org.opentest4j.TestAbortedException");

  private PreferredAssumptionException(String assumptionException) {
    this.assumptionExceptionClassName = assumptionException;
  }

  private final String assumptionExceptionClassName;
  
  public String getAssumptionExceptionClassName() {
    return assumptionExceptionClassName;
  }
  
  @Override
  public String toString() {
    return format("%s(%s)", name(), assumptionExceptionClassName) ;
  }
  
}
