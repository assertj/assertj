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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.stacktraceelement.ShouldBeNative.shouldBeNative;
import static org.assertj.core.error.stacktraceelement.ShouldHaveClassName.shouldHaveClassName;
import static org.assertj.core.error.stacktraceelement.ShouldHaveFileName.shouldHaveFileName;
import static org.assertj.core.error.stacktraceelement.ShouldHaveLineNumber.shouldHaveLineNumber;
import static org.assertj.core.error.stacktraceelement.ShouldHaveMethodName.shouldHaveMethodName;
import static org.assertj.core.error.stacktraceelement.ShouldNotBeNative.shouldNotBeNative;

import java.util.Objects;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.CheckReturnValue;

/**
 * Base class for all implementations of assertions for
 * {@link StackTraceElement stack trace elements}.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @author Ashley Scopes
 */
public abstract class AbstractStackTraceElementAssert<SELF extends AbstractStackTraceElementAssert<SELF>>
  extends AbstractObjectAssert<SELF, StackTraceElement> {

  private final Failures failures;

  /**
   * Initialize this assertion.
   *
   * @param actual   the stack trace element to perform assertions upon.
   * @param selfType the type of the implementation of this class.
   */
  protected AbstractStackTraceElementAssert(StackTraceElement actual, Class<?> selfType) {
    super(actual, selfType);
    failures = Failures.instance();
  }

  /**
   * Perform an assertion on the file name for the stack trace frame.
   *
   * <p>This information may not be available in some cases. In these scenarios, the value
   * will be {@code null}.
   *
   * @return the string assertions to perform on the file name.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @see StackTraceElement#getFileName()
   */
  @CheckReturnValue
  public AbstractStringAssert<?> fileName() {
    objects.assertNotNull(info, actual);
    return toAssert(actual.getFileName());
  }

  /**
   * Assert that this stack trace element has the given file name.
   *
   * @param fileName the file name to assert against (can be {@code null}).
   * @return {@code this} assertions object for further assertions.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element has a different
   *        {@link StackTraceElement#getFileName() file name} to the provided
   *        {@code fileName}.
   */
  public SELF hasFileName(String fileName) {
    objects.assertNotNull(info, actual);
    String actualFileName = actual.getFileName();

    // fileName can be null, as can actualFileName. Use a null-safe equality check.
    if (!Objects.equals(actualFileName, fileName)) {
      throw failures.failure(
        info,
        shouldHaveFileName(actual, fileName),
        actualFileName,
        fileName
      );
    }

    return myself;
  }

  /**
   * Perform an assertion on the line number for the stack trace frame.
   *
   * <p>This information may not be available in some cases, including for native code.
   * In these scenarios, the value will be {@code null}. If you wish to assert on the raw value
   * instead, consider using {@link #rawLineNumber()}.
   *
   * @return the integer assertions to perform on the line number.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @see StackTraceElement#getLineNumber()
   * @see #rawLineNumber()
   */
  @CheckReturnValue
  public AbstractIntegerAssert<?> lineNumber() {
    objects.assertNotNull(info, actual);

    // Negative numbers imply the information is unavailable.
    Integer lineNumber = actual.getLineNumber() < 0
      ? null
      : actual.getLineNumber();

    return toAssert(lineNumber);
  }

  /**
   * Perform an assertion on the raw value for the line number.
   *
   * <p>This will never be null, and values for native code may be JVM dependant.
   *
   * @return the integer assertions to perform on the line number.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @see StackTraceElement#getLineNumber()
   * @see #lineNumber()
   */
  @CheckReturnValue
  public AbstractIntegerAssert<?> rawLineNumber() {
    objects.assertNotNull(info, actual);
    return toAssert(actual.getLineNumber());
  }

  /**
   * Assert that this stack trace element has the given line number.
   *
   * @param lineNumber the line number to assert against.
   * @return {@code this} assertions object for further assertions.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element has a different
   *        {@link StackTraceElement#getLineNumber() line number} to the provided
   *        {@code lineNumber}.
   */
  public SELF hasLineNumber(int lineNumber) {
    objects.assertNotNull(info, actual);
    int actualLineNumber = actual.getLineNumber();

    if (actualLineNumber != lineNumber) {
      throw failures.failure(
        info,
        shouldHaveLineNumber(actual, lineNumber),
        actualLineNumber,
        lineNumber
      );
    }

    return myself;
  }

  /**
   * Perform an assertion on the class name for the stack trace frame.
   *
   * <p>This should always be present.
   *
   * @return the string assertions to perform on the class name.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @see StackTraceElement#getClassName()
   */
  @CheckReturnValue
  public AbstractStringAssert<?> className() {
    objects.assertNotNull(info, actual);
    return toAssert(actual.getClassName());
  }

  /**
   * Assert that this stack trace element has the given class name.
   *
   * @param className the class name to assert against.
   * @return {@code this} assertions object for further assertions.
   * @throws NullPointerException if the {@code className} parameter is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element has a different
   *        {@link StackTraceElement#getClassName() class name} to the provided
   *        {@code className}.
   */
  public SELF hasClassName(String className) {
    requireNonNull(className, "className must not be null");
    objects.assertNotNull(info, actual);
    String actualClassName = actual.getClassName();

    if (!actualClassName.equals(className)) {
      throw failures.failure(
        info,
        shouldHaveClassName(actual, className),
        actualClassName,
        className
      );
    }

    return myself;
  }

  /**
   * Perform an assertion on the method name for the stack trace frame.
   *
   * <p>This should always be present.
   *
   * @return the string assertions to perform on the method name.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @see StackTraceElement#getMethodName()
   */
  @CheckReturnValue
  public AbstractStringAssert<?> methodName() {
    objects.assertNotNull(info, actual);
    return toAssert(actual.getMethodName());
  }

  /**
   * Assert that this stack trace element has the given method name.
   *
   * @param methodName the method name to assert against.
   * @return {@code this} assertions object for further assertions.
   * @throws NullPointerException if the {@code methodName} parameter is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element has a different
   *        {@link StackTraceElement#getMethodName() method name} to the provided
   *        {@code methodName}.
   */
  public SELF hasMethodName(String methodName) {
    requireNonNull(methodName, "methodName must not be null");
    objects.assertNotNull(info, actual);
    String actualMethodName = actual.getMethodName();

    if (!actualMethodName.equals(methodName)) {
      throw failures.failure(
        info,
        shouldHaveMethodName(actual, methodName),
        actualMethodName,
        methodName
      );
    }

    return myself;
  }

  /**
   * Assert that the stack trace frame is for a native method.
   *
   * @return {@code this} assertions object.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element is for a non-native method.
   * @see StackTraceElement#isNativeMethod()
   */
  public SELF isNativeMethod() {
    objects.assertNotNull(info, actual);
    if (!actual.isNativeMethod()) {
      throw failures.failure(info, shouldBeNative(actual));
    }
    return myself;
  }

  /**
   * Assert that the stack trace frame is for a non-native method.
   *
   * @return {@code this} assertions object.
   * @throws AssertionError if the {@code actual} stack trace element is {@code null}.
   * @throws AssertionError if the {@code actual} stack trace element is for a native method.
   * @see StackTraceElement#isNativeMethod()
   */
  public SELF isNotNativeMethod() {
    objects.assertNotNull(info, actual);
    if (actual.isNativeMethod()) {
      throw failures.failure(info, shouldNotBeNative(actual));
    }
    return myself;
  }

  /**
   * Create an assertion for the given string value and return it.
   *
   * @param string the string value to create an assertion for.
   * @return the generated assertion.
   */
  @CheckReturnValue
  protected abstract AbstractStringAssert<?> toAssert(String string);

  /**
   * Create an assertion for the given integer value and return it.
   *
   * @param integer the integer value to create an assertion for.
   * @return the generated assertion.
   */
  @CheckReturnValue
  protected abstract AbstractIntegerAssert<?> toAssert(Integer integer);
}
