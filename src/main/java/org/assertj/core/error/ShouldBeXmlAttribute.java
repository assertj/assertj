package org.assertj.core.error;

public class ShouldBeXmlAttribute extends BasicErrorMessageFactory {

  public ShouldBeXmlAttribute(String reason) {
    super("\nExpected to contain single Attribute, but %s have been found!", unquotedString(reason));
  }

}
