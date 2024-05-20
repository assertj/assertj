/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssumptionExceptionFactory.getPreferredAssumptionException;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.Assumptions.setPreferredAssumptionException;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.configuration.PreferredAssumptionException;
import org.junit.AssumptionViolatedException;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestNG_with_JUnit4_Test {

  private static final PreferredAssumptionException DEFAULT_PREFERRED_ASSUMPTION_EXCEPTION = getPreferredAssumptionException();

  @AfterMethod
  public void afterEachTest() {
    // reset to the default value to avoid side effects on the other tests
    setPreferredAssumptionException(DEFAULT_PREFERRED_ASSUMPTION_EXCEPTION);
  }

  @Test
  public void should_have_TestNG_in_the_classpath() {
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> Class.forName("org.testng.SkipException"));
  }

  @Test
  public void should_have_JUnit_4_in_the_classpath() {
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> Class.forName("org.junit.AssumptionViolatedException"));
  }

  @Test
  public void should_not_have_opentest4j_in_the_classpath() {
    // WHEN/THEN
    assertThatExceptionOfType(ClassNotFoundException.class).isThrownBy(() -> Class.forName("org.opentest4j.TestAbortedException"));
  }

  @Test
  public void should_throw_SkipException_when_assumption_fails() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assumeThat(true).isFalse());
    // THEN
    then(thrown).isInstanceOf(SkipException.class);
  }

  @Test
  public void should_throw_AssumptionViolatedException_when_assumption_fails_if_preferredAssumptionException_is_set_to_JUnit4() {
    // GIVEN
    setPreferredAssumptionException(PreferredAssumptionException.JUNIT4);
    // WHEN
    Throwable thrown = catchThrowable(() -> assumeThat(true).isFalse());
    // THEN
    then(thrown).isInstanceOf(AssumptionViolatedException.class);
  }

}
