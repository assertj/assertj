package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ThrowableAssert_extracting_with_Function_Test {

  @Test
  void should_allow_type_specific_extractor() {
    // GIVEN
    Exception cause = new Exception("boom!");
    ClassNotFoundException exception = new ClassNotFoundException("message", cause);
    // WHEN/THEN
    assertThat(exception).extracting(ClassNotFoundException::getException)
                         .isSameAs(cause);
  }

}
