package org.assertj.core.internal.object2darrays;

import org.assertj.core.internal.Object2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Object2DArrays_assertNumberOfRows_Test extends Object2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    object2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
