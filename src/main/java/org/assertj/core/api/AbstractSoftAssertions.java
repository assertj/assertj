/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.util.List;

import org.assertj.core.internal.Failures;

public class AbstractSoftAssertions {

  protected final SoftProxies proxies;

  public AbstractSoftAssertions() {
    proxies = new SoftProxies();
  }

  public <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
    return proxies.create(assertClass, actualClass, actual);
  }

  /**
   * Fails with the given message.
   * 
   * @param failureMessage error message.
   * @since 2.6.0 / 3.6.0
   */
  public void fail(String failureMessage) {
    AssertionError error = Failures.instance().failure(failureMessage);
    proxies.collectError(error);
  }

  /**
   * Fails with the given message built like {@link String#format(String, Object...)}.
   * 
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @since 2.6.0 / 3.6.0
   */
  public void fail(String failureMessage, Object... args) {
    AssertionError error = Failures.instance().failure(String.format(failureMessage, args));
    proxies.collectError(error);
  }
  
  /**
   * Fails with the given message and with the {@link Throwable} that caused the failure.
   * 
   * @param failureMessage error message.
   * @param realCause cause of the error.
   * @since 2.6.0 / 3.6.0
   */
  public void fail(String failureMessage, Throwable realCause) {
    AssertionError error = Failures.instance().failure(failureMessage);
    error.initCause(realCause);
    proxies.collectError(error);
  }

  /**
   * Fails with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * 
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   * @since 2.6.0 / 3.6.0
   *
   * {@link Fail#shouldHaveThrown(Class)} can be used as a replacement.
   */
  public void failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
      shouldHaveThrown(throwableClass);
  }

  /**
   * Fails with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   * 
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   * @since 2.6.0 / 3.6.0
   */
  public void shouldHaveThrown(Class<? extends Throwable> throwableClass) {
      AssertionError error = Failures.instance().expectedThrowableNotThrown(throwableClass);
      proxies.collectError(error);
  }

  /**
   * Returns a copy of list of soft assertions collected errors.
   * @return a copy of list of soft assertions collected errors.
   */
  public List<Throwable> errorsCollected() {
    return addLineNumberToErrorMessages(proxies.errorsCollected());
  }

  /**
   * Returns the result of last soft assertion which can be used to decide what the next one should be.
   * <p>
   * Example :
   * <pre><code class='java'> Person person = ...
   * SoftAssertions soft = new SoftAssertions();
   * if (soft.assertThat(person.getAddress()).isNotNull().wasSuccess()) {
   *     soft.assertThat(person.getAddress().getStreet()).isNotNull();
   * }</code></pre>
   *
   * @return true if the last assertion was a success.
   */
  public boolean wasSuccess() {
    return proxies.wasSuccess();
  }

  private List<Throwable> addLineNumberToErrorMessages(List<Throwable> errors) {
    for (Throwable error : errors) {
      addLineNumberToErrorMessage(error);
    }
    return errors;
  }

  private void addLineNumberToErrorMessage(Throwable error) {
    StackTraceElement testStackTraceElement = getFirstStackTraceElementFromTest(error.getStackTrace());
    if (testStackTraceElement != null) {
      changeErrorMessage(error, buildErrorMessageWithLineNumber(error.getMessage(), testStackTraceElement));
    }
  }

  private String buildErrorMessageWithLineNumber(String originalErrorMessage, StackTraceElement testStackTraceElement) {
    String testClassName = simpleClassNameOf(testStackTraceElement);
    String testName = testStackTraceElement.getMethodName();
    int lineNumber = testStackTraceElement.getLineNumber();
    return format("%s%nat %s.%s(%s.java:%s)", originalErrorMessage, testClassName, testName, testClassName, lineNumber);
  }

  private void changeErrorMessage(Throwable error, String errorMessageWithLineNumber) {
    try {
      Field field = Throwable.class.getDeclaredField("detailMessage");
      field.setAccessible(true);
      field.set(error, errorMessageWithLineNumber);
    } catch (Exception ignored) {}
  }

  private String simpleClassNameOf(StackTraceElement testStackTraceElement) {
    String className = testStackTraceElement.getClassName();
    return className.substring(className.lastIndexOf('.') + 1);
  }

  private StackTraceElement getFirstStackTraceElementFromTest(StackTraceElement[] stacktrace) {
    for (StackTraceElement element : stacktrace) {
      String className = element.getClassName();
      if (className.startsWith("sun.reflect")
          || className.startsWith("jdk.internal.reflect")
          || className.startsWith("java.")
          || className.startsWith("javax.")
          || className.startsWith("org.junit.")
          || className.startsWith("org.eclipse.jdt.internal.junit.")
          || className.startsWith("org.eclipse.jdt.internal.junit4.")
          || className.startsWith("org.apache.maven.surefire")
          || className.startsWith("net.sf.cglib.proxy")
          || className.startsWith("org.assertj")) {
        continue;
      }
      return element;
    }
    return null;
  }
}
