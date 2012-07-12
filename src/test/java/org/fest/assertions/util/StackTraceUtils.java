package org.fest.assertions.util;

public class StackTraceUtils {

  /**
   * Returns true if given {@link Throwable} stack trace contains Fest related elements, false otherwise.
   * @param throwable the {@link Throwable} we want to check stack trace for Fest related elements.
   * @return true if given {@link Throwable} stack trace contains Fest related elements, false otherwise.
   */
  public static boolean hasStackTraceElementRelatedToFest(Throwable throwable) {
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    for (StackTraceElement stackTraceElement : stackTrace) {
      if (stackTraceElement.getClassName().contains("org.fest")) { return true; }
    }
    return false;
  }

}
