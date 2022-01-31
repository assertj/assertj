package org.assertj.core.error;

public class ShouldBeVisible extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBeVisible(Object actual) {
    return new ShouldBeVisible(actual);
  }

  private ShouldBeVisible(Object actual) {
    super("%nExpecting %s to be visible", actual);
  }
}
