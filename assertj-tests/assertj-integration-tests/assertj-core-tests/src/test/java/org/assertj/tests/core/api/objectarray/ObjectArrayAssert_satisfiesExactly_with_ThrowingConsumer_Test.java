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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_satisfiesExactly_with_ThrowingConsumer_Test {

  @Test
  void should_pass_if_each_element_satisfies_its_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String> requirements1 = name -> {
      if (name.isBlank()) throw new IOException("blank name");
    };
    ThrowingConsumer<String> requirements2 = name -> assertThat(name).startsWith("Lei");
    ThrowingConsumer<String> requirements3 = name -> assertThat(name).endsWith("da");
    // WHEN/THEN
    then(actual).satisfiesExactly(requirements1, requirements2, requirements3);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    ThrowingConsumer<String> requirements = name -> assertThat(name).isNotNull();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_requirements_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String>[] requirements = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage("%nExpecting an array but was: null".formatted());
  }

  @Test
  void should_fail_if_some_elements_do_not_satisfy_their_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String> requirements1 = name -> assertThat(name).isNotBlank();
    ThrowingConsumer<String> requirements2 = name -> assertThat(name).startsWith("Han");
    ThrowingConsumer<String> requirements3 = name -> assertThat(name).endsWith("da");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements1, requirements2,
                                                                                        requirements3));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(assertionError).hasMessageStartingWith("%nExpecting each element of:%n  [\"Luke\", \"Leia\", \"Yoda\"]%nto satisfy the requirements at its index, but these elements did not:%n%n".formatted())
                        .hasMessageContaining(shouldStartWith("Leia", "Han").create());
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
    var throwable = catchThrowable(() -> assertThat(actual).satisfiesExactly(requirements));
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
    var throwable = catchThrowable(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    ThrowingConsumer<Object> requirements = element -> assertThat(element).isNotNull();
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .satisfiesExactly(requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
