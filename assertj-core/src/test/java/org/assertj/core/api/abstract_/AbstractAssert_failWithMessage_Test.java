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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ConcreteAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>AbstractAssert#failWithMessage(String, Object...)</code>.
 *
 * @author Joel Costigliola
 */
class AbstractAssert_failWithMessage_Test {

  private ConcreteAssert assertion;

  @BeforeEach
  void setup() {
    assertion = new ConcreteAssert("foo");
  }

  @Test
  void should_fail_with_simple_message() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertion.failWithMessage("fail"));
    // THEN
    then(error).hasMessage("fail");
  }

  @Test
  void should_fail_with_message_having_args() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertion.failWithMessage("fail %d %s", 5, "times"));
    // THEN
    then(error).hasMessage("fail 5 times");
  }

  @Test
  void should_keep_description_set_by_user() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertion.as("user description").failWithMessage("fail %d %s", 5, "times"));
    // THEN
    then(error).hasMessage("[user description] fail 5 times");
  }

  @Test
  void should_keep_specific_error_message_and_description_set_by_user() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertion.as("test context")
                                                               .overridingErrorMessage("my %d errors %s %%s", 5, "!")
                                                               .failWithMessage("%d %s", 5, "time"));
    // THEN
    then(error).hasMessage("[test context] my 5 errors ! %s");
  }

}
