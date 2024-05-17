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
 * Copyright 2012-2024 the original author or authors.
 */
package org.example.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setRemoveAssertJRelatedElementsFromStackTrace;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.StackTraceUtils.checkNoAssertjStackTraceElementIn;

import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class Remove_assertJ_stacktrace_elements_Test {

  private boolean initialRemoveAssertJRelatedElementsFromStackTraceValue;

  @BeforeEach
  public void beforeTest() {
    initialRemoveAssertJRelatedElementsFromStackTraceValue = Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace();
    setRemoveAssertJRelatedElementsFromStackTrace(true);
  }

  @AfterEach
  public void afterTest() {
    setRemoveAssertJRelatedElementsFromStackTrace(initialRemoveAssertJRelatedElementsFromStackTraceValue);
  }

  @ParameterizedTest
  @MethodSource
  void stacktrace_should_not_include_assertj_elements_nor_elements_coming_from_assertj(ThrowingCallable throwingCallable) {
    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);
    // THEN
    checkNoAssertjStackTraceElementIn(assertionError);
    // since we remove assertj elements, there is no easy way to check we have removed elements before/above assertj
    // -> we check that the first element is the test class itself.
    then(assertionError.getStackTrace()[0].toString()).contains("Remove_assertJ_stacktrace_elements_Test");

  }

  static Stream<ThrowingCallable> stacktrace_should_not_include_assertj_elements_nor_elements_coming_from_assertj() {
    return Stream.of(() -> assertThat(0).isEqualTo(1),
                     () -> assertThat(0).satisfies(x -> assertThat(x).isEqualTo(1)));
  }

}
