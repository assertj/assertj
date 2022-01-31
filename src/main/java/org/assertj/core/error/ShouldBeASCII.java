package org.assertj.core.error;

public class ShouldBeASCII extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBeASCII(Object actual) {
    return new ShouldBeASCII(actual);
  }

  private ShouldBeASCII(Object actual) {
    super("%nExpecting %s to be ASCII", actual);
  }
}
