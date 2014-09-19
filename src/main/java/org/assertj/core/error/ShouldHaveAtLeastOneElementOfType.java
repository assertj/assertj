package org.assertj.core.error;


/**
 * Creates an error message indicating that a group does not have an element of the given type.
 */
public class ShouldHaveAtLeastOneElementOfType extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveAtLeastOneElementOfType}</code>.
   * @param actual array or Iterable
   * @param expectedType the expected type of one element at least
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldHaveAtLeastOneElementOfType shouldHaveAtLeastOneElementOfType(Object actual, Class<?> expectedType) {
    return new ShouldHaveAtLeastOneElementOfType(actual, expectedType);
  }

  private ShouldHaveAtLeastOneElementOfType(Object actual, Class<?> expectedType) {
    super("%nExpecting:%n  <%s>%nto have at least one element of type:%n  <%s>%nbut had none.", actual, expectedType);
  }
}
