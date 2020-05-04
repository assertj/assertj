package org.assertj.core.error;

import java.util.Collection;

/**
 * @author phx
 */
public class ShouldHaveConstructors extends BasicErrorMessageFactory {
  /**
   * Creates a new <code>{@link ShouldHaveConstructors}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected expected parameters for this construction
   * @param missing missing parameters of the constructions for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory ShouldHaveConstructors(Class<?> actual,
                                                          Collection<Class<?>[]> expected,
                                                            Collection<Class<?>[]> missing) {
    return new ShouldHaveConstructors(actual, expected, missing);
  }

  private ShouldHaveConstructors(Class<?> actual, Collection<Class<?>[]> expected,
                                  Collection<Class<?>[]> missing) {
    super("%nExpecting%n  <%s>%nto have parameters:%n  <%s>%nbut the following parameters were " +
      "not found:%n  <%s>", actual, expected, missing);
  }
}
