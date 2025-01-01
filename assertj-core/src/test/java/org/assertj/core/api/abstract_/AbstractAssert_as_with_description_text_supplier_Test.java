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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.assertj.core.api.ConcreteAssert;
import org.junit.jupiter.api.Test;

class AbstractAssert_as_with_description_text_supplier_Test {

  @Test
  void descriptionText_should_evaluate_lazy_description() {
    // GIVEN
    ConcreteAssert assertions = new ConcreteAssert("foo");
    // WHEN
    assertions.as(() -> "description");
    // THEN
    then(assertions.descriptionText()).isEqualTo("description");
  }

  @Test
  void should_not_evaluate_description_when_assertion_succeeds() {
    // GIVEN
    final AtomicBoolean evaluated = new AtomicBoolean(false);
    Supplier<String> descriptionSupplier = spiedSupplier(evaluated);
    // WHEN
    assertThat(true).as(descriptionSupplier).isTrue();
    // THEN
    then(evaluated).isFalse();
  }

  @Test
  void should_evaluate_description_when_assertion_fails() {
    // GIVEN
    final AtomicBoolean evaluated = new AtomicBoolean(false);
    Supplier<String> descriptionSupplier = spiedSupplier(evaluated);
    // WHEN
    expectAssertionError(() -> assertThat(true).as(descriptionSupplier).isFalse());
    // THEN
    then(evaluated).isTrue();
  }

  @Test
  void should_return_this() {
    // GIVEN
    ConcreteAssert assertions = new ConcreteAssert("foo");
    // WHEN
    ConcreteAssert returnedAssertions = assertions.as(() -> "description");
    // THEN
    then(returnedAssertions).isSameAs(assertions);
  }

  @Test
  void should_throw_evaluate_lazy_description() {
    // GIVEN
    ConcreteAssert assertions = new ConcreteAssert("foo");
    Supplier<String> descriptionSupplier = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertions.as(descriptionSupplier).descriptionText());
    // THEN
    then(throwable).isInstanceOf(IllegalStateException.class)
                   .hasMessage("the descriptionSupplier should not be null");
  }

  private static Supplier<String> spiedSupplier(final AtomicBoolean evaluated) {
    return () -> {
      evaluated.set(true);
      return "description";
    };
  }

}
