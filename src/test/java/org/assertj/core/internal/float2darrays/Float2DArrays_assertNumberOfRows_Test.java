package org.assertj.core.internal.float2darrays;

import org.assertj.core.internal.Float2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Float2DArrays_assertNumberOfRows_Test extends Float2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    float2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
