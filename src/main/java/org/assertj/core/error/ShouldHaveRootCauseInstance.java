package org.assertj.core.error;

import org.assertj.core.util.Throwables;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Throwable} have a root cause
 * instance of a certain type.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveRootCauseInstance extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link BasicErrorMessageFactory}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expectedCauseType the expected cause type.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveRootCauseInstance(Throwable actual,
      Class<? extends Throwable> expectedCauseType) {
    return Throwables.getRootCause(actual) == null ? new ShouldHaveRootCauseInstance(expectedCauseType)
        : new ShouldHaveRootCauseInstance(actual, expectedCauseType);
  }

  private ShouldHaveRootCauseInstance(Throwable actual, Class<? extends Throwable> expectedCauseType) {
    super("%nExpecting a throwable with root cause being an instance of:%n <%s>%nbut was an instance of:%n <%s>",
        expectedCauseType, Throwables.getRootCause(actual));
  }

  private ShouldHaveRootCauseInstance(Class<? extends Throwable> expectedCauseType) {
    super("%nExpecting a throwable with root cause being an instance of:%n <%s>%nbut current throwable has no cause.",
        expectedCauseType);
  }
}
