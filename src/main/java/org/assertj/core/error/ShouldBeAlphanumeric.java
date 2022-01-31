package org.assertj.core.error;

public class ShouldBeAlphanumeric extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBeAlphanumeric(Object actual) {
    return new ShouldBeAlphanumeric(actual);
  }

  private ShouldBeAlphanumeric(Object actual) {
    super("%nExpecting %s to be alphanumeric", actual);
  }
}
