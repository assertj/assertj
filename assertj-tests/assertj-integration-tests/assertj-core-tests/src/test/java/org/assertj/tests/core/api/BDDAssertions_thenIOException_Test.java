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
package org.assertj.tests.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIOException;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.tests.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class BDDAssertions_thenIOException_Test {

  @Test
  void should_pass_when_IOException_is_thrown() {
    // GIVEN
    ThrowingCallable throwingSupplier = codeThrowing(new IOException("something was wrong"));
    // WHEN/THEN
    thenIOException().isThrownBy(throwingSupplier).withMessage("something was wrong");
  }

  @Test
  void should_fail_when_IOException_is_not_thrown() {
    // GIVEN
    IllegalArgumentException exception = new IllegalArgumentException("boom");
    ThrowingCallable throwingSupplier = codeThrowing(exception);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> thenIOException().isThrownBy(throwingSupplier));
    // THEN
    then(assertionError).hasMessage(shouldBeInstance(exception, IOException.class).create());
  }

  @Test
  void should_fail_when_no_exception_is_thrown() {
    // GIVEN
    ThrowingCallable throwingSupplier = () -> {};
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> thenIOException().isThrownBy(throwingSupplier));
    // THEN
    then(assertionError).hasMessage("%nExpecting code to throw a java.io.IOException, but no throwable was thrown.".formatted());
  }

}
