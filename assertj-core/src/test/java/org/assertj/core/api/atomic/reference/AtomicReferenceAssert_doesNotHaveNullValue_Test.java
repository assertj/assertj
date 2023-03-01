package org.assertj.core.api.atomic.reference;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class AtomicReferenceAssert_doesNotHaveNullValue_Test {

  @Test
  void should_pass_when_actual_does_not_have_the_null_value() {
    AtomicReference<String> actual = new AtomicReference<>("foo");
    assertThat(actual).doesNotHaveNullValue();
  }

  @Test
  void should_fail_when_actual_has_the_null_value() {
    AtomicReference<String> actual = new AtomicReference<>(null);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).doesNotHaveNullValue())
                                                   .withMessage(actualIsNull());
  }

}
