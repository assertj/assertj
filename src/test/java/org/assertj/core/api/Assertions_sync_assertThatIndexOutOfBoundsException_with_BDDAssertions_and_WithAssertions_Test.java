package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Assertions_sync_assertThatIndexOutOfBoundsException_with_BDDAssertions_and_WithAssertions_Test
    extends BaseAssertionsTest {

  @Test
  void Assertions_assertThatIndexOutOfBoundsException_and_BDDAssertions_thenIndexOutOfBoundsException_should_be_the_same() {
    Method[] assertThatIndexOutOfBoundsExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                   "assertThatIndexOutOfBoundsException");
    Method[] thenIndexOutOfBoundsExceptionInBDDAssertions = findMethodsWithName(BDDAssertions.class,
                                                                                "thenIndexOutOfBoundsException");

    assertThat(thenIndexOutOfBoundsExceptionInBDDAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                                            .containsExactlyInAnyOrder(assertThatIndexOutOfBoundsExceptionInAssertions);
  }

  @Test
  void Assertions_assertThatIndexOutOfBoundsException_and_WithAssertions_assertThatIndexOutOfBoundsException_should_be_the_same() {
    Method[] assertThatIndexOutOfBoundsExceptionInAssertions = findMethodsWithName(Assertions.class,
                                                                                   "assertThatIndexOutOfBoundsException");
    Method[] assertThatIndexOutOfBoundsExceptionInWithAssertions = findMethodsWithName(WithAssertions.class,
                                                                                       "assertThatIndexOutOfBoundsException");

    assertThat(assertThatIndexOutOfBoundsExceptionInWithAssertions).usingElementComparator(IGNORING_DECLARING_CLASS_ONLY)
                                                                   .containsExactlyInAnyOrder(assertThatIndexOutOfBoundsExceptionInAssertions);
  }
}
