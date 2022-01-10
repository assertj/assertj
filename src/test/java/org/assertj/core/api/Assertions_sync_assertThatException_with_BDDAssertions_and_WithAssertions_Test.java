package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Assertions_sync_assertThatException_with_BDDAssertions_and_WithAssertions_Test extends BaseAssertionsTest {

  @Test
  void Assertions_assertThatException_and_BDDAssertions_thenException_should_be_the_same() {
    Method[] assertThatExceptionInAssertions = findMethodsWithName(Assertions.class, "assertThatException");
    Method[] thenExceptionInBDDAssertions = findMethodsWithName(BDDAssertions.class, "thenException");

    assertThat(thenExceptionInBDDAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                            .containsExactlyInAnyOrder(assertThatExceptionInAssertions);
  }

  @Test
  void Assertions_assertThatException_and_WithAssertions_assertThatException_should_be_the_same() {
    Method[] assertThatExceptionInAssertions = findMethodsWithName(Assertions.class, "assertThatException");
    Method[] assertThatExceptionInWithAssertions = findMethodsWithName(WithAssertions.class, "assertThatException");

    assertThat(assertThatExceptionInWithAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                   .containsExactlyInAnyOrder(assertThatExceptionInAssertions);
  }
}
