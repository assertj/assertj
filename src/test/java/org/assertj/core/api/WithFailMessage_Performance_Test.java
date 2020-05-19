package org.assertj.core.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.atomic.AtomicReference;

public class WithFailMessage_Performance_Test {

  @Test
  public void withFailMessage_performance_improved_test() {
    int i = 100000;
    while (i != 0) {
      AtomicReference<String> actual = new AtomicReference<>("foo");
      try {
        assertThat(actual).withFailMessage(()->{
          return "error";
        }).hasValue("foo");
      }catch (AssertionError e) {

      }
      --i;
    }
  }

  @Test
  public void withFailMessage_performance_test() {
    int i = 100000;
    while (i != 0) {
      AtomicReference<String> actual = new AtomicReference<>("foo");
      try {
        String error = "error";
        assertThat(actual).withFailMessage(error).hasValue("foo");
      }catch (AssertionError e) {

      }
      --i;
    }
  }


}
