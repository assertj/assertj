package org.assertj.core.tests.java17.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

public class AssertionErrors {

  public static AssertionError expectAssertionError(ThrowingCallable shouldRaiseAssertionError) {
    AssertionError error = catchThrowableOfType(shouldRaiseAssertionError, AssertionError.class);
    assertThat(error).as("The code under test should have raised an AssertionError").isNotNull();
    return error;
  }

}
