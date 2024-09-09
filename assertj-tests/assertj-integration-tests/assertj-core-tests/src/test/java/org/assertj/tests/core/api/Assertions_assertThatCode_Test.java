package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.EOFException;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.api.Assertions#assertThatCode(ThrowingCallable)}
 *
 * @author Mikhail Polivakha
 */
public class Assertions_assertThatCode_Test {

  @Test
  void should_pass_assert_happy_path() {
    String value = "value";

    Assertions
              .assertThatCode(() -> value)
              .doesNotThrowAnyException()
              .resultsInValueSatisfying(s -> s.equals(value));
  }

  @Test
  void should_pass_assert_error_path() {
    Assertions
              .assertThatCode(() -> {
                if (true) {
                  throw new IllegalArgumentException();
                } else {
                  return "unreachable";
                }
              })
              .raisesThrowableOfType(IllegalArgumentException.class);
  }

  @Test
  void should_fail_assert_on_exception_type_mismatch_on_error_path() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions
                                                                               .assertThatCode(() -> {
                                                                                 if (true) {
                                                                                   throw new IllegalArgumentException();
                                                                                 } else {
                                                                                   return "unreachable";
                                                                                 }
                                                                               })
                                                                               .raisesThrowableOfType(EOFException.class));
  }

  @Test
  void should_fail_assertion_on_exception() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions
                                                                               .assertThatCode(() -> {
                                                                                 throw new RuntimeException();
                                                                               })
                                                                               .doesNotThrowAnyException());
  }

  @Test
  void should_fail_on_evaluated_value_mismatch() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions
                                                                               .assertThatCode(() -> "one")
                                                                               .resultsInValueSatisfying(s -> s.equals("another")));
  }

  @Test
  void should_fail_with_description() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Assertions
                                                                               .assertThatCode(() -> "one")
                                                                               .describedAs("Some message")
                                                                               .as("Some message")
                                                                               .resultsInValueSatisfying(s -> s.equals("another")));
  }
}
