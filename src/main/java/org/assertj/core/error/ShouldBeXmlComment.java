package org.assertj.core.error;

public class ShouldBeXmlComment extends BasicErrorMessageFactory {

  private ShouldBeXmlComment(String reason) {
    super("\nExpected to contain single Comment, but %s have been found!", unquotedString(reason));
  }

  public static ErrorMessageFactory shouldBeCommentBut(String reason) {
    return new ShouldBeXmlComment(reason);
  }

}
