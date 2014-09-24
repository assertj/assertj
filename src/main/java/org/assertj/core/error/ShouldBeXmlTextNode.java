package org.assertj.core.error;

public class ShouldBeXmlTextNode extends BasicErrorMessageFactory {

  private ShouldBeXmlTextNode(String reason) {
    super("\nExpected to contain single Text node, but %s have been found!", unquotedString(reason));
  }

  public static ErrorMessageFactory shouldBeTextNodeBut(String reason) {
    return new ShouldBeXmlTextNode(reason);
  }

}
