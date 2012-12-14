package org.fest.assertions.util;

import static java.lang.String.format;

public class ExceptionUtils {

  /**
   * Throws a {@link IllegalArgumentException} if given condition is true with message formatted with given arguments
   * using {@link String#format(String, Object...)}.
   * 
   * @param condition condition that will trigger the {@link IllegalArgumentException} if true
   * @param exceptionMessage message set in thrown IllegalArgumentException
   * @param exceptionMessageArgs arguments to be used to format exceptionMessage
   * @throws IllegalArgumentException if condition is true
   */
  public static void throwIllegalArgumentExceptionIfTrue(boolean condition, String exceptionMessage,
      Object... exceptionMessageArgs) {
    if (condition) {
      throw new IllegalArgumentException(format(exceptionMessage, exceptionMessageArgs));
    }
  }

}
