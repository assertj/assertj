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
package org.assertj.tests.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;
import static org.assertj.tests.core.testkit.NavigationMethodBaseTest.extractObjectField;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.tests.core.testkit.NavigationMethodBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThrowableAssert_suppressedExceptions_Test implements NavigationMethodBaseTest<ThrowableAssert<Throwable>> {

  private final Throwable actual = new Throwable("initial error");

  @BeforeEach
  public final void setUp() {
    Throwable invalidArgException = new IllegalArgumentException("invalid argument");
    Throwable ioException = new IOException("IO error");
    actual.addSuppressed(invalidArgException);
    actual.addSuppressed(ioException);
  }

  @Test
  void should_be_able_to_call_throwable_array_assertions_on_actual_suppressed_exceptions() {
    assertThat(actual).suppressedExceptions()
                      .hasSize(2)
                      .containsExactly(actual.getSuppressed());
  }

  @Test
  void should_be_able_to_return_to_the_initial_throwable_and_chain_throwable_assertions() {
    // noinspection EqualsWithItself
    assertThat(actual).suppressedExceptions()
                      .hasSize(2)
                      .returnToInitialThrowable()
                      .isSameAs(actual)
                      .hasMessage("initial error");
  }

  @Test
  void should_add_navigation_description_if_none_was_set() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).suppressedExceptions().isEmpty());
    // THEN
    then(assertionError).hasMessageContaining("[checking suppressed exceptions]");
  }

  @Test
  void should_ignore_navigation_description_if_a_description_was_set() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).suppressedExceptions().as("my desc").isEmpty());
    // THEN
    then(assertionError).message()
                        .contains("my desc")
                        .doesNotContain("checking suppressed exceptions");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var nullPointerException = catchNullPointerException(() -> assertThat(actual).suppressedExceptions());
    // THEN
    then(nullPointerException).hasMessage("Can not perform assertions on the suppressed exceptions of a null throwable.");
  }

  @Override
  public ThrowableAssert<Throwable> getAssertion() {
    // noinspection unchecked
    return (ThrowableAssert<Throwable>) assertThat(actual);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ThrowableAssert<Throwable> assertion) {
    return assertion.suppressedExceptions();
  }

  @Override
  public void should_keep_existing_assertion_state() { // except description that is set separately
    // GIVEN
    ThrowableAssert<Throwable> underTest = getAssertion().as("description not propagated")
                                                         .withFailMessage("error message")
                                                         .withRepresentation(UNICODE_REPRESENTATION)
                                                         .usingComparator(ALWAYS_EQUALS);
    // WHEN
    AbstractAssert<?, ?> result = invoke_navigation_method(underTest);
    // THEN
    then(result).hasFieldOrPropertyWithValue("objects", extractObjectField(underTest))
                .extracting(AbstractAssert::getWritableAssertionInfo)
                .usingRecursiveComparison().ignoringFields("description")
                .isEqualTo(underTest.info);

  }
}
