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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class Assertions_assertThatExceptionOfType_Test {

  private static <E> Supplier<E> s(Supplier<E> supplier) {
    return supplier;
  }

  public static Iterable<? extends Object> data() {
    return Arrays.asList(
      new Object[] {
        s(() -> assertThatExceptionOfType(UnsupportedOperationException.class)),
        UnsupportedOperationException.class,
        s(() -> new UnsupportedOperationException())  
      },
      new Object[] {s(() -> assertThatNullPointerException()), NullPointerException.class, s(() -> new NullPointerException("value"))},
      new Object[] {s(() -> assertThatIllegalArgumentException()), IllegalArgumentException.class, s(() -> new IllegalArgumentException("arg"))},
      new Object[] {s(() -> assertThatIllegalStateException()), IllegalStateException.class, s(() -> new IllegalStateException("state"))},
      new Object[] {s(() -> assertThatIOException()), IOException.class, s(() -> new IOException("io"))}      
    );
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_create_ExpectThrowableAssert(Supplier<ThrowableTypeAssert<? extends Exception>> assertionGenerator,
                                                  Class<? extends Exception> exceptionType,
                                                  Supplier<? extends Exception> exceptionBuilder) {
    ThrowableTypeAssert<? extends Exception> assertions = assertionGenerator.get();
    assertThat(assertions).isNotNull();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_pass_ExceptionType(Supplier<ThrowableTypeAssert<? extends Exception>> assertionGenerator,
                                        Class<? extends Exception> exceptionType,
                                        Supplier<? extends Exception> exceptionBuilder) {
    ThrowableTypeAssert<? extends Exception> assertions = assertionGenerator.get();
    assertThat(assertions.expectedThrowableType).isSameAs(exceptionType);
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_create_ChainedThrowableAssert(Supplier<ThrowableTypeAssert<? extends Exception>> assertionGenerator,
                                                   Class<? extends Exception> exceptionType,
                                                   Supplier<? extends Exception> exceptionBuilder) {
    ThrowableAssertAlternative<? extends Exception> assertions = assertionGenerator.get().isThrownBy(() -> {
      throw exceptionBuilder.get();
    });
    assertThat(assertions).isNotNull();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_pass_thrown_exception_to_ChainedThrowableAssert(Supplier<ThrowableTypeAssert<? extends Exception>> assertionGenerator,
                                                                     Class<? extends Exception> exceptionType,
                                                                     Supplier<? extends Exception> exceptionBuilder) {
    Exception exception = exceptionBuilder.get();
    ThrowableAssertAlternative<? extends Exception> assertions = assertionGenerator.get().isThrownBy(() -> {
      throw exception;
    });
    assertThat(assertions.actual).isSameAs(exception);
  }
}
