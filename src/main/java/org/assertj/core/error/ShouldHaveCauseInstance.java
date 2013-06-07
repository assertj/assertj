package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Throwable} have a cause instance of
 * a certain type.
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveCauseInstance extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link org.assertj.core.error.BasicErrorMessageFactory}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expectedCauseType the expected cause type.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveCauseInstance(Throwable actual,
      Class<? extends Throwable> expectedCauseType) {
    return actual.getCause() == null ? new ShouldHaveCauseInstance(expectedCauseType) : new ShouldHaveCauseInstance(
        actual, expectedCauseType);
  }

  private ShouldHaveCauseInstance(Throwable actual, Class<? extends Throwable> expectedCauseType) {
    super("%nExpecting a throwable with cause being an instance of:%n <%s>%nbut was an instance of:%n <%s>",
        expectedCauseType, actual.getCause());
  }

  private ShouldHaveCauseInstance(Class<? extends Throwable> expectedCauseType) {
    super("%nExpecting a throwable with cause being an instance of:%n <%s>%nbut current throwable has no cause.",
        expectedCauseType);
  }
}
