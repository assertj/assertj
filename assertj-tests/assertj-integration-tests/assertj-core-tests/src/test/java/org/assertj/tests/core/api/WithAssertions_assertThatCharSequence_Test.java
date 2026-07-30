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

import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class WithAssertions_assertThatCharSequence_Test {

  static WithAssertions withAssertions = mock(CALLS_REAL_METHODS);

  @Test
  void should_accept_CharSequence() {
    // GIVEN
    CharSequence actual = "Yoda";
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = withAssertions.assertThatCharSequence(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_String() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = withAssertions.assertThatCharSequence(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_StringBuffer() {
    // GIVEN
    StringBuffer actual = new StringBuffer("Yoda");
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = withAssertions.assertThatCharSequence(actual);
    // THEN
    result.startsWith("Yo");
  }

  @Test
  void should_accept_StringBuilder() {
    // GIVEN
    StringBuilder actual = new StringBuilder("Yoda");
    // WHEN
    AbstractCharSequenceAssert<?, ? extends CharSequence> result = withAssertions.assertThatCharSequence(actual);
    // THEN
    result.startsWith("Yo");
  }

}
