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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import org.assertj.core.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;


/**
 * Tests for <code>{@link AbstractThrowableAssert#message()}</code>.
 *
 * @author Trang Nguyen
 */

class AbstractThrowableAssert_message_Test {
  @Test
  void should_create_failure_with_no_exception() {
    // GIVEN
    Object actual = null;
    // WHEN
    AbstractStringAssert assertionError = assertThatThrownBy(() -> assertThat(actual).isInstanceOf(Object.class)).message();
    // THEN
    then(assertionError).withFailMessage("Expecting actual not to be null");
  }

  @Test
  void should_pass_with_exception() {
    // GIVEN
    ConcreteAssert assertion = new ConcreteAssert("foo");
    Object actual = "Actual";
    Object expected = "Expected";
    // WHEN
    AssertionFailedError afe = assertion.failureWithActualExpected(actual, expected, "fail");
    // THEN
    then(afe).message().containsAnyOf("fail", "Fiali");
  }

}
