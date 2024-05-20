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
package org.assertj.tests.core.kotlin.testkit

import org.assertj.core.api.AbstractAssert
import org.assertj.core.internal.Objects
import org.assertj.core.util.VisibleForTesting
import org.opentest4j.AssertionFailedError

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ConcreteAssert(actual: Any?) :
  AbstractAssert<ConcreteAssert, Any?>(actual, ConcreteAssert::class.java) {
  /**
   * Not a really relevant assertion, the goal is to show how to write a new assertion with a specific error message
   * that honors the description set by the assertion user.
   */
  fun checkNull(): ConcreteAssert {
    // set a specific error message
    val info = writableAssertionInfo
    info.overridingErrorMessage("specific error message")
    Objects.instance().assertNull(info, actual)
    // return the current assertion for method chaining
    return this
  }

  fun failIfTrue(fail: Boolean): ConcreteAssert {
    // set a specific error message
    if (fail) {
      failWithMessage("%s error message", "predefined")
    }
    // return the current assertion for method chaining
    return this
  }

  @VisibleForTesting
  public override fun failWithMessage(errorMessage: String, vararg arguments: Any) {
    super.failWithMessage(errorMessage, *arguments)
  }

  @VisibleForTesting
  public override fun failWithActualExpectedAndMessage(
    actual: Any,
    expected: Any,
    errorMessage: String,
    vararg arguments: Any
  ) {
    super.failWithActualExpectedAndMessage(actual, expected, errorMessage, *arguments)
  }

  @VisibleForTesting
  public override fun failure(errorMessage: String, vararg arguments: Any): AssertionError {
    return super.failure(errorMessage, *arguments)
  }

  @VisibleForTesting
  public override fun failureWithActualExpected(
    actual: Any,
    expected: Any,
    errorMessage: String,
    vararg arguments: Any
  ): AssertionFailedError {
    return super.failureWithActualExpected(actual, expected, errorMessage, *arguments) as AssertionFailedError
  }
}
