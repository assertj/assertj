package org.assertj.core.util;

public class StackTraceUtils {

  /**
   * Returns true if given {@link Throwable} stack trace contains AssertJ related elements, false otherwise.
   *
   * @param throwable the {@link Throwable} we want to check stack trace for AssertJ related elements.
   * @return true if given {@link Throwable} stack trace contains AssertJ related elements, false otherwise.
   */
  public static boolean hasStackTraceElementRelatedToAssertJ(Throwable throwable) {
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    for (StackTraceElement stackTraceElement : stackTrace) {
      if (stackTraceElement.getClassName().contains("org.assert")) return true;
    }
    return false;
  }

}
