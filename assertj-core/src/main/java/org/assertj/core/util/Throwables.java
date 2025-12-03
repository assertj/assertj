/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Utility methods related to <code>{@link Throwable}</code>s.
 *
 * @author Alex Ruiz
 * @author Daniel Zlotin
 */
public final class Throwables {

  private static final String ORG_ASSERTJ = "org.assert";
  private static final String JAVA_BASE = "java.";
  private static final String JDK_BASE = "jdk.";

  private Throwables() {}

  private static final Function<Throwable, String> ERROR_DESCRIPTION_EXTRACTOR = throwable -> {
    Throwable cause = throwable.getCause();
    if (cause == null) return throwable.getMessage();
    // error has a cause, display the cause message and the first stack trace elements.
    int maxStackTraceElements = StandardRepresentation.getMaxStackTraceElementsDisplayed();
    String stackTraceDescription = stream(cause.getStackTrace()).limit(maxStackTraceElements)
                                                                .map("\tat %s%n"::formatted)
                                                                .collect(joining());
    return format("%s%n" +
                  "cause message: %s%n" +
                  "cause first %d stack trace %s:%n" +
                  "%s",
                  throwable.getMessage(),
                  cause.getMessage(),
                  maxStackTraceElements,
                  maxStackTraceElements == 1 ? "element" : "elements",
                  stackTraceDescription);
  };

  public static List<String> describeErrors(List<? extends Throwable> errors) {
    return extract(errors, ERROR_DESCRIPTION_EXTRACTOR);
  }

  /**
   * Allows catching a {@link Throwable} more easily when used with Java 8 lambdas.
   *
   * @param shouldRaiseThrowable The lambda with the code that should raise the throwable.
   * @return The captured throwable or null if no throwable was raised.
   */
  public static Throwable catchThrowable(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
    try {
      shouldRaiseThrowable.call();
    } catch (Throwable throwable) {
      return throwable;
    }
    return null;
  }

  /**
   * Appends the stack trace of the current thread to the one in the given <code>{@link Throwable}</code>.
   *
   * @param t the given {@code Throwable}.
   * @param methodToStartFrom the name of the method used as the starting point of the current thread's stack trace.
   */
  public static void appendStackTraceInCurrentThreadToThrowable(Throwable t, String methodToStartFrom) {
    List<StackTraceElement> stackTrace = newArrayList(t.getStackTrace());
    stackTrace.addAll(stackTraceInCurrentThread(methodToStartFrom));
    t.setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
  }

  private static List<StackTraceElement> stackTraceInCurrentThread(String methodToStartFrom) {
    List<StackTraceElement> filtered = stackTraceInCurrentThread();
    List<StackTraceElement> toRemove = new ArrayList<>();
    for (StackTraceElement e : filtered) {
      if (methodToStartFrom.equals(e.getMethodName())) {
        break;
      }
      toRemove.add(e);
    }
    filtered.removeAll(toRemove);
    return filtered;
  }

  private static List<StackTraceElement> stackTraceInCurrentThread() {
    return newArrayList(Thread.currentThread().getStackTrace());
  }

  /**
   * Removes the AssertJ-related elements from the <code>{@link Throwable}</code> stack trace that have little value for
   * end user, that is assertj elements and the ones coming from assertj (for example assertj calling some java jdk
   * classes to build assertion errors dynamically).
   * <p>
   * Therefore, instead of seeing this:
   * <pre><code class='java'> --------------- stack trace not filtered -----------------
   org.opentest4j.AssertionFailedError:
   expected: "messi"
   but was: "ronaldo"
  
   at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
   at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
   at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
   at org.assertj.core/org.assertj.core.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:28)
   at org.assertj.core/org.assertj.core.error.ShouldBeEqual.assertionFailedError(ShouldBeEqual.java:208)
   at org.assertj.core/org.assertj.core.error.ShouldBeEqual.toAssertionError(ShouldBeEqual.java:113)
   at org.assertj.core/org.assertj.core.internal.Failures.failure(Failures.java:88)
   at org.assertj.core/org.assertj.core.internal.Objects.assertEqual(Objects.java:214)
  
   --------------- stack trace filtered -----------------
   org.opentest4j.AssertionFailedError:
   expected: "messi"
   but was: "ronaldo"
  
   at java.base/java.lang.reflect.Method.invoke(Method.java:580)
   at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
   at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)</code></pre>
  
   * @param throwable the {@code Throwable} to filter stack trace.
   */
  public static void removeAssertJRelatedElementsFromStackTrace(Throwable throwable) {
    if (throwable == null) return;
    List<StackTraceElement> purgedStack = list();
    boolean firstAssertjStackTraceElementFound = false;
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    // traverse stack from the root element (main program) as it makes it easier to identify the first assertj element
    // then we ignore all assertj and java or jdk elements.
    for (int i = stackTrace.length - 1; i >= 0; i--) {
      StackTraceElement stackTraceElement = stackTrace[i];
      if (isFromAssertJ(stackTraceElement)) {
        firstAssertjStackTraceElementFound = true;
        continue; // skip element
      }
      if (!firstAssertjStackTraceElementFound) {
        // keep everything before first assertj stack trace element
        purgedStack.add(stackTraceElement);
      } else {
        // we already are in assertj stack, so now we also ignore java elements too as they come from assertj
        if (!isFromJavaOrJdkPackages(stackTraceElement)) purgedStack.add(stackTraceElement);
      }
    }
    reverse(purgedStack); // reverse as we traversed the stack in reverse order when purging it.
    throwable.setStackTrace(purgedStack.toArray(new StackTraceElement[0]));
  }

  private static boolean isFromAssertJ(StackTraceElement stackTrace) {
    return stackTrace.getClassName().contains(ORG_ASSERTJ);
  }

  private static boolean isFromJavaOrJdkPackages(StackTraceElement stackTrace) {
    String className = stackTrace.getClassName();
    return className.contains(JAVA_BASE) || className.contains(JDK_BASE);
  }

  /**
   * Get the root cause (i.e., the last non-null cause) from a {@link Throwable}.
   *
   * @param throwable the {@code Throwable} to get root cause from.
   * @return the root cause if any, else {@code null}.
   */
  public static Throwable getRootCause(Throwable throwable) {
    if (throwable.getCause() == null) return null;
    Throwable cause;
    while ((cause = throwable.getCause()) != null)
      throwable = cause;
    return throwable;
  }

  /**
   * Get the stack trace from a {@link Throwable} as a {@link String}.
   *
   * <p>
   * The result of this method vary by JDK version as this method uses
   * {@link Throwable#printStackTrace(java.io.PrintWriter)}. On JDK1.3 and earlier, the cause exception will not be
   * shown unless the specified throwable alters printStackTrace.
   * </p>
   *
   * @param throwable the {@code Throwable} to get stack trace from.
   * @return the stack trace as a {@link String}.
   */
  public static String getStackTrace(Throwable throwable) {
    StringWriter sw = null;
    PrintWriter pw = null;
    try {
      sw = new StringWriter();
      pw = new PrintWriter(sw, true);
      throwable.printStackTrace(pw);
      return sw.getBuffer().toString();
    } finally {
      Closeables.closeQuietly(sw, pw);
    }
  }

  public static <T extends Throwable> List<T> addLineNumberToErrorMessages(List<? extends T> errors) {
    return errors.stream()
                 .map(Throwables::addLineNumberToErrorMessage)
                 .collect(toList());
  }

  public static StackTraceElement getFirstStackTraceElementFromTest(StackTraceElement[] stacktrace) {
    for (StackTraceElement element : stacktrace) {
      String className = element.getClassName();
      if (isProxiedAssertionClass(className)
          || className.startsWith("sun.reflect")
          || className.startsWith("jdk.internal.reflect")
          || className.startsWith("java.")
          || className.startsWith("javax.")
          || className.startsWith("org.junit.")
          || className.startsWith("org.eclipse.jdt.internal.junit.")
          || className.startsWith("org.eclipse.jdt.internal.junit5.")
          || className.startsWith("com.intellij.junit5.")
          || className.startsWith("com.intellij.rt.execution.junit.")
          || className.startsWith("com.intellij.rt.junit.") // since IntelliJ IDEA build 193.2956.37
          || className.startsWith("org.apache.maven.surefire")
          || className.startsWith("org.pitest.")
          || className.startsWith("org.assertj")) {
        continue;
      }
      return element;
    }
    return null;
  }

  private static boolean isProxiedAssertionClass(String className) {
    return className.contains("$ByteBuddy$");
  }

  private static <T extends Throwable> T addLineNumberToErrorMessage(T error) {
    StackTraceElement testStackTraceElement = Throwables.getFirstStackTraceElementFromTest(error.getStackTrace());
    if (testStackTraceElement != null) {
      try {
        return createNewInstanceWithLineNumberInErrorMessage(error, testStackTraceElement);
      } catch (@SuppressWarnings("unused") SecurityException | ReflectiveOperationException ignored) {}
    }
    return error;
  }

  private static <T extends Throwable> T createNewInstanceWithLineNumberInErrorMessage(T error,
                                                                                       StackTraceElement testStackTraceElement) throws ReflectiveOperationException {
    T errorWithLineNumber = isOpentest4jAssertionFailedError(error)
        ? buildOpentest4jAssertionFailedErrorWithLineNumbers(error, testStackTraceElement)
        : buildAssertionErrorWithLineNumbersButNoActualOrExpectedValues(error, testStackTraceElement);
    errorWithLineNumber.setStackTrace(error.getStackTrace());
    Stream.of(error.getSuppressed()).forEach(errorWithLineNumber::addSuppressed);
    return errorWithLineNumber;
  }

  private static <T extends Throwable> boolean isOpentest4jAssertionFailedError(T error) {
    return isInstanceOf(error, "org.opentest4j.AssertionFailedError");
  }

  private static boolean isInstanceOf(Object object, String className) {
    try {
      Class<?> type = Class.forName(className);
      return type.isInstance(object);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private static <T extends Throwable> T buildAssertionErrorWithLineNumbersButNoActualOrExpectedValues(T error,
                                                                                                       StackTraceElement testStackTraceElement) throws ReflectiveOperationException {
    @SuppressWarnings("unchecked")
    Constructor<? extends T> constructor = (Constructor<? extends T>) error.getClass().getConstructor(String.class,
                                                                                                      Throwable.class);
    return constructor.newInstance(buildErrorMessageWithLineNumber(error.getMessage(), testStackTraceElement), error.getCause());
  }

  private static <T extends Throwable> T buildOpentest4jAssertionFailedErrorWithLineNumbers(T error,
                                                                                            StackTraceElement testStackTraceElement) throws ReflectiveOperationException {
    // AssertionFailedError has actual and expected fields of type ValueWrapper
    Object actualWrapper = byName("actual").apply(error);
    Object expectedWrapper = byName("expected").apply(error);
    if (actualWrapper != null && expectedWrapper != null) {
      // try to call AssertionFailedError(String message, Object expected, Object actual, Throwable cause)
      try {
        Object actual = byName("value").apply(actualWrapper);
        Object expected = byName("value").apply(expectedWrapper);

        @SuppressWarnings("unchecked")
        Constructor<? extends T> constructor = (Constructor<? extends T>) error.getClass().getConstructor(String.class,
                                                                                                          Object.class,
                                                                                                          Object.class,
                                                                                                          Throwable.class);
        return constructor.newInstance(buildErrorMessageWithLineNumber(error.getMessage(), testStackTraceElement),
                                       expected,
                                       actual,
                                       error.getCause());
      } catch (IntrospectionError e) {
        // fallback to AssertionFailedError(String message, Throwable cause) constructor
      }
    }
    return buildAssertionErrorWithLineNumbersButNoActualOrExpectedValues(error, testStackTraceElement);
  }

  private static String buildErrorMessageWithLineNumber(String originalErrorMessage, StackTraceElement testStackTraceElement) {
    String testClassName = simpleClassNameOf(testStackTraceElement);
    String testName = testStackTraceElement.getMethodName();
    int lineNumber = testStackTraceElement.getLineNumber();
    String atLineNumber = "at %s.%s(%s.java:%s)".formatted(testClassName, testName, testClassName, lineNumber);
    if (originalErrorMessage.contains(atLineNumber)) {
      return originalErrorMessage;
    }
    return format(originalErrorMessage.endsWith("%n".formatted()) ? "%s%s" : "%s%n%s", originalErrorMessage, atLineNumber);
  }

  private static String simpleClassNameOf(StackTraceElement testStackTraceElement) {
    String className = testStackTraceElement.getClassName();
    return className.substring(className.lastIndexOf('.') + 1);
  }

}
