package org.assertj.core.internal.boolean2darrays;

import org.assertj.core.internal.Boolean2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Boolean2DArrays_assertNumberOfRows_Test extends Boolean2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    boolean2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
