package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Assertions_sync_assertThatRuntimeException_with_BDDAssertions_and_WithAssertions_Test extends BaseAssertionsTest {

  @Test
  void Assertions_assertThatRuntimeException_and_BDDAssertions_thenRuntimeException_should_be_the_same() {
    Method[] assertThatRuntimeExceptionInAssertions = findMethodsWithName(Assertions.class, "assertThatRuntimeException");
    Method[] thenRuntimeExceptionInBDDAssertions = findMethodsWithName(BDDAssertions.class, "thenRuntimeException");

    assertThat(thenRuntimeExceptionInBDDAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                                   .containsExactlyInAnyOrder(assertThatRuntimeExceptionInAssertions);
  }

  @Test
  void Assertions_assertThatRuntimeException_and_WithAssertions_assertThatRuntimeException_should_be_the_same() {
    Method[] assertThatRuntimeExceptionInAssertions = findMethodsWithName(Assertions.class, "assertThatRuntimeException");
    Method[] assertThatRuntimeExceptionInWithAssertions = findMethodsWithName(WithAssertions.class, "assertThatRuntimeException");

    assertThat(assertThatRuntimeExceptionInWithAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                          .containsExactlyInAnyOrder(assertThatRuntimeExceptionInAssertions);
  }
}
