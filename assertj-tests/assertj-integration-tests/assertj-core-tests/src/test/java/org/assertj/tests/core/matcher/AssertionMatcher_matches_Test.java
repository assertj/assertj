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
package org.assertj.tests.core.matcher;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.tests.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.internal.Failures;
import org.assertj.core.matcher.AssertionMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssertionMatcher_matches_Test {
  private static final Integer ZERO = 0;
  private static final Integer ONE = 1;

  private final AssertionMatcher<Integer> isZeroMatcher = new AssertionMatcher<Integer>() {
    @Override
    public void assertion(Integer actual) throws AssertionError {
      assertThat(actual).isZero();
    }
  };

  private boolean removeAssertJRelatedElementsFromStackTrace;

  /**
   * Stacktrace filtering must be disabled in order to check frames in
   * {@link this#matcher_should_fill_description_when_assertion_fails()}.
   * I use setUp and tearDown methods to ensure that it is set to original value after a test.
   */
  @BeforeEach
  public void setUp() {
    removeAssertJRelatedElementsFromStackTrace = Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace();
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  @AfterEach
  public void tearDown() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  @Test
  void matcher_should_pass_when_assertion_passes() {
    assertThat(isZeroMatcher.matches(ZERO)).isTrue();
  }

  @Test
  void matcher_should_not_fill_description_when_assertion_passes() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ZERO)).isTrue();

    isZeroMatcher.describeTo(description);
    verifyNoInteractions(description);
  }

  @Test
  void matcher_should_fail_when_assertion_fails() {
    assertThat(isZeroMatcher.matches(ONE)).isFalse();
  }

  /**
   * {@link Failures#removeAssertJRelatedElementsFromStackTrace} must be set to true
   * in order for this test to pass. It is in {@link this#setUp()}.
   */
  @Test
  void matcher_should_fill_description_when_assertion_fails() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ONE)).isFalse();

    isZeroMatcher.describeTo(description);
    verify(description).appendText("AssertionError with message: ");
    verify(description).appendText(shouldBeEqualMessage("1", "0"));
    verify(description).appendText(format("%n%nStacktrace was: "));
    // @format:off
    verify(description).appendText(argThat(s -> s.contains(format("%nexpected: 0%n but was: 1"))
        && s.contains("at org.assertj.tests.core.matcher.AssertionMatcher_matches_Test$1.assertion(AssertionMatcher_matches_Test.java:")
        && s.contains("at org.assertj.core.matcher.AssertionMatcher.matches(AssertionMatcher.java:")
        && s.contains("at org.assertj.tests.core.matcher.AssertionMatcher_matches_Test.matcher_should_fill_description_when_assertion_fails(AssertionMatcher_matches_Test.java:")));
    // @format:on
  }
}
