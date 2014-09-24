package org.assertj.core.error;

public class ShouldBeSingleXmlNode extends BasicErrorMessageFactory {

  private ShouldBeSingleXmlNode(String reason) {
    super("\nExpected to contain single node, but %s have been found!", unquotedString(reason));
  }

  public static ErrorMessageFactory shouldBeSingleXmlNodeBut(String reason) {
    return new ShouldBeSingleXmlNode(reason);
  }

}
