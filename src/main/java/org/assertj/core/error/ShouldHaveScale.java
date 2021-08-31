package org.assertj.core.error;

public class ShouldHaveScale extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveScale(int actual, int expected) {
    return new ShouldHaveScale(actual, expected);
  }

  private ShouldHaveScale(int actual, int expected) {
    super("%nexpecting actual to have a scale of: %s%n  but has a scale of: %s", expected, actual);
  }
}
