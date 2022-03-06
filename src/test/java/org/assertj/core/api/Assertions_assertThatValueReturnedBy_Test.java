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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatValueReturnedBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;

class Assertions_assertThatValueReturnedBy_Test {

  @Test
  void can_assert_on_callable_returned_value() {
    // Given
    Callable<Integer> ultimateAnswer = () -> 42;
    // Then
    assertThatValueReturnedBy(ultimateAnswer).isEqualTo(42);
    assertThatValueReturnedBy(ultimateAnswer).asInstanceOf(INTEGER).isLessThan(50);
  }

  @Test
  void should_fail_with_the_exception_throw_by_callable() {
    // Given
    Exception exception = new Exception("boom");
    Callable<?> boom = () -> {
      throw exception;
    };
    // When
    AssertionError assertionError = expectAssertionError(() -> assertThatValueReturnedBy(boom));
    // Then
    then(assertionError).hasMessage("Unexpected exception when resolving callable")
                        .hasCause(exception);
  }

}
