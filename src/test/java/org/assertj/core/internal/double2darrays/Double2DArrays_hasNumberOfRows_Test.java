package org.assertj.core.internal.double2darrays;

import org.assertj.core.internal.Double2DArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class Double2DArrays_hasNumberOfRows_Test extends Double2DArraysBaseTest {

  @Test
  void should_delegate_to_Arrays2D() {
    // WHEN
    double2dArrays.assertNumberOfRows(info, actual, 2);
    // THEN
    verify(arrays2d).assertNumberOfRows(info, failures, actual, 2);
  }

}
