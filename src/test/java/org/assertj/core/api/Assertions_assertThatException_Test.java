package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.ThrowingCallableFactory.codeThrowing;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_assertThatException_Test {

  @Test
  void should_pass_when_throw_Exception() {
    assertThatException().isThrownBy(codeThrowing(new Exception()));
  }

  @Test
  void should_pass_when_throw_Exception_Subclass() {
    assertThatException().isThrownBy(codeThrowing(new RuntimeException()));
  }

  @Test
  void should_fail_when_throw_wrong_type() {
    // GIVEN
    ThrowingCallable throwingCallable = () -> assertThatException().isThrownBy(codeThrowing(new Error()));
    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);
    // THEN
    then(assertionError).hasMessageContainingAll(Error.class.getName(), Exception.class.getName());
  }

  @Test
  void should_fail_when_no_exception_thrown() {
    // GIVEN
    ThrowingCallable throwingCallable = () -> assertThatException().isThrownBy(() -> {});
    // WHEN
    AssertionError assertionError = expectAssertionError(throwingCallable);
    // THEN
    then(assertionError).hasMessage(format("%nExpecting code to raise a throwable."));
  }
}
