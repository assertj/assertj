/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
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
    String stackTraceDescription = stream(cause.getStackTrace()).limit(5)
                                                                .map(stackTraceElement -> format("\tat %s%n", stackTraceElement))
                                                                .collect(joining());
    return format("%s%n" +
                  "cause message: %s%n" +
                  "cause first five stack trace elements:%n" +
                  "%s",
                  throwable.getMessage(),
                  cause.getMessage(),
                  stackTraceDescription);
  };

  public static List<String> describeErrors(List<? extends Throwable> errors) {
    return extract(errors, ERROR_DESCRIPTION_EXTRACTOR);
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
   * <pre><code class='java'> org.junit.ComparisonFailure: expected:&lt;'[Ronaldo]'&gt; but was:&lt;'[Messi]'&gt;
   *   at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
   *   at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
   *   at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
   *   at java.lang.reflect.Constructor.newInstance(Constructor.java:501)
   *   at org.assertj.core.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:34)
   *   at org.assertj.core.error.ShouldBeEqual.newComparisonFailure(ShouldBeEqual.java:111)
   *   at org.assertj.core.error.ShouldBeEqual.comparisonFailure(ShouldBeEqual.java:103)
   *   at org.assertj.core.error.ShouldBeEqual.newAssertionError(ShouldBeEqual.java:81)
   *   at org.assertj.core.internal.Failures.failure(Failures.java:76)
   *   at org.assertj.core.internal.Objects.assertEqual(Objects.java:116)
   *   at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:74)
   *   at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:13)</code></pre>
   *
   * We get this:
   * <pre><code class='java'> org.junit.ComparisonFailure: expected:&lt;'[Ronaldo]'&gt; but was:&lt;'[Messi]'&gt;
   *   at examples.StackTraceFilterExample.main(StackTraceFilterExample.java:20)</code></pre>
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
          || className.startsWith("org.eclipse.jdt.internal.junit4.")
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
    String atLineNumber = format("at %s.%s(%s.java:%s)", testClassName, testName, testClassName, lineNumber);
    if (originalErrorMessage.contains(atLineNumber)) {
      return originalErrorMessage;
    }
    return format(originalErrorMessage.endsWith(format("%n")) ? "%s%s" : "%s%n%s", originalErrorMessage, atLineNumber);
  }

  private static String simpleClassNameOf(StackTraceElement testStackTraceElement) {
    String className = testStackTraceElement.getClassName();
    return className.substring(className.lastIndexOf('.') + 1);
  }

}
