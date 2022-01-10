package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Assertions_sync_assertThatReflectiveOperationException_with_BDDAssertions_and_WithAssertions_Test
    extends BaseAssertionsTest {

  @Test
  void Assertions_assertThatReflectiveOperationException_and_BDDAssertions_thenReflectiveOperationException_should_be_the_same() {
    Method[] assertThatReflectiveOperationExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                      "assertThatReflectiveOperationException");
    Method[] thenReflectiveOperationExceptionInBDDAssertions = findMethodsWithName(BDDAssertions.class,
                                                                                   "thenReflectiveOperationException");

    assertThat(thenReflectiveOperationExceptionInBDDAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                                               .containsExactlyInAnyOrder(assertThatReflectiveOperationExceptionInAssertions);
  }

  @Test
  void Assertions_assertThatReflectiveOperationException_and_WithAssertions_assertThatReflectiveOperationException_should_be_the_same() {
    Method[] assertThatReflectiveOperationExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                      "assertThatReflectiveOperationException");
    Method[] assertThatReflectiveOperationExceptionInWithAssertions = findMethodsWithName(WithAssertions.class,
                                                                                          "assertThatReflectiveOperationException");

    assertThat(assertThatReflectiveOperationExceptionInWithAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                                      .containsExactlyInAnyOrder(assertThatReflectiveOperationExceptionInAssertions);
  }
}
