package org.fest.assertions.util;

import static org.fest.assertions.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExceptionUtilsTest {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_IllegalArgumentException_with_given_message_if_condition_is_true() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("error message with arg1, arg2");
    throwIllegalArgumentExceptionIfTrue(true, "error message with %s, %s", "arg1", "arg2");
  }

  @Test
  public void should_not_throw_IllegalArgumentException_if_condition_is_false() {
    throwIllegalArgumentExceptionIfTrue(false, "some message");
  }

}
