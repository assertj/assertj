package org.assertj.core.error;

public class ShouldBeXmlAttribute extends BasicErrorMessageFactory {

  private ShouldBeXmlAttribute(String reason) {
    super("\nExpected to contain single Attribute, but %s have been found!", unquotedString(reason));
  }

  public static ErrorMessageFactory shouldBeAttributeBut(String reason) {
    return new ShouldBeXmlAttribute(reason);
  }

}
