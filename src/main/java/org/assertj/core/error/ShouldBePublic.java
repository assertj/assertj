package org.assertj.core.error;


import java.lang.reflect.Constructor;

/**
 * Creates an error message indicating that an assertion that verifies that a constructor is public
 *
 * @author phx
 */
public class ShouldBePublic extends BasicErrorMessageFactory {

  private ShouldBePublic(Constructor actual, String modifier) {
    super("%nExpecting <%s> to be a public%nthe modifier is <%s>", actual, modifier);
  }

  /**
   * Creates a new <code>{@link ShouldBePublic}</code>.
   *
   * @param actual the actual Constructor.
   */
  public static ErrorMessageFactory shouldBePublic(Constructor actual, String modifier) {
    return new ShouldBePublic(actual, modifier);
  }
}
