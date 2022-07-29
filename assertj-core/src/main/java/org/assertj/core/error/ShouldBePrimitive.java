package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a class is (or not) a primitive data type,
 * i.e, byte, short, int, long, float, double, char and boolean.
 *
 * @author Manuel Gutierrez
 */
public class ShouldBePrimitive extends BasicErrorMessageFactory {

  /**
   * Creates a new instance of <code>{@link ShouldBePrimitive }</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static BasicErrorMessageFactory shouldBePrimitive(Class<?> actual) {
    return new ShouldBePrimitive(actual, true);
  }

  /**
   * Creates a new instance of <code>{@link ShouldBePrimitive}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static BasicErrorMessageFactory shouldNotBePrimitive(Class<?> actual) {
    return new ShouldBePrimitive(actual, false);
  }

  private ShouldBePrimitive(Class<?> actual, boolean toBeOrNotToBe) {
    super("%nExpecting%n  %s%n" + (toBeOrNotToBe ? "" : " not ") + "to be a primitive data type", actual);
  }
}
