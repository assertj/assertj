package org.assertj.core.error;

public class ShouldBeNumeric extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBeNumeric(Object actual) {
    return new ShouldBeNumeric(actual);
  }

  private ShouldBeNumeric(Object actual) {
    super("%nExpecting %s to be numeric", actual);
  }
}
