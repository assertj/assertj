/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CustomAsserts_filter_stacktrace_Test {

  static Stream<ThrowingCallable> stacktrace_should_not_include_assertj_elements_nor_elements_coming_from_assertj() {
    return Stream.of(() -> assertThat(0).isEqualTo(1),
                     () -> assertThat(0).satisfies(x -> assertThat(x).isEqualTo(1)));
  }

  @ParameterizedTest
  @MethodSource
  void stacktrace_should_not_include_assertj_elements_nor_elements_coming_from_assertj(ThrowingCallable throwingCallable) {
    // WHEN
    var assertionError = expectAssertionError(throwingCallable);
    // THEN
    StackTraceElement[] stackTrace = assertionError.getStackTrace();
    then(stackTrace).noneSatisfy(stackTraceElement -> assertThat(stackTraceElement.toString()).contains("org.assertj.core"));
    then(stackTrace[0].toString()).contains("CustomAsserts_filter_stacktrace_Test");
  }

  @Test
  void should_filter_when_custom_assert_fails_with_message() {
    // WHEN
    var assertionError = expectAssertionError(() -> new CustomAssert("").fail());
    // THEN
    then(assertionError.getStackTrace()).areNot(elementOfCustomAssert());
  }

  @Test
  void should_filter_when_custom_assert_fails_with_overridden_message() {
    // WHEN
    var assertionError = expectAssertionError(() -> new CustomAssert("").overridingErrorMessage("overridden message").fail());
    // THEN
    then(assertionError.getStackTrace()).areNot(elementOfCustomAssert());
  }

  @Test
  void should_filter_when_custom_assert_throws_assertion_error() {
    // WHEN
    var assertionError = expectAssertionError(() -> new CustomAssert("").throwAnAssertionError());
    // THEN
    then(assertionError.getStackTrace()).areNot(elementOfCustomAssert());
  }

  @Test
  void should_filter_when_abstract_custom_assert_fails() {
    // WHEN
    var assertionError = expectAssertionError(() -> new CustomAssert("").failInAbstractAssert());
    // THEN
    then(assertionError.getStackTrace()).areNot(elementOfCustomAssert());
  }

  @Test
  void should_not_filter_when_global_remove_option_is_disabled() {
    boolean initialValue = Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace();
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
    try {
      // WHEN
      var assertionError = expectAssertionError(() -> new CustomAssert("").fail());
      // THEN
      then(assertionError.getStackTrace()).areAtLeastOne(elementOfCustomAssert());
    } finally {
      Assertions.setRemoveAssertJRelatedElementsFromStackTrace(initialValue);
    }
  }

  private static Condition<StackTraceElement> elementOfCustomAssert() {
    return new Condition<>("StackTraceElement of " + CustomAssert.class) {
      @Override
      public boolean matches(StackTraceElement value) {
        return value.getClassName().equals(CustomAssert.class.getName());
      }
    };
  }

  private static class CustomAssert extends CustomAbstractAssert<CustomAssert> {

    public CustomAssert(String actual) {
      super(actual, CustomAssert.class);
    }

    public CustomAssert fail() {
      failWithMessage("failing assert");
      return this;
    }

    public CustomAssert throwAnAssertionError() {
      throwAssertionError(new BasicErrorMessageFactory("failing assert"));
      return this;
    }
  }

  private static class CustomAbstractAssert<S extends CustomAbstractAssert<S>> extends AbstractObjectAssert<S, String> {

    protected CustomAbstractAssert(String actual, Class<?> selfType) {
      super(actual, selfType);
    }

    public S failInAbstractAssert() {
      failWithMessage("failing assert");
      return myself;
    }
  }
}
