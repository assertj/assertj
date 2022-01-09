package org.assertj.core.internal.char2darrays;

import org.assertj.core.internal.Char2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Char2DArrays_assertNumberOfRows_Test extends Char2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    char2DArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
