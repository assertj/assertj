package org.assertj.core.error;

public class ShouldBeXmlComment extends BasicErrorMessageFactory {

  public ShouldBeXmlComment(String reason) {
    super("\nExpected to contain single Comment, but %s have been found!", unquotedString(reason));
  }

}
