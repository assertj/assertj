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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveCauseInstance.shouldHaveCauseInstance;
import static org.assertj.core.error.ShouldHaveCauseReference.shouldHaveCauseReference;
import static org.assertj.core.error.ShouldHaveMessage.shouldHaveMessage;
import static org.assertj.core.error.ShouldHaveMessageFindingMatchRegex.shouldHaveMessageFindingMatchRegex;
import static org.assertj.core.error.ShouldHaveMessageMatchingRegex.shouldHaveMessageMatchingRegex;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldHaveNoSuppressedExceptions.shouldHaveNoSuppressedExceptions;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCause;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCauseWithMessage;
import static org.assertj.core.error.ShouldHaveRootCauseExactlyInstance.shouldHaveRootCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveRootCauseInstance.shouldHaveRootCauseInstance;
import static org.assertj.core.error.ShouldHaveSuppressedException.shouldHaveSuppressedException;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonValidations.checkTypeIsNotNull;
import static org.assertj.core.util.Throwables.getRootCause;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Throwable}</code>s.
 *
 * @author Joel Costigliola
 * @author Libor Ondrusek
 * @author Jack Gough
 * @author Mike Gilchrist
 */
public class Throwables {

  private static final Throwables INSTANCE = new Throwables();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Throwables instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Throwables() {}

  /**
   * Asserts that the given actual {@code Throwable} message is equal to the given one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param expectedMessage the expected message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   */
  public void assertHasMessage(AssertionInfo info, Throwable actual, String expectedMessage) {
    assertNotNull(info, actual);
    if (java.util.Objects.equals(actual.getMessage(), expectedMessage)) return;
    throw failures.failure(info, shouldHaveMessage(actual, expectedMessage), actual.getMessage(), expectedMessage);
  }

  public void assertHasCause(AssertionInfo info, Throwable actual, Throwable expectedCause) {
    assertNotNull(info, actual);
    Throwable actualCause = actual.getCause();
    if (actualCause == expectedCause) return;
    if (null == expectedCause) {
      assertHasNoCause(info, actual);
      return;
    }
    if (actualCause == null) throw failures.failure(info, shouldHaveCause(actualCause, expectedCause));
    if (!compareThrowable(actualCause, expectedCause))
      throw failures.failure(info, shouldHaveCause(actualCause, expectedCause));
  }

  /**
   * Asserts that the actual {@code Throwable} has a cause that refers to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param expectedCause the expected cause.
   */
  public void assertHasCauseReference(AssertionInfo info, Throwable actual, Throwable expectedCause) {
    assertNotNull(info, actual);
    Throwable actualCause = actual.getCause();
    if (actualCause != expectedCause) throw failures.failure(info, shouldHaveCauseReference(actualCause, expectedCause));
  }

  /**
   * Asserts that the actual {@code Throwable} has a root cause similar to the given one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param expectedRootCause the expected root cause.
   */
  public void assertHasRootCause(AssertionInfo info, Throwable actual, Throwable expectedRootCause) {
    assertNotNull(info, actual);
    Throwable actualRootCause = getRootCause(actual);
    if (actualRootCause == expectedRootCause) return;
    if (null == expectedRootCause) {
      assertHasNoCause(info, actual);
      return;
    }
    if (actualRootCause == null) throw failures.failure(info, shouldHaveRootCause(actual, null, expectedRootCause));
    if (!compareThrowable(actualRootCause, expectedRootCause))
      throw failures.failure(info, shouldHaveRootCause(actual, actualRootCause, expectedRootCause));
  }

  /**
   * Asserts that the message of the root cause of the actual {@code Throwable} is equal to the given one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param expectedMessage the expected message of the root cause.
   */
  public void assertHasRootCauseMessage(AssertionInfo info, Throwable actual, String expectedMessage) {
    assertNotNull(info, actual);
    Throwable rootCause = getRootCause(actual);
    if (null == rootCause) throw failures.failure(info, shouldHaveRootCauseWithMessage(actual, rootCause, expectedMessage));
    if (java.util.Objects.equals(rootCause.getMessage(), expectedMessage)) return;
    throw failures.failure(info, shouldHaveRootCauseWithMessage(actual, rootCause, expectedMessage), rootCause.getMessage(),
                           expectedMessage);
  }

  /**
   * Asserts that the actual {@code Throwable} does not have a cause.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   */
  public void assertHasNoCause(AssertionInfo info, Throwable actual) {
    assertNotNull(info, actual);
    Throwable actualCause = actual.getCause();
    if (actualCause == null) return;
    throw failures.failure(info, shouldHaveNoCause(actual));
  }

  /**
   * Asserts that the actual {@code Throwable} has a cause.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a cause.
   */
  public void assertHasCause(AssertionInfo info, Throwable actual) {
    assertNotNull(info, actual);
    Throwable actualCause = actual.getCause();
    if (actualCause != null) return;
    throw failures.failure(info, shouldHaveCause(actual));
  }

  /**
   * Asserts that the actual {@code Throwable} has a root cause.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a root cause.
   */
  public void assertHasRootCause(AssertionInfo info, Throwable actual) {
    assertNotNull(info, actual);
    Throwable rootCause = getRootCause(actual);
    if (rootCause != null) return;
    throw failures.failure(info, shouldHaveRootCause(actual));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} starts with the given description.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   */
  public void assertHasMessageStartingWith(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().startsWith(description)) return;
    throw failures.failure(info, shouldStartWith(actual.getMessage(), description));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} contains with the given description.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public void assertHasMessageContaining(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().contains(description)) return;
    throw failures.failure(info, shouldContain(actual, description));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} contains with the given values.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param values the Strings expected to be contained in the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public void assertHasMessageContainingAll(AssertionInfo info, Throwable actual, CharSequence... values) {
    doCommonCheckForMessages(info, actual, values);
    assertNotNull(info, actual);
    String actualMessage = actual.getMessage();
    Set<CharSequence> notFound = stream(values).filter(value -> actualMessage == null || !actualMessage.contains(value))
                                               .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty()) return;
    if (notFound.size() == 1 && values.length == 1) {
      throw failures.failure(info, shouldContain(actual, values[0]), actual, values[0]);
    }
    throw failures.failure(info, shouldContain(actual, values, notFound), actual, values);
  }

  /**
   * Asserts that the message of the actual {@code Throwable} does not contain the given content or is {@code null}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param content the content expected not to be contained in the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains the given content.
   * @since 3.12.0
   */
  public void assertHasMessageNotContaining(AssertionInfo info, Throwable actual, String content) {
    assertNotNull(info, actual);
    if (actual.getMessage() == null || !actual.getMessage().contains(content)) return;
    throw failures.failure(info, shouldNotContain(actual.getMessage(), content), actual.getMessage(), content);
  }

  /**
   * Asserts that the message of the actual {@code Throwable} does not contain any of the given values or is {@code null}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param values the contents expected to not be contained in the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public void assertHasMessageNotContainingAny(AssertionInfo info, Throwable actual, CharSequence... values) {
    doCommonCheckForMessages(info, actual, values);
    String actualMessage = actual.getMessage();
    Set<CharSequence> found = stream(values).filter(value -> actualMessage != null && actualMessage.contains(value))
                                            .collect(toCollection(LinkedHashSet::new));
    if (found.isEmpty()) return;
    if (found.size() == 1 && values.length == 1) {
      throw failures.failure(info, shouldNotContain(actualMessage, values[0]), actualMessage, values[0]);
    }
    throw failures.failure(info, shouldNotContain(actualMessage, values, found, StandardComparisonStrategy.instance()),
                           actualMessage, values);
  }

  /**
   * Asserts that the stack trace of the actual {@code Throwable} contains with the given description.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   */
  public void assertHasStackTraceContaining(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    String stackTrace = org.assertj.core.util.Throwables.getStackTrace(actual);
    if (stackTrace != null && stackTrace.contains(description)) return;
    throw failures.failure(info, shouldContain(stackTrace, description));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} matches with the given regular expression.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public void assertHasMessageMatching(AssertionInfo info, Throwable actual, String regex) {
    requireNonNull(regex, "regex must not be null");
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().matches(regex)) return;
    throw failures.failure(info, shouldHaveMessageMatchingRegex(actual, regex));
  }

  /**
   * Asserts that a sequence of the message of the actual {@code Throwable} matches with the given regular expression (see {@link java.util.regex.Matcher#find()}).
   * The Pattern used under the hood enables the {@link Pattern#DOTALL} mode.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param regex the regular expression expected to be found in the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} doesn't contain any sequence matching with the given regular expression
   * @throws NullPointerException if the regex is null
   */
  public void assertHasMessageFindingMatch(AssertionInfo info, Throwable actual, String regex) {
    requireNonNull(regex, "regex must not be null");
    assertNotNull(info, actual);
    Objects.instance().assertNotNull(info, actual.getMessage(), "exception message of actual");
    if (Pattern.compile(regex, Pattern.DOTALL).asPredicate().test(actual.getMessage())) return;
    throw failures.failure(info, shouldHaveMessageFindingMatchRegex(actual, regex));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} ends with the given description.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   */
  public void assertHasMessageEndingWith(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().endsWith(description)) return;
    throw failures.failure(info, shouldEndWith(actual.getMessage(), description));
  }

  /**
   * Assert that the cause of actual {@code Throwable} is an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public void assertHasCauseInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    if (type.isInstance(actual.getCause())) return;
    throw failures.failure(info, shouldHaveCauseInstance(actual, type));
  }

  /**
   * Assert that the cause of actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *           type.
   */
  public void assertHasCauseExactlyInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    Throwable cause = actual.getCause();
    if (cause != null && type.equals(cause.getClass())) return;
    throw failures.failure(info, shouldHaveCauseExactlyInstance(actual, type));
  }

  /**
   * Assert that the root cause of actual {@code Throwable} is an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public void assertHasRootCauseInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    if (type.isInstance(getRootCause(actual))) return;
    throw failures.failure(info, shouldHaveRootCauseInstance(actual, type));
  }

  /**
   * Assert that the root cause of actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *           given type.
   */
  public void assertHasRootCauseExactlyInstanceOf(AssertionInfo info, Throwable actual,
                                                  Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    Throwable rootCause = getRootCause(actual);
    if (rootCause != null && type.equals(rootCause.getClass())) return;
    throw failures.failure(info, shouldHaveRootCauseExactlyInstance(actual, type));
  }

  public void assertHasNoSuppressedExceptions(AssertionInfo info, Throwable actual) {
    assertNotNull(info, actual);
    Throwable[] suppressed = actual.getSuppressed();
    if (suppressed.length != 0) throw failures.failure(info, shouldHaveNoSuppressedExceptions(suppressed));
  }

  public void assertHasSuppressedException(AssertionInfo info, Throwable actual,
                                           Throwable expectedSuppressedException) {
    assertNotNull(info, actual);
    requireNonNull(expectedSuppressedException, "The expected suppressed exception should not be null");
    Throwable[] suppressed = actual.getSuppressed();
    for (Throwable throwable : suppressed) {
      if (compareThrowable(throwable, expectedSuppressedException)) return;
    }
    throw failures.failure(info, shouldHaveSuppressedException(actual, expectedSuppressedException));
  }

  private static void doCommonCheckForMessages(AssertionInfo info, Throwable actual, CharSequence[] values) {
    assertNotNull(info, actual);
    checkIsNotNull(values);
    checkIsNotEmpty(values);
    checkCharSequenceArrayDoesNotHaveNullElements(values);
  }

  private static void assertNotNull(AssertionInfo info, Throwable actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private static void checkIsNotNull(CharSequence... values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
  }

  private static void checkIsNotEmpty(CharSequence... values) {
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
  }

  private static void checkCharSequenceArrayDoesNotHaveNullElements(CharSequence[] values) {
    if (values.length == 1) {
      checkCharSequenceIsNotNull(values[0]);
    } else {
      for (int i = 0; i < values.length; i++) {
        requireNonNull(values[i], "Expecting CharSequence elements not to be null but found one at index " + i);
      }
    }
  }

  private static void checkCharSequenceIsNotNull(CharSequence sequence) {
    requireNonNull(sequence, "The char sequence to look for should not be null");
  }

  private static boolean compareThrowable(Throwable actual, Throwable expected) {
    return java.util.Objects.equals(actual.getMessage(), expected.getMessage())
           && java.util.Objects.equals(actual.getClass(), expected.getClass());
  }
}
