package org.assertj.core.error;

public class ShouldHaveThrownException extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldThrowException(Class<?> clazz, Class<?> expectedException) {
    return new ShouldHaveThrownException(clazz, expectedException);
  }

  private ShouldHaveThrownException(Class<?> clazz, Class<?> expectedException) {
    super("%nExpecting:%n  %s%n to have thrown %n %s", clazz, expectedException);
  }
}
