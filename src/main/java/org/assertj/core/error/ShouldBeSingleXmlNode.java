package org.assertj.core.error;

public class ShouldBeSingleXmlNode extends BasicErrorMessageFactory {

  public ShouldBeSingleXmlNode(String reason) {
    super("\nExpected to contain single node, but %s have been found!", unquotedString(reason));
  }

}
