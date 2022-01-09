package org.assertj.core.internal.int2darrays;

import org.assertj.core.internal.Int2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Int2DArrays_assertNumberOfRows_Test extends Int2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    int2DArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
