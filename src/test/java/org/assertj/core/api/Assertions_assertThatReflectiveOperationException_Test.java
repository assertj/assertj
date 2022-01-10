package org.assertj.core.api;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatReflectiveOperationException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;


public class Assertions_assertThatReflectiveOperationException_Test {

  @Test
  void should_pass_when_throw_ReflectiveOperationException() {
    // WHEN
    assertThatReflectiveOperationException().isThrownBy(() -> {
      throw new ReflectiveOperationException();
    });
  }

  @Test
  void should_fail_when_throw_wrong_type() {
    // GIVEN
    ThrowingCallable throwingCallable = () -> assertThatReflectiveOperationException().isThrownBy(() -> {
      throw new Error();
    });

    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);

    // THEN
    then(assertionError).hasMessageContainingAll(Error.class.getName(), ReflectiveOperationException.class.getName());
  }

  @Test
  void should_fail_when_no_exception_thrown() {
    // GIVEN
    ThrowingCallable throwingCallable = () -> assertThatReflectiveOperationException().isThrownBy(() -> {
    });

    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);

    // THEN
    then(assertionError).hasMessage(format("%nExpecting code to raise a throwable."));
  }
}
