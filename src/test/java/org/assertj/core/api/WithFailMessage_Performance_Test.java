package org.assertj.core.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.atomic.AtomicReference;

public class WithFailMessage_Performance_Test {

  @Test
  public void withFailMessage_performance_test() {
    String error = "ssss";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      AtomicReference<String> actual = new AtomicReference<>("foo");
      assertThat(actual).withFailMessage(error).hasValue("bar");
    }).withMessageContaining(error);
  }

}
