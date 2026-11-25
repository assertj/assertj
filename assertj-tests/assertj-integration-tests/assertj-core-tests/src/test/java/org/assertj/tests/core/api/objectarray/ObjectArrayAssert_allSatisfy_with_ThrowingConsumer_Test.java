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
package org.assertj.tests.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_allSatisfy_with_ThrowingConsumer_Test {

  @Test
  void should_pass_if_all_elements_satisfy_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String> requirements = s -> {
      if (s.length() != 4) throw new IOException("unexpected length");
      assertThat(s).doesNotContain("V");
    };
    // WHEN/THEN
    then(actual).allSatisfy(requirements);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    ThrowingConsumer<String> requirements = s -> assertThat(s).isNotNull();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Yoda" };
    ThrowingConsumer<String> requirements = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    then(exception).hasMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    String[] emptyArray = {};
    ThrowingConsumer<String> impossibleRequirements = s -> assertThat(s).contains("error").doesNotContain("error");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(emptyArray).allSatisfy(impossibleRequirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_if_some_elements_do_not_satisfy_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String> requirements = s -> assertThat(s).startsWith("L");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(assertionError).hasMessageStartingWith("%nExpecting all elements of:%n  [\"Luke\", \"Leia\", \"Yoda\"]%nto satisfy given requirements, but these elements did not:%n%n".formatted())
                        .hasMessageContaining(shouldStartWith("Yoda", "L").create());
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    String[] actual = { "foo" };
    Throwable exception = new Throwable("boom!");
    ThrowingConsumer<String> requirements = s -> {
      throw exception;
    };
    // WHEN
    var throwable = catchThrowable(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    String[] actual = { "foo" };
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> requirements = s -> {
      throw runtimeException;
    };
    // WHEN
    var throwable = catchThrowable(() -> assertThat(actual).allSatisfy(requirements));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    ThrowingConsumer<String> requirements = s -> assertThat(s).startsWith("Lu");
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .allSatisfy(requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
