package org.assertj.core.api.abstract_;

import org.assertj.core.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;


/**
 * Tests for <code>{@link AbstractThrowableAssert#message()}</code>.
 *
 * @author Trang Nguyen
 */

class AbstractThrowableAssert_message_Test {
  @Test
  void should_create_failure_with_no_exception() {
    // GIVEN
    Object actual = null;
    // WHEN
    StringAssert assertionError = assertThatThrownBy(() -> assertThat(actual).isInstanceOf(Object.class)).message();
    // THEN
    then(assertionError).withFailMessage("Expecting actual not to be null");
  }

  @Test
  void should_pass_with_exception() {
    // GIVEN
    ConcreteAssert assertion = new ConcreteAssert("foo");
    Object actual = "Actual";
    Object expected = "Expected";
    // WHEN
    AssertionFailedError afe = assertion.failureWithActualExpected(actual, expected, "fail");
    // THEN
    then(afe).message().containsAnyOf("fail", "Fiali");
  }

}
