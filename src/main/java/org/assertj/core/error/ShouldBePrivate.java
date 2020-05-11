package org.assertj.core.error;


import java.lang.reflect.Constructor;

/**
 * Creates an error message indicating that an assertion that verifies that a constructor is
 * private
 *
 * @author phx
 */
public class ShouldBePrivate extends BasicErrorMessageFactory {

  private ShouldBePrivate(Constructor actual, String modifier) {
    super("%nExpecting <%s> to be a Private%nbut the modifier is <%s>", actual, modifier);
  }

  /**
   * Creates a new <code>{@link ShouldBePrivate}</code>.
   *
   * @param actual the actual Constructor.
   */
  public static ErrorMessageFactory shouldBePrivate(Constructor actual, String modifier) {
    return new ShouldBePrivate(actual, modifier);
  }
}
