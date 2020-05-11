package org.assertj.core.error;


import java.lang.reflect.Constructor;

/**
 * Creates an error message indicating that an assertion that verifies that a constructor is
 * protected
 *
 * @author phx
 */
public class ShouldBeProtected extends BasicErrorMessageFactory {

  private ShouldBeProtected(Constructor actual, String modifier) {
    super("%nExpecting <%s> to be a Protected%nbut the modifier is <%s>", actual, modifier);
  }

  /**
   * Creates a new <code>{@link ShouldBeProtected}</code>.
   *
   * @param actual the actual Constructor.
   */
  public static ErrorMessageFactory ShouldBeProtected(Constructor actual, String modifier) {
    return new ShouldBeProtected(actual, modifier);
  }
}
