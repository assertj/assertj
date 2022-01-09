package org.assertj.core.internal.long2darrays;

import org.assertj.core.internal.Long2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Long2DArrays_assertNumberOfRows_Test extends Long2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    long2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
