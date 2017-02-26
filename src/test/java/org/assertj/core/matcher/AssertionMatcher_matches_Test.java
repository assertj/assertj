package org.assertj.core.matcher;

import org.assertj.core.internal.Failures;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
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

  /**
   * Stacktrace filtering must be disabled in order to check frames in
   * {@link this#should_fill_description_when_assertion_fails}.
   * I use setUp and tearDown methods to ensure that it is set to original
   * value after a test.
   */
  @Before
  public void setUp() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  @After
  public void tearDown() {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(true);
  }

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
   * {@link Failures#removeAssertJRelatedElementsFromStackTrace} must be set to true
   * in order for this sets to pass. It is set in {@link this#setUp} to true.
   */
  @Test
  public void should_fill_description_when_assertion_fails() {
    Description description = mock(Description.class);

    assertThat(isZeroMatcher.matches(ONE)).isFalse();

    isZeroMatcher.describeTo(description);
    verify(description).appendText("AssertionError with message: ");
    verify(description).appendText("expected:<[0]> but was:<[1]>");
    verify(description).appendText(String.format("%n%nStacktrace was: "));
    verify(description).appendText(argThat(new ArgumentMatcher<String>() {
      @Override public boolean matches(String s) {
        return s.contains("org.junit.ComparisonFailure: expected:<[0]> but was:<[1]>")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher_matches_Test$1.assertion(AssertionMatcher_matches_Test.java:20)")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher_matches_Test$1.assertion(AssertionMatcher_matches_Test.java:17)")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher.matches(AssertionMatcher.java:53)")
            && s.contains("at org.assertj.core.matcher.AssertionMatcher_matches_Test.should_fill_description_when_assertion_fails(AssertionMatcher_matches_Test.java:68)");
      }
    }));
  }
}