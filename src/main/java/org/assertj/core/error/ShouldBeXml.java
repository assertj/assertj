package org.assertj.core.error;

public class ShouldBeXml extends BasicErrorMessageFactory {

  private ShouldBeXml(CharSequence actual) {
    super("\nExpecting xml document but was:<%s>", actual);
  }

  public static ErrorMessageFactory shouldBeXml(CharSequence actual) {
    return new ShouldBeXml(actual);
  }

}
