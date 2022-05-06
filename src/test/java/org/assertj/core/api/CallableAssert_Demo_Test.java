/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;

class CallableAssert_Demo_Test { // FIXME to be deleted

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
