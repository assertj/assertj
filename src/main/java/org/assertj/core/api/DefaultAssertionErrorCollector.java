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

import static java.lang.String.format;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.extractor.Extractors.byName;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.util.introspection.IntrospectionError;

public class DefaultAssertionErrorCollector implements AssertionErrorCollector {

  // Marking this field as volatile doesn't ensure complete thread safety
  // (mutual exclusion, race-free behaviour), but guarantees eventual visibility
  private volatile boolean wasSuccess = true;
  private List<AssertionError> collectedAssertionErrors = synchronizedList(new ArrayList<>());

  private AfterAssertionErrorCollected callback = this;

  private AssertionErrorCollector delegate = null;

  public DefaultAssertionErrorCollector() {
    super();
  }

  // I think ideally, this would be set in the constructor and made final;
  // however that would require a new constructor that would not make it
  // backward compatible with existing SoftAssertionProvider implementations.
  @Override
  public void setDelegate(AssertionErrorCollector delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<AssertionErrorCollector> getDelegate() {
    return Optional.ofNullable(delegate);
  }

  @Override
  public void collectAssertionError(AssertionError error) {
    if (delegate == null) {
      collectedAssertionErrors.add(error);
      wasSuccess = false;
    } else {
      delegate.collectAssertionError(error);
    }
    callback.onAssertionErrorCollected(error);
  }

  /**
   * Returns a list of soft assertions collected errors. If a delegate
   * has been set (see {@link #setDelegate(AssertionErrorCollector) setDelegate()},
   * then this method will return the result of the delegate's {@code assertErrorsCollected()}.
   *
   * @return A list of soft assertions collected errors.
   */
  @Override
  public List<AssertionError> assertionErrorsCollected() {
    List<AssertionError> errors = delegate != null ? delegate.assertionErrorsCollected()
        : unmodifiableList(collectedAssertionErrors);
    return decorateErrorsCollected(errors);
  }

  /**
   * Register a callback allowing to react after an {@link AssertionError} is collected by the current soft assertion.
   * <p>
   * The callback is an instance of {@link AfterAssertionErrorCollected} which can be expressed as lambda.
   * <p>
   * Example:
   * <pre><code class='java'> SoftAssertions softly = new SoftAssertions();
   * StringBuilder reportBuilder = new StringBuilder(format("Assertions report:%n"));
  
   * // register our callback
   * softly.setAfterAssertionErrorCollected(error -&gt; reportBuilder.append(String.format("------------------%n%s%n", error.getMessage())));
   *
   * // the AssertionError corresponding to the failing assertions are registered in the report
   * softly.assertThat("The Beatles").isEqualTo("The Rolling Stones");
   * softly.assertThat(123).isEqualTo(123)
   *                       .isEqualTo(456);</code></pre>
   * <p>
   * resulting {@code reportBuilder}:
   * <pre><code class='java'> Assertions report:
   * ------------------
   * Expecting:
   *  &lt;"The Beatles"&gt;
   * to be equal to:
   *  &lt;"The Rolling Stones"&gt;
   * but was not.
   * ------------------
   * Expecting:
   *  &lt;123&gt;
   * to be equal to:
   *  &lt;456&gt;
   * but was not.</code></pre>
   * <p>
   * Alternatively, if you have defined your own SoftAssertions subclass and inherited from {@link AbstractSoftAssertions},
   * the only thing you have to do is to override {@link AfterAssertionErrorCollected#onAssertionErrorCollected(AssertionError)}.
   *
   * @param afterAssertionErrorCollected the callback.
   *
   * @since 3.17.0
   */
  public void setAfterAssertionErrorCollected(AfterAssertionErrorCollected afterAssertionErrorCollected) {
    callback = afterAssertionErrorCollected;
  }

  @Override
  public void succeeded() {
    if (delegate == null) {
      wasSuccess = true;
    } else {
      delegate.succeeded();
    }
  }

  @Override
  public boolean wasSuccess() {
    return delegate == null ? wasSuccess : delegate.wasSuccess();
  }

  /**
   * Modifies collected errors. Override to customize modification.
   * @param <T> the supertype to use in the list return value
   * @param errors list of errors to decorate
   * @return decorated list
  */
  protected <T extends Throwable> List<T> decorateErrorsCollected(List<? extends T> errors) {
    return addLineNumberToErrorMessages(errors);
  }

  private static <T extends Throwable> List<T> addLineNumberToErrorMessages(List<? extends T> errors) {
    return errors.stream()
                 .map(DefaultAssertionErrorCollector::addLineNumberToErrorMessage)
                 .collect(toList());
  }

  private static <T extends Throwable> T addLineNumberToErrorMessage(T error) {
    StackTraceElement testStackTraceElement = getFirstStackTraceElementFromTest(error.getStackTrace());
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
    Stream.of(error.getSuppressed()).forEach(suppressed -> errorWithLineNumber.addSuppressed(suppressed));
    return errorWithLineNumber;
  }

  private static <T extends Throwable> boolean isOpentest4jAssertionFailedError(T error) {
    return "org.opentest4j.AssertionFailedError".equals(error.getClass().getName());
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
    return format("%s%nat %s.%s(%s.java:%s)", originalErrorMessage, testClassName, testName, testClassName, lineNumber);
  }

  private static String simpleClassNameOf(StackTraceElement testStackTraceElement) {
    String className = testStackTraceElement.getClassName();
    return className.substring(className.lastIndexOf('.') + 1);
  }

  private static StackTraceElement getFirstStackTraceElementFromTest(StackTraceElement[] stacktrace) {
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

}
