package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;

class CallableAssert_Demo_Test {

  @Test
  void demo() {
    Callable<Boolean> callable = () -> "value".isEmpty();

    // AbstractCallableAssert
    assertThatCode(callable).doesNotThrowAnyException()
                            .result() // returns AbstractObjectAssert
                            .isEqualTo(false);

    // AbstractCallableAssert
    assertThatCode(callable).result(BOOLEAN) // implicit doesNotThrowAnyException()
                            .isFalse();

    // AbstractCallableAssert
    assertThatCode(() -> "value".isEmpty()).doesNotThrowAnyException()
                                           .result()
                                           .isEqualTo(false);

    // AbstractCallableAssert
    assertThatCode(() -> nonVoidThrowingMethod()).hasThrownException() // returns ThrowableAssertAlternative
                                                 .withMessage("Boom!")
                                                 .withNoCause();

    ThrowableAssert.ThrowingCallable assertjThrowingCallable = () -> "value".isEmpty();

    // AbstractThrowableAssert
    assertThatCode(assertjThrowingCallable).doesNotThrowAnyException();

    // AbstractThrowableAssert
    assertThatCode(() -> System.out.println("")).doesNotThrowAnyException();

    // AbstractThrowableAssert
    assertThatCode(() -> voidThrowingMethod()).hasMessage("Boom!");
  }

  private static Object nonVoidThrowingMethod() {
    throw new RuntimeException("Boom!");
  }

  private static void voidThrowingMethod() {
    throw new RuntimeException("Boom!");
  }

  @Test
  public void intendedApi() {
    assertThatCode(this::doStuff).describedAs("doing things")
                                 .doesNotThrowAnyException()
                                 .result() // implicit doesNotThrowAnyException()
                                 .isEqualTo(3);
  }

  private int doStuff() throws Exception {
//    if (new Random().nextBoolean()) {
//      throw new Exception();
//    }
    return 3;
  }

}
