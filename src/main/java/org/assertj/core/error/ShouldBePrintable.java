package org.assertj.core.error;

public class ShouldBePrintable extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldBePrintable(Object actual) {
    return new ShouldBePrintable(actual);
  }

  private ShouldBePrintable(Object actual) {
    super("%nExpecting %s to be printable", actual);
  }
}
