/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.assertj.core.internal.Failures;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class AssertionMatcher_matches_Test {
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
  @Before
  public void setUp() {
    removeAssertJRelatedElementsFromStackTrace = Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace();
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  @After
  public void tearDown() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  @Test
  public void matcher_should_pass_when_assertion_passes() {
    assertThat(isZeroMatcher.matches(ZERO)).isTrue();
  }

  @Test
  public void matcher_should_not_fill_description_when_assertion_passes() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ZERO)).isTrue();

    isZeroMatcher.describeTo(description);
    verifyZeroInteractions(description);
  }

  @Test
  public void matcher_should_fail_when_assertion_fails() {
    assertThat(isZeroMatcher.matches(ONE)).isFalse();
  }

  /**
   * {@link Failures#removeAssertJRelatedElementsFromStackTrace} must be set to true
   * in order for this test to pass. It is in {@link this#setUp()}.
   */
  @Test
  public void matcher_should_fill_description_when_assertion_fails() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ONE)).isFalse();

    isZeroMatcher.describeTo(description);
    verify(description).appendText("AssertionError with message: ");
    verify(description).appendText("expected:<[0]> but was:<[1]>");
    verify(description).appendText(String.format("%n%nStacktrace was: "));
    // @format:off
    verify(description).appendText(argThat(new ArgumentMatcher<String>() {
      @Override 
      public boolean matches(String s) {
        return s.contains("org.junit.ComparisonFailure: expected:<[0]> but was:<[1]>")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher_matches_Test$1.assertion(AssertionMatcher_matches_Test.java:")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher.matches(AssertionMatcher.java:")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher_matches_Test.matcher_should_fill_description_when_assertion_fails(AssertionMatcher_matches_Test.java:");
      }
    }));
    // @format:on
  }
}