package org.assertj.core.internal.short2darrays;

import org.assertj.core.internal.Short2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Short2DArrays_hasNumberOfRows_Test extends Short2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    short2DArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
