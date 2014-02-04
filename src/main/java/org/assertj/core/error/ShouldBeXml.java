package org.assertj.core.error;

public class ShouldBeXml extends BasicErrorMessageFactory {

  public ShouldBeXml(CharSequence actual) {
    super("\nExpecting xml document but was:<%s>", actual);
  }

}
