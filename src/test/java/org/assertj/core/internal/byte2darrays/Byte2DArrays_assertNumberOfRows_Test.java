package org.assertj.core.internal.byte2darrays;

import org.assertj.core.internal.Byte2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Byte2DArrays_assertNumberOfRows_Test extends Byte2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    byte2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
