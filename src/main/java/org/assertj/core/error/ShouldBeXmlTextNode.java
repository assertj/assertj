package org.assertj.core.error;

public class ShouldBeXmlTextNode extends BasicErrorMessageFactory {

  public ShouldBeXmlTextNode(String reason) {
    super("\nExpected to contain single Text node, but %s have been found!", unquotedString(reason));
  }

}
