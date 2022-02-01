package org.assertj.core.error;

public class ShouldBeHexadecimal extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBeHexadecimal(Object actual) {
    return new ShouldBeHexadecimal(actual);
  }

  private ShouldBeHexadecimal(Object actual) {
    super("%nExpecting %s to be hexadecimal", actual);
  }
}
