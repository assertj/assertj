package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;

public class CronFieldValidation {

  private static final CronFieldValidation INSTANCE = new CronFieldValidation();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static CronFieldValidation instance() {
    return INSTANCE;
  }

  public void assertIsValid(AssertionInfo info, CronField actual, Failures failures) {
    assertNotNull(info, actual);
    actual.assertIsValid(info, failures);
  }

  private void assertNotNull(AssertionInfo info, CronField actual) {
    Objects.instance().assertNotNull(info, actual);
  }

}
