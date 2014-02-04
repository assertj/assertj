package org.assertj.core.error;

public class ShouldBeXmlElement extends BasicErrorMessageFactory {

  public ShouldBeXmlElement(String reason) {
    super("\nExpected to contain single Element, but %s!", unquotedString(reason));
  }

}
