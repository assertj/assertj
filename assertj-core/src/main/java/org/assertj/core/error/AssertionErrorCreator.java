/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.description.Description;
import org.assertj.core.internal.FileContent;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.Throwables;

/// Creates assertion errors, using OpenTest4J types when available.
public class AssertionErrorCreator {

  /// The [AssertionErrorCreator] instance.
  private static final AssertionErrorCreator INSTANCE = new AssertionErrorCreator(new ConstructorInvoker());

  /// Returns the singleton instance of this class.
  ///
  /// @return the singleton instance of this class.
  public static AssertionErrorCreator instance() {
    return INSTANCE;
  }

  private static final Class<?>[] MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR = array(String.class, Object.class, Object.class);
  private final Method valueWrapperCreateMethod;
  private final ConstructorInvoker constructorInvoker;
  private final Constructor<?> fileInfoConstructor;
  /// flag indicating whether or not we remove elements related to AssertJ from assertion error stack trace.
  private boolean removeAssertJRelatedElementsFromStackTrace = Configuration.REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE;

  AssertionErrorCreator(ConstructorInvoker constructorInvoker) {
    this.constructorInvoker = constructorInvoker;
    this.valueWrapperCreateMethod = getValueWrapperCreateMethod();
    this.fileInfoConstructor = getFileInfoConstructor();
  }

  private static Method getValueWrapperCreateMethod() {
    try {
      Class<?> valueWrapperClass = Class.forName("org.opentest4j.ValueWrapper");
      return valueWrapperClass.getMethod("create", Object.class, String.class);
    } catch (Exception e) {
      return null;
    }
  }

  private static Constructor<?> getFileInfoConstructor() {
    try {
      Class<?> fileInfoClass = Class.forName("org.opentest4j.FileInfo");
      return fileInfoClass.getConstructor(String.class, byte[].class);
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      return null;
    }
  }

  /// Sets whether we remove elements related to AssertJ from assertion error stack trace.
  ///
  /// @param removeAssertJRelatedElementsFromStackTrace flag
  public void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    ConfigurationProvider.loadRegisteredConfiguration();
    this.removeAssertJRelatedElementsFromStackTrace = removeAssertJRelatedElementsFromStackTrace;
  }

  /// Returns whether or not we remove elements related to AssertJ from assertion error stack trace.
  ///
  /// @return whether or not we remove elements related to AssertJ from assertion error stack trace.
  public boolean isRemoveAssertJRelatedElementsFromStackTrace() {
    return removeAssertJRelatedElementsFromStackTrace;
  }

  /// Creates an assertion error with actual and expected values.
  ///
  /// @param message the error message
  /// @param actual the actual value
  /// @param expected the expected value
  /// @param representation the value representation
  /// @return the assertion error
  public AssertionError assertionError(String message, Object actual, Object expected, Representation representation) {
    return assertionFailedError(message, actual, expected, representation).orElseGet(() -> assertionError(message));
  }

  private Optional<AssertionError> assertionFailedError(String message, Object actual, Object expected,
                                                        Representation representation) {
    try {
      Object o = constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                                MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR,
                                                message,
                                                valueWrapper(expected, representation),
                                                valueWrapper(actual, representation));

      if (o instanceof AssertionError error) {
        removeAssertJRelatedElementsFromStackTraceIfNeeded(error);
        return Optional.of(error);
      }

    } catch (@SuppressWarnings("unused") Throwable ignored) {}
    return Optional.empty();
  }

  private Object valueWrapper(Object value, Representation representation) {
    if (value instanceof FileContent fileContent && fileInfoConstructor != null) {
      try {
        return fileInfoConstructor.newInstance(fileContent.path(), fileContent.contents());
      } catch (Exception e) {
        return value; // best effort
      }
    }
    if (valueWrapperCreateMethod == null) return value;
    try {
      return valueWrapperCreateMethod.invoke(null, value, representation.toStringOf(value));
    } catch (Exception e) {
      return value; // best effort
    }
  }

  /// Creates an assertion error with the given message.
  ///
  /// @param message the error message
  /// @return the assertion error
  public AssertionError assertionError(String message) {
    AssertionError assertionError = new AssertionError(message);
    removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    return assertionError;
  }

  /**
   * Creates an assertion error with the given message, printing a thread dump if the assertion info requests it.
   *
   * @param info the assertion information
   * @param message the error message
   * @return the assertion error
   */
  public AssertionError assertionError(AssertionInfo info, String message) {
    var assertionError = assertionError(message);
    printThreadDumpIfNeeded(info);
    return assertionError;
  }

  /**
   * Creates an assertion error with actual and expected values, printing a thread dump if the assertion info requests it.
   *
   * @param info the assertion information, its representation is used to format the values
   * @param message the error message
   * @param actual the actual value
   * @param expected the expected value
   * @return the assertion error
   */
  public AssertionError assertionError(AssertionInfo info, String message, Object actual, Object expected) {
    var assertionError = assertionError(message, actual, expected, info.representation());
    printThreadDumpIfNeeded(info);
    return assertionError;
  }

  /**
   * Returns a description of all current threads.
   *
   * @return the thread dump description
   */
  public static String threadDumpDescription() {
    var lineSeparator = System.lineSeparator();
    var threadDumpDescription = new StringBuilder();
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
    for (ThreadInfo threadInfo : threadInfos) {
      threadDumpDescription.append(format("\"%s\"%n\tjava.lang.Thread.State: %s",
                                          threadInfo.getThreadName(), threadInfo.getThreadState()));
      for (StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
        threadDumpDescription.append(lineSeparator).append("\t\tat ").append(stackTraceElement);
      }
      threadDumpDescription.append(lineSeparator).append(lineSeparator);
    }
    return threadDumpDescription.toString();
  }

  // multiple assertions error

  /// Creates an error aggregating multiple assertion errors.
  ///
  /// @param errors the assertion errors
  /// @return the aggregate assertion error
  public AssertionError multipleAssertionsError(List<AssertionError> errors) {
    return multipleAssertionsError(null, null, errors);
  }

  /// Creates an error aggregating multiple assertion errors.
  ///
  /// @param description the assertion description
  /// @param objectUnderTest the object under test
  /// @param errors the assertion errors
  /// @return the aggregate assertion error
  public AssertionError multipleAssertionsError(Description description, Object objectUnderTest, List<AssertionError> errors) {
    MultipleAssertionsError multipleAssertionsError = new MultipleAssertionsError(description, objectUnderTest, errors);
    removeAssertJRelatedElementsFromStackTraceIfNeeded(multipleAssertionsError);
    return multipleAssertionsError;
  }

  /// If [#isRemoveAssertJRelatedElementsFromStackTrace()] is true, filters the stack trace of the given [AssertionError]
  /// by removing stack trace elements related to AssertJ in order to get a more readable stack trace.
  ///
  /// See example below:
  /// ```
  /// --------------- stack trace not filtered -----------------
  /// org.opentest4j.AssertionFailedError:
  /// expected: "messi"
  ///  but was: "ronaldo"
  ///
  /// at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
  /// at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499)
  /// at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:483)
  /// at org.assertj.core.error.ConstructorInvoker.newInstance(ConstructorInvoker.java:43)
  /// at org.assertj.core.error.AssertionErrorCreator.assertionFailedError(AssertionErrorCreator.java:111)
  /// at org.assertj.core.error.AssertionErrorCreator.assertionError(AssertionErrorCreator.java:105)
  /// at org.assertj.core.error.AssertionErrorCreator.assertionError(AssertionErrorCreator.java:175)
  /// at org.assertj.core.error.AssertionErrorCreator.assertionError(AssertionErrorCreator.java:333)
  /// at org.assertj.core.internal.Objects.assertEqual(Objects.java:327)
  /// at org.assertj.core.api.AbstractAssert.lambda$isEqualTo$0(AbstractAssert.java:501)
  /// at org.assertj.core.api.AbstractAssert.executeAssertion(AbstractAssert.java:155)
  /// at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:501)
  /// at org.assertj.core.api.AbstractStringAssert.isEqualTo(AbstractStringAssert.java:429)
  /// at com.example.PlayerTest.should_be_messi(PlayerTest.java:15)
  /// at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
  /// at java.base/java.lang.reflect.Method.invoke(Method.java:565)
  ///
  /// --------------- stack trace filtered -----------------
  /// org.opentest4j.AssertionFailedError:
  /// expected: "messi"
  ///  but was: "ronaldo"
  ///
  /// at com.example.PlayerTest.should_be_messi(PlayerTest.java:15)
  /// at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
  /// at java.base/java.lang.reflect.Method.invoke(Method.java:565)
  /// ```
  ///
  /// @param assertionError the `AssertionError` to filter stack trace if option is set.
  public void removeAssertJRelatedElementsFromStackTraceIfNeeded(AssertionError assertionError) {
    if (removeAssertJRelatedElementsFromStackTrace) {
      Throwables.removeAssertJRelatedElementsFromStackTrace(assertionError);
    }
  }

  /**
   * Creates an assertion error when an overriding message is configured.
   *
   * @param info     assertion information
   * @return the assertion error or {@code null}
   */
  public AssertionError errorWithOverriddenMessage(AssertionInfo info) {
    String overridingErrorMessage = info.overridingErrorMessage();
    if (isNullOrEmpty(overridingErrorMessage)) return null;
    String message = MessageFormatter.instance().format(info.description(), info.representation(), overridingErrorMessage);
    return assertionError(info, message);
  }

  /**
   * Creates the assertion error message.
   *
   * @param info           assertion information
   * @param messageFactory the error message factory
   * @return the assertion error message
   */
  public String assertionErrorMessage(AssertionInfo info, ErrorMessageFactory messageFactory) {
    String overridingErrorMessage = info.overridingErrorMessage();
    return isNullOrEmpty(overridingErrorMessage)
        ? messageFactory.create(info.description(), info.representation())
        : MessageFormatter.instance().format(info.description(), info.representation(), overridingErrorMessage);
  }

  /**
   * Creates a <code>{@link AssertionError}</code> for a {@link Throwable} class that was expected to be thrown.
   *
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return the created <code>{@link AssertionError}</code>.
   * @since 2.6.0 / 3.6.0
   */
  public AssertionError expectedThrowableNotThrown(Class<? extends Throwable> throwableClass) {
    return assertionError(format("%s should have been thrown", throwableClass.getSimpleName()));
  }

  /// Creates an [AssertionError] following this pattern:
  ///
  /// 1. creates an [AssertionError] using [AssertionInfo#overridingErrorMessage()] as the error message if such value
  ///    is not `null`, or
  /// 2. uses the given [ErrorMessageFactory] to create the detail message of the [AssertionError], prepending the value
  ///    of [AssertionInfo#description()] to the error message
  ///
  /// @param info contains information about the failed assertion.
  /// @param messageFactory knows how to create detail messages for `AssertionError`s.
  /// @return the created [AssertionError].
  public AssertionError assertionError(AssertionInfo info, ErrorMessageFactory messageFactory) {
    AssertionError error = errorWithOverriddenMessage(info);
    if (error != null) return error;
    String assertionErrorMessage = assertionErrorMessage(info, messageFactory);
    return assertionError(info, assertionErrorMessage);
  }

  /**
   * Creates an assertion error for an equality failure.
   *
   * @param info          assertion information
   * @param shouldBeEqual the equality error message
   * @return the assertion error
   */
  public AssertionError assertionError(AssertionInfo info, ShouldBeEqual shouldBeEqual) {
    AssertionError error = errorWithOverriddenMessage(info);
    if (error != null) return error;
    String message = shouldBeEqual.smartErrorMessage(info.description(), info.representation());
    return assertionError(info, message, shouldBeEqual.actual, shouldBeEqual.expected);
  }

  /**
   * Creates an assertion error with actual and expected values.
   *
   * @param info           assertion information
   * @param messageFactory the error message factory
   * @param actual         the actual value
   * @param expected       the expected value
   * @return the assertion error
   */
  public AssertionError assertionError(AssertionInfo info, ErrorMessageFactory messageFactory, Object actual, Object expected) {
    String assertionErrorMessage = assertionErrorMessage(info, messageFactory);
    return assertionError(info, assertionErrorMessage, actual, expected);
  }

  private static void printThreadDumpIfNeeded(AssertionInfo info) {
    if (info.printThreadDumpOnError()) System.err.println(threadDumpDescription());
  }
}
