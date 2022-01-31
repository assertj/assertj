package org.assertj.core.error;

public class ShouldBeAlphabetic extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBeAlphabetic(Object actual) {
    return new ShouldBeAlphabetic(actual);
  }

  private ShouldBeAlphabetic(Object actual) {
    super("%nExpecting %s to be alphabetic", actual);
  }
}
