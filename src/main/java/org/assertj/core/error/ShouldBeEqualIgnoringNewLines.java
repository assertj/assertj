package org.assertj.core.error;

public class ShouldBeEqualIgnoringNewLines  extends BasicErrorMessageFactory {
	 public static ErrorMessageFactory shouldBeEqualIgnoringNewLines(CharSequence actual, CharSequence expected) {
	    return new ShouldBeEqualIgnoringNewLines(actual, expected);
	 }

	  private ShouldBeEqualIgnoringNewLines(CharSequence actual, CharSequence expected) {
	    super("%nExpecting:%n  <%s>%nto be equal to:%n  <%s>%nignoring newlines (\n, \r\n).",
	          actual, expected);
	  }

}

