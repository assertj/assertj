package org.assertj.core.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.atomic.AtomicReference;

public class WithFailMessage_Performance_Test {

  @Test
  public void withFailMessage_performance_test() {
    String error = "hyb";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      AtomicReference<String> actual = new AtomicReference<>("foo");
      assertThat(actual).withFailMessage(error).hasValue("bar");
    }).withMessageContaining(error);
  }

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

}
