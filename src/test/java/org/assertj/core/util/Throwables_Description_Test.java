package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class Throwables_Description_Test {

  public static void main(String[] args) {
    Exception e;
    try {
      throw new RuntimeException();
    } catch (RuntimeException e2) {
      e = e2;
    }

    assertThat(e).isNull();
//    assertThat(e).as(ExceptionUtils.getStackTrace(e)).isNull();
  }
}
