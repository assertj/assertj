package org.assertj.core.error;

public class ShouldBeInIntegerFormat extends BasicErrorMessageFactory{  public static ErrorMessageFactory shouldBeInIntegerFormat(Object actual) {
  return new ShouldBeInIntegerFormat(actual);
}

  private ShouldBeInIntegerFormat(Object actual) {
    super("%nExpecting:%n  <%s>%nto be in integer format but it was not.", actual);
  }

}
