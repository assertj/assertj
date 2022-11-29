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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.tests.kotlin

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.BDDAssertions
import org.assertj.core.api.ConcreteAssert
import org.assertj.core.util.AssertionsUtil
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Supplier

/**
 * See [org.assertj.core.api.abstract_.AbstractAssert_as_with_description_text_supplier_Test].
 * `as` is a keyword in Kotlin, so we use `describedAs` instead.
 */
internal class Assertions_describedAs_text_supplier_Test {

  @Test
  fun descriptionText_should_evaluate_lazy_description() {
    // GIVEN
    val assertions = ConcreteAssert("foo")
    // WHEN
    assertions.describedAs { "description" }
    // THEN
    BDDAssertions.then(assertions.descriptionText()).isEqualTo("description")
  }


  @Test
  fun should_not_evaluate_description_when_assertion_succeeds() {
    // GIVEN
    val evaluated = AtomicBoolean(false)
    val descriptionSupplier = spiedSupplier(evaluated)
    // WHEN
    assertThat(true).describedAs(descriptionSupplier).isTrue
    // THEN
    BDDAssertions.then(evaluated).isFalse
  }

  @Test
  fun should_evaluate_description_when_assertion_fails() {
    // GIVEN
    val evaluated = AtomicBoolean(false)
    val descriptionSupplier = spiedSupplier(evaluated)
    // WHEN
    AssertionsUtil.expectAssertionError {
      assertThat(true).describedAs(descriptionSupplier).isFalse()
    }
    // THEN
    BDDAssertions.then(evaluated).isTrue
  }

  @Test
  fun should_return_this() {
    // GIVEN
    val assertions = ConcreteAssert("foo")
    // WHEN
    val returnedAssertions = assertions.describedAs { "description" }
    // THEN
    BDDAssertions.then(returnedAssertions).isSameAs(assertions)
  }

  @Test
  fun should_throw_evaluate_lazy_description() {
    // GIVEN
    val assertions = ConcreteAssert("foo")
    val descriptionSupplier: Supplier<String>? = null
    // WHEN
    val throwable = Assertions.catchThrowable {
      assertions.describedAs(
        descriptionSupplier
      ).descriptionText()
    }
    // THEN
    BDDAssertions.then(throwable).isInstanceOf(IllegalStateException::class.java)
      .hasMessage("the descriptionSupplier should not be null")
  }

  private fun spiedSupplier(evaluated: AtomicBoolean) =
    Supplier {
      evaluated.set(true)
      "description"
    }
}
