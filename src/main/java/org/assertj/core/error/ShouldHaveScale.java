package org.assertj.core.error;

import java.math.BigDecimal;

public class ShouldHaveScale extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveScale(BigDecimal actual, int expected) {
    return new ShouldHaveScale(actual, expected);
  }

  private ShouldHaveScale(BigDecimal actual, int expected) {
    super("%nexpecting actual %s to have a scale of:%n  %s%nbut had a scale of:%n  %s", actual, expected, actual.scale());
  }
}
