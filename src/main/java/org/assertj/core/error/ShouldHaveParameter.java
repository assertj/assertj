package org.assertj.core.error;

import java.lang.reflect.Constructor;

/**
 * Creates an error message indicating that an assertion that verifies that a constructor contain
 * list of classes
 *
 * @author phx
 */
public class ShouldHaveParameter extends BasicErrorMessageFactory {

  public ShouldHaveParameter(Constructor actual, Class<?>[] expect, Class<?>[] miss) {
    super("%nExpecting <%s> to have parameters <%s>%nbut the parameters are <%s>", actual, expect,
      miss);
  }

  /**
   * Creates a new <code>{@link ShouldHaveParameter}</code>.
   *
   * @param actual the actual Constructor.
   */
  public static ErrorMessageFactory shouldHaveParameter(Constructor actual, Class<?>[] expect,
    Class<?>... miss) {
    return new ShouldHaveParameter(actual, expect, miss);
  }
}
