package org.assertj.core.error;

public class ShouldBeXmlElement extends BasicErrorMessageFactory {

  private ShouldBeXmlElement(String reason) {
    super("\nExpected to contain single Element, but %s have been found!", unquotedString(reason));
  }

  public static ErrorMessageFactory shouldBeElementBut(String reason) {
    return new ShouldBeXmlElement(reason);
  }

}
