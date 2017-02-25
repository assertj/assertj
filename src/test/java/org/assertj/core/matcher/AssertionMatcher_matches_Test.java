package org.assertj.core.matcher;

import org.assertj.core.internal.Failures;
import org.assertj.core.util.Throwables;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AssertionMatcher_matches_Test {
  private static final Integer ZERO = 0;
  private static final Integer ONE = 1;

  private final AssertionMatcher<Integer> isZeroMatcher = new AssertionMatcher<Integer>() {
    @Override
    public void assertion(Integer actual) throws AssertionError {
      assertThat(actual).isZero();
    }
  };

  @Test
  public void should_pass_when_assertion_passes() {
    assertThat(isZeroMatcher.matches(ZERO)).isTrue();
  }

  @Test
  public void should_not_fill_description_when_assertion_passes() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ZERO)).isTrue();

    isZeroMatcher.describeTo(description);
    verifyZeroInteractions(description);
  }

  @Test
  public void should_fail_when_assertion_fails() {
    assertThat(isZeroMatcher.matches(ONE)).isFalse();
  }

  /**
   * Stacktrace is not complete here in this test, because {@link Failures} filters out
   * all stacktrace frames that contain {@link Throwables#ORG_ASSERTJ} package name.
   *
   * These are lines filtered out by {@link Failures}:
   *
   * at org.assertj.core.matcher.AssertionMatcher_matches_Test$1.assertion(AssertionMatcher_matches_Test.java:19)
   * at org.assertj.core.matcher.AssertionMatcher_matches_Test$1.assertion(AssertionMatcher_matches_Test.java:15)
   * at org.assertj.core.matcher.AssertionMatcher.matches(AssertionMatcher.java:54)
   * at org.assertj.core.matcher.AssertionMatcher_matches_Test.should_fill_description_when_assertion_fails(AssertionMatcher_matches_Test.java:53)
   */
  @Test
  public void should_fill_description_when_assertion_fails() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ONE)).isFalse();

    isZeroMatcher.describeTo(description);
    verify(description).appendText("AssertionError with message: ");
    verify(description).appendText("expected:<[0]> but was:<[1]>");
    verify(description).appendText("\n\nStacktrace was: ");
    verify(description).appendText(argThat(new ArgumentMatcher<String>() {
      @Override public boolean matches(String s) {
        return s.contains("org.junit.ComparisonFailure: expected:<[0]> but was:<[1]>");
      }
    }));
  }
}