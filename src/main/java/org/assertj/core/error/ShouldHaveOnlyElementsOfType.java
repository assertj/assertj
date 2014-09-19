package org.assertj.core.error;


/**
 * Creates an error message indicating that a group does not have an element of the given type.
 */
public class ShouldHaveOnlyElementsOfType extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveOnlyElementsOfType}</code>.
   * @param actual array or Iterable
   * @param expectedType the expected type of all elements
   * @param unexpectedType the type of one element that is not expectedType or it subclasses.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldHaveOnlyElementsOfType shouldHaveOnlyElementsOfType(Object actual, Class<?> expectedType, Class<?> unexpectedType) {
    return new ShouldHaveOnlyElementsOfType(actual, expectedType, unexpectedType);
  }

  private ShouldHaveOnlyElementsOfType(Object actual, Class<?> expectedType, Class<?> unexpectedType) {
    super("%nExpecting:%n  <%s>%nto only have elements of type:%n  <%s>%nbut found:%n  <%s>", actual, expectedType, unexpectedType);
  }
}
