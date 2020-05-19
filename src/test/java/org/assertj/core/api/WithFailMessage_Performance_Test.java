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
<<<<<<< HEAD
    String error = "hyb";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
=======
    int i = 100000;
    while (i != 0) {
>>>>>>> 39b15c511aeb7f32b7981ffb3b7d40a347a857de
      AtomicReference<String> actual = new AtomicReference<>("foo");
      try {
        String error = "error";
        assertThat(actual).withFailMessage(error).hasValue("foo");
      }catch (AssertionError e) {

      }
      --i;
    }
  }

<<<<<<< HEAD
  @Test
  public void test1() {
    long startTime = System.nanoTime();
    try {
      String error = "hyb";
      assertThat(error).withFailMessage("my error").isEqualTo("wy");
    }catch (AssertionError e){
      long endTime = System.nanoTime();
      System.out.println("程序运行时间： " + (endTime - startTime)/1000000 + "ms");
    }
  }
=======
>>>>>>> 39b15c511aeb7f32b7981ffb3b7d40a347a857de

}
