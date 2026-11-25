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
package org.assertj.core.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveCauseInstance.shouldHaveCauseInstance;
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
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;
import static org.assertj.core.error.ShouldNotHaveThrownExcept.shouldNotHaveThrownExcept;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonValidations.checkTypeIsNotNull;
import static org.assertj.core.util.Throwables.getRootCause;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.error.BasicErrorMessageFactory;

/**
 * Base class for all implementations of assertions for {@link Throwable}s.
 *
 * @param <SELF>  the "self" type of this assertion class.
 * @param <ACTUAL> the type of the "actual" value.
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Jack Gough
 * @author Mike Gilchrist
 * @author Paweł Baczyński
 */
public abstract class AbstractThrowableAssert<SELF extends AbstractThrowableAssert<SELF, ACTUAL>, ACTUAL extends Throwable>
    extends AbstractObjectAssert<SELF, ACTUAL> {

  /**
   * Creates a new throwable assertion.
   *
   * @param actual the actual throwable to verify
   * @param selfType the type of the concrete assertion
   */
  protected AbstractThrowableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that a throwable has been captured.
   *
   * @return this assertion object
   */
  protected SELF hasBeenThrown() {
    return executeAssertion(() -> {
      if (actual == null) {
        throw AssertionErrorCreator.instance().assertionError(info,
                                                              new BasicErrorMessageFactory("%nExpecting code to raise a throwable."));
      }
    });
  }

  SELF hasBeenThrown(Object returnedValue) {
    return executeAssertionNavigation(() -> {
      if (actual == null) {
        throw AssertionErrorCreator.instance().assertionError(info,
                                                              new BasicErrorMessageFactory("%nExpecting code to raise a throwable, but it returned %s instead.",
                                                                                           returnedValue));
      }
      return myself;
    }, () -> myself);
  }

  /**
   * Sets the description of the assertion that is going to be called <b>after</b>.
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * Example:
   * <pre><code class='java'> // WRONG: assertion fails in assertThatThrown but "display me" won't appear in the error message
   * assertThatThrownBy(() -&gt; {}).as("display me").isInstanceOf(Exception.class);
   *
   * // CORRECT: assertion fails  in assertThatThrown AND "display me" will appear in the error message
   * assertThatThrownBy(() -&gt; {}, "display me").isInstanceOf(Exception.class);</code></pre>
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing
   * users to pass their own implementation of a description. For example, a description that creates its value lazily,
   * only when an assertion failure occurs.
   * </p>
   *
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(Description)
   */
  @Override
  public SELF as(String description, Object... args) {
    return super.as(description, args);
  }

  /**
   * Lazily specifies the description of the assertion that is going to be called, the given description is <b>not</b> evaluated
   * if the assertion succeeds.
   * <p>
   * Example :
   * <pre><code class='java'> // set an incorrect age to Mr Frodo which we all know is 33 years old.
   * frodo.setAge(50);
   *
   * // the lazy test description is <b>not</b> evaluated as the assertion succeeds
   * assertThat(frodo.getAge()).as(() -&gt; &quot;check Frodo's age&quot;).isEqualTo(50);
   *
   * try
   * {
   *   // the lazy test description is evaluated as the assertion fails
   *   assertThat(frodo.getAge()).as(() -&gt; &quot;check Frodo's age&quot;).isEqualTo(33);
   * }
   * catch (AssertionError e)
   * {
   *   assertThat(e).hasMessageContaining(&quot;[check Frodo's age]&quot;);
   * }</code></pre>
   * <p>
   * The description must be set <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * Example:
   * <pre><code class='java'> // WRONG: assertion fails in assertThatThrownBy but "display me" won't appear in the error message
   * assertThatThrownBy(() -&gt; {}).as(() -&gt; &quot;display me&quot;).isInstanceOf(Exception.class);
   *
   * // CORRECT: assertion fails in assertThatThrownBy AND "display me" will appear in the error message
   * assertThatThrownBy(() -&gt; {}, &quot;display me&quot;).isInstanceOf(Exception.class);</code></pre>
   * @param descriptionSupplier the description {@link Supplier}.
   * @return {@code this} object.
   * @throws IllegalStateException if the descriptionSupplier is {@code null} when evaluated.
   */
  @Override
  public SELF as(Supplier<String> descriptionSupplier) {
    return super.as(descriptionSupplier);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} is equal to the given one.
   *
   * @param message the expected message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   */
  public SELF hasMessage(String message) {
    return executeAssertion(() -> assertHasMessage(message));
  }

  /**
   * Verifies that the message of the actual (@code Throwable) is equal to the given one, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("foo is not a valid input");
   * Throwable throwable = new Throwable(invalidArgException);
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasMessage("%s is not a valid input", "foo");
   *
   * // These assertions fail:
   * assertThat(throwable).hasMessage("%s is not a valid input", "bar");
   * assertThat(throwable).hasMessage("%s is not a valid input", 12);
   * assertThat(null).hasMessage("%s is not a valid input", "foo");</code></pre>
   *
   * @param message    a format string representing the expected message
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError         if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError         if the message of the actual {@code Throwable} is not equal to the given one.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessage(String message, Object... parameters) {
    return hasMessage(message.formatted(parameters));
  }

  /**
   * Verifies that the actual {@code Throwable} has a cause similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("invalid arg");
   * Throwable throwable = new Throwable(invalidArgException);
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasCause(invalidArgException);
   *
   * // These assertions fail:
   * assertThat(throwable).hasCause(new IllegalArgumentException("bad arg"));
   * assertThat(throwable).hasCause(new NullPointerException());
   * assertThat(throwable).hasCause(null); // prefer hasNoCause()</code></pre>
   *
   * @param cause the expected cause
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   */
  public SELF hasCause(Throwable cause) {
    return executeAssertion(() -> assertHasCause(cause));
  }

  /**
   * Verifies that the actual {@code Throwable} does not have a cause.
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   */
  public SELF hasNoCause() {
    return executeAssertion(() -> assertHasNoCause());
  }

  /**
   * Returns a new assertion object that uses the cause of the current Throwable as the actual Throwable under test.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable cause =  new IllegalArgumentException("wrong amount 123");
   * Throwable exception = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwableWithMessage).cause()
   *                                 .hasMessageStartingWith("wrong amount");</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a cause.
   * @since 3.23.0
   */
  public AbstractThrowableAssert<?, ?> cause() {
    return executeAssertionNavigation(() -> {
      assertHasCause();
      return new ThrowableAssert<>(actual.getCause()).withAssertionState(myself);
    }, ThrowableAssert::nullThrowableAssert);
  }

  /**
   * Returns a new assertion object that uses the root cause of the current Throwable as the actual Throwable under test.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable rootCause =  new JdbcException("invalid query");
   * Throwable cause =  new RuntimeException(rootCause);
   * Throwable exception = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwableWithMessage).rootCause()
   *                                 .hasMessageStartingWith("invalid");</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a root cause.
   * @since 3.23.0
   */
  public AbstractThrowableAssert<?, ?> rootCause() {
    return executeAssertionNavigation(() -> {
      assertHasRootCause();
      return new ThrowableAssert<>(org.assertj.core.util.Throwables.getRootCause(actual)).withAssertionState(myself);
    }, ThrowableAssert::nullThrowableAssert);
  }

  /**
   * Returns a new list assertion including the actual {@link Throwable} and all its causes (if any) down to the root cause.
   * <p>
   * The list only contains the actual throwable if there is no cause.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable rootCause = new JdbcException("invalid query");
   * Throwable cause =  new RuntimeException("some failure", rootCause);
   * Throwable throwable = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwable).throwablesChain()
   *                      .extracting(Throwable::getMessage)
   *                      .anyMatch("some failure");
   *
   * // if there is no cause, throwablesChain simply returns the top level throwable
   * assertThat(rootCause).throwablesChain()
   *                      .containsExactly(rootCause);</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @since 4.0.0
   */
  public ListAssert<Throwable> throwablesChain() {
    return executeAssertionNavigation(() -> {
      isNotNull();
      List<Throwable> throwablesChain = getThrowablesChain(actual);
      return assertThat(throwablesChain);
    }, ListAssert::nullListAssert);
  }

  private static List<Throwable> getThrowablesChain(Throwable throwable) {
    List<Throwable> throwables = new ArrayList<>();
    while (throwable != null) {
      throwables.add(throwable);
      throwable = throwable.getCause();
    }
    return throwables;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageStartingWith("wrong amount");
   *
   * // assertions will fail:
   * assertThat(throwableWithMessage).hasMessageStartingWith("right amount"); </code></pre>
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   */
  public SELF hasMessageStartingWith(String description) {
    return executeAssertion(() -> assertHasMessageStartingWith(description));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageStartingWith("%s amount", "wrong");
   *
   * // assertions will fail:
   * assertThat(throwableWithMessage).hasMessageStartingWith("%s amount", "right"); </code></pre>
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @param parameters  argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError         if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError         if the message of the actual {@code Throwable} does not start with the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessageStartingWith(String description, Object... parameters) {
    return executeAssertion(() -> assertHasMessageStartingWith(description.formatted(parameters)));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageContaining("123");
   *
   * // assertions will fail:
   * assertThat(throwableWithoutMessage).hasMessageContaining("123");
   * assertThat(throwableWithMessage).hasMessageContaining("234"); </code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public SELF hasMessageContaining(String description) {
    return executeAssertion(() -> assertHasMessageContaining(description));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains all the given values.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageContainingAll("amount", "123");
   *
   * // assertions will fail:
   * assertThat(throwableWithoutMessage).hasMessageContainingAll("123");
   * assertThat(throwableWithMessage).hasMessageContainingAll("234"); </code></pre>
   *
   * @param values the Strings expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain all the given values.
   */
  public SELF hasMessageContainingAll(CharSequence... values) {
    return executeAssertion(() -> assertHasMessageContainingAll(values));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} does not contain the given content or is {@code null}.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertions will pass:
   * assertThat(throwableWithMessage).hasMessageNotContaining("234");
   * assertThat(throwableWithoutMessage).hasMessageNotContaining("foo");
   *
   * // assertion will fail:
   * assertThat(throwableWithMessage).hasMessageNotContaining("amount");</code></pre>
   *
   * @param content the content expected not to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains the given content.
   * @since 3.12.0
   */
  public SELF hasMessageNotContaining(String content) {
    return executeAssertion(() -> assertHasMessageNotContaining(content));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} does not contain any of the given values or is {@code null}.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertions will pass:
   * assertThat(throwableWithMessage).hasMessageNotContainingAny("234");
   * assertThat(throwableWithoutMessage).hasMessageNotContainingAny("foo");
   *
   * // assertion will fail:
   * assertThat(throwableWithMessage).hasMessageNotContainingAny("foo", "amount");</code></pre>
   *
   * @param values the contents expected to not be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains any of the given values.
   * @since 3.12.0
   */
  public SELF hasMessageNotContainingAny(CharSequence... values) {
    return executeAssertion(() -> assertHasMessageNotContainingAny(values));
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwableWithMessage).hasStackTraceContaining("amount 123");
   *
   * // assertion will fail
   * assertThat(throwableWithMessage).hasStackTraceContaining("456");</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   */
  public SELF hasStackTraceContaining(String description) {
    return executeAssertion(() -> assertHasStackTraceContaining(description));
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion succeeds
   * assertThat(throwableWithMessage).hasStackTraceContaining("%s", amount);
   *
   * // assertion fails
   * assertThat(throwableWithMessage).hasStackTraceContaining("%d", 456);</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @param parameters  argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError         if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError         if the stack trace of the actual {@code Throwable} does not contain the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasStackTraceContaining(String description, Object... parameters) {
    return executeAssertion(() -> assertHasStackTraceContaining(description.formatted(parameters)));
  }

  /**
   * Verifies that the message of the {@code Throwable} under test matches the given regular expression.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion succeeds
   * assertThat(throwable).hasMessageMatching("wrong amount [0-9]*");
   *
   * // assertion fails
   * assertThat(throwable).hasMessageMatching("wrong amount [0-9]* euros");</code></pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public SELF hasMessageMatching(String regex) {
    return executeAssertion(() -> assertHasMessageMatching(regex));
  }

  /**
   * Verifies that the message of the {@code Throwable} under test matches the given regular expression {@link Pattern}.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion succeeds
   * assertThat(throwable).hasMessageMatching(Pattern.compile("wrong amount [0-9]*"));
   *
   * // assertion fails
   * assertThat(throwable).hasMessageMatching(Pattern.compile("wrong amount [0-9]* euros"));</code></pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public SELF hasMessageMatching(Pattern regex) {
    return executeAssertion(() -> assertHasMessageMatching(regex));
  }

  /**
   * Verifies that a sequence of the message of the actual {@code Throwable} matches with
   * the given regular expression (see {@link java.util.regex.Matcher#find()}).<br>
   * The {@link Pattern} used under the hood enables the {@link Pattern#DOTALL} mode.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException(&quot;Dear John,\n&quot; +
   *                                                    &quot;it' s a wrong amount&quot;);
   * // assertion will pass
   * assertThat(throwable).hasMessageFindingMatch(&quot;wrong amount&quot;);
   * assertThat(throwable).hasMessageFindingMatch(&quot;Dear John&quot;);
   * assertThat(throwable).hasMessageFindingMatch(&quot;wrong amount$&quot;);
   *
   * // assertion will fail
   * assertThat(throwable).hasMessageFindingMatch(&quot;Dear John$&quot;);</code></pre>
   *
   * @param regex the regular expression expected to be found in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the message of the actual {@code Throwable} doesn't contain any sequence matching with the given regular expression
   * @throws NullPointerException if the regex is null
   * @since 3.12.0
   */
  public SELF hasMessageFindingMatch(String regex) {
    return executeAssertion(() -> assertHasMessageFindingMatch(regex));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwableWithMessage).hasMessageEndingWith("123");
   *
   * // assertion will fail
   * assertThat(throwableWithMessage).hasMessageEndingWith("456");</code></pre>
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   */
  public SELF hasMessageEndingWith(String description) {
    return executeAssertion(() -> assertHasMessageEndingWith(description));
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwableWithMessage).hasMessageEndingWith("%s 123", "amount");
   *
   * // assertion will fail
   * assertThat(throwableWithMessage).hasMessageEndingWith("amount %d", 456);</code></pre>
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @param parameters  argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError         if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError         if the message of the actual {@code Throwable} does not end with the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessageEndingWith(String description, Object... parameters) {
    return executeAssertion(() -> assertHasMessageEndingWith(description.formatted(parameters)));
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertions will pass
   * assertThat(throwable).hasCauseInstanceOf(NullPointerException.class);
   * assertThat(throwable).hasCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThat(throwable).hasCauseInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} has no cause.
   * @throws AssertionError       if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public SELF hasCauseInstanceOf(Class<? extends Throwable> type) {
    return executeAssertion(() -> assertHasCauseInstanceOf(type));
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertion will pass
   * assertThat(throwable).hasCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThat(throwable).hasCauseExactlyInstanceOf(RuntimeException.class);
   * assertThat(throwable).hasCauseExactlyInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} has no cause.
   * @throws AssertionError       if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *                              type.
   */
  public SELF hasCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    return executeAssertion(() -> assertHasCauseExactlyInstanceOf(type));
  }

  /**
   * Verifies that the actual {@code Throwable} has a root cause similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("invalid arg");
   * Throwable throwable = new Throwable(new RuntimeException(invalidArgException));
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasRootCause(invalidArgException);
   *
   * // These assertions fail:
   * assertThat(throwable).hasRootCause(new IllegalArgumentException("bad arg"));
   * assertThat(throwable).hasRootCause(new RuntimeException());
   * assertThat(throwable).hasRootCause(null); // prefer hasNoCause()</code></pre>
   *
   * @param cause the expected root cause
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   * @since 3.12.0
   */
  public SELF hasRootCause(Throwable cause) {
    return executeAssertion(() -> assertHasRootCause(cause));
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException()));
   *
   * // assertions will pass
   * assertThat(throwable).hasRootCauseInstanceOf(NullPointerException.class);
   * assertThat(throwable).hasRootCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThat(throwable).hasRootCauseInstanceOf(IllegalStateException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} has no cause.
   * @throws AssertionError       if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public SELF hasRootCauseInstanceOf(Class<? extends Throwable> type) {
    return executeAssertion(() -> assertHasRootCauseInstanceOf(type));
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException()));
   *
   * // assertion will pass
   * assertThat(throwable).hasRootCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThat(throwable).hasRootCauseExactlyInstanceOf(RuntimeException.class);
   * assertThat(throwable).hasRootCauseExactlyInstanceOf(IllegalStateException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} has no cause.
   * @throws AssertionError       if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *                              given type.
   */
  public SELF hasRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    return executeAssertion(() -> assertHasRootCauseExactlyInstanceOf(type));
  }

  /**
   * Verifies that the message of the root cause of the actual {@code Throwable} is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException("object")));
   *
   * // assertion will pass
   * assertThat(throwable).hasRootCauseMessage("object");
   *
   * // assertions will fail
   * assertThat((Throwable) null).hasRootCauseMessage("object");
   * assertThat(throwable).hasRootCauseMessage("another object");
   * assertThat(new Throwable()).hasRootCauseMessage("object");
   * assertThat(new Throwable(new NullPointerException())).hasRootCauseMessage("object");</code></pre>
   *
   * @param message the expected root cause message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the root cause of the actual {@code Throwable} is not equal to
   *                        the given one.
   * @since 3.14.0
   */
  public SELF hasRootCauseMessage(String message) {
    return executeAssertion(() -> assertHasRootCauseMessage(message));
  }

  /**
   * Verifies that the message of the root cause of the actual {@code Throwable} is equal to the given one, after
   * being formatted using {@link String#format(String, Object...)} method.
   * <p>
   * Example:
   * <pre><code class='java'>Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException("expected message")));
   *
   * // assertion will pass
   * assertThat(throwable).hasRootCauseMessage("%s %s", "expected", "message");
   *
   * // assertions will fail
   * assertThat((Throwable) null).hasRootCauseMessage("%s %s", "expected", "message");
   * assertThat(throwable).hasRootCauseMessage("%s", "message");
   * assertThat(new Throwable()).hasRootCauseMessage("%s %s", "expected", "message");
   * assertThat(new Throwable(new NullPointerException())).hasRootCauseMessage("%s %s", "expected", "message");</code></pre>
   *
   * @param message    the expected root cause message.
   * @param parameters argument referenced by the format specifiers in the format string.
   * @return this assertion object.
   * @throws AssertionError         if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError         if the root cause of the actual {@code Throwable} is {@code null}.
   * @throws AssertionError         if the message of the root cause of the actual {@code Throwable} is not equal to the given one.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   * @since 3.14.0
   */
  public SELF hasRootCauseMessage(String message, Object... parameters) {
    return hasRootCauseMessage(message.formatted(parameters));
  }

  /**
   * Verifies that the actual {@code Throwable} has no suppressed exceptions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Throwable()).hasNoSuppressedExceptions();
   *
   * // assertion will fail
   * Throwable throwableWithSuppressedException = new Throwable();
   * throwableWithSuppressedException.addSuppressed(new IllegalArgumentException());
   * assertThat(throwableWithSuppressedException).hasNoSuppressedExceptions();</code></pre>
   *
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError       if the actual {@code Throwable} has any suppressed exceptions.
   * @since 2.6.0 / 3.6.0
   */
  public SELF hasNoSuppressedExceptions() {
    return executeAssertion(() -> assertHasNoSuppressedExceptions());
  }

  /**
   * Verifies that the actual {@code Throwable} has a suppressed exception similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable();
   * Throwable invalidArgException = new IllegalArgumentException("invalid argument");
   * throwable.addSuppressed(invalidArgException);
   *
   * // These assertions succeed:
   * assertThat(throwable).hasSuppressedException(invalidArgException);
   * assertThat(throwable).hasSuppressedException(new IllegalArgumentException("invalid argument"));
   *
   * // These assertions fail:
   * assertThat(throwable).hasSuppressedException(new IllegalArgumentException("invalid parameter"));
   * assertThat(throwable).hasSuppressedException(new NullPointerException());</code></pre>
   *
   * @param suppressedException the expected suppressed exception
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have the given suppressed exception.
   * @since 2.6.0 / 3.6.0
   */
  public SELF hasSuppressedException(Throwable suppressedException) {
    return executeAssertion(() -> assertHasSuppressedException(suppressedException));
  }

  /**
   * Returns a new assertion object with the current {@link Throwable} suppressed exceptions as the object under test.
   * <p>
   * As suppressed exceptions are contained in a {@code Throwable[]} instance,
   * {@linkplain AbstractObjectArrayAssert array assertions} can be chained after this invocation.
   * <p>
   * You can navigate back to the current {@code Throwable} with
   * {@link SuppressedExceptionsAssert#returnToThrowable() returnToThrowable()}.
   * <p>
   * Examples:
   * <pre><code class='java'>Throwable throwable = new Throwable("boom!");
   * Throwable invalidArgException = new IllegalArgumentException("invalid argument");
   * Throwable ioException = new IOException("IO error");
   * throwable.addSuppressed(invalidArgException);
   * throwable.addSuppressed(ioException);
   *
   * // these assertions succeed:
   * assertThat(throwable).suppressedExceptions()
   *                      .containsOnly(invalidArgException, ioException)
   *                      .returnToThrowable()
   *                      .hasMessage("boom!");
   *
   * // this assertion fails:
   * assertThat(throwable).suppressedExceptions()
   *                      .isEmpty();</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @since 3.28.0
   */
  public SuppressedExceptionsAssert<SELF, ACTUAL> suppressedExceptions() {
    return executeAssertionNavigation(() -> {
      isNotNull();
      SuppressedExceptionsAssert<SELF, ACTUAL> suppressedExceptionsAssert = SuppressedExceptionsAssert.from(myself);
      return suppressedExceptionsAssert.describedAs("checking suppressed exceptions");
    }, () -> new SuppressedExceptionsAssert<>(myself, new Throwable[0]));
  }

  /**
   * Verifies that the {@link org.assertj.core.api.ThrowableAssert.ThrowingCallable} didn't raise a throwable.
   * <p>
   * Example :
   * <pre><code class='java'> assertThatCode(() -&gt; foo.bar()).doesNotThrowAnyException();</code></pre>
   *
   * @throws AssertionError if the actual statement raised a {@code Throwable}.
   * @since 3.7.0
   */
  public void doesNotThrowAnyException() {
    executeAssertion(() -> {
      if (actual != null) throw AssertionErrorCreator.instance().assertionError(info, shouldNotHaveThrown(actual));
    });
  }

  /**
   * Verifies that the {@link org.assertj.core.api.ThrowableAssert.ThrowingCallable} didn't raise a throwable
   * except matching the provided type(s).
   * <p>
   * Example:
   * <pre><code class='java'> void foo() {
   *   throw new IllegalArgumentException();
   * }
   *
   * void bar() {
   * }
   *
   * // assertions succeed:
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(IllegalArgumentException.class);
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(RuntimeException.class);
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(IllegalArgumentException.class, IllegalStateException.class);
   * assertThatCode(() -&gt; bar()).doesNotThrowAnyExceptionExcept();
   *
   * // assertions fails:
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept();
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(NumberFormatException.class);
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(NumberFormatException.class, IOException.class);</code></pre>
   *
   * @param ignoredExceptionTypes types allowed to be thrown.
   * @throws AssertionError if the actual statement raised a {@code Throwable} with type other than provided one(s).
   * @since 3.26.0
   */
  @SafeVarargs
  public final void doesNotThrowAnyExceptionExcept(Class<? extends Throwable>... ignoredExceptionTypes) {
    executeAssertion(() -> {
      if (isNotAnInstanceOfAny(ignoredExceptionTypes))
        throwAssertionError(shouldNotHaveThrownExcept(actual, ignoredExceptionTypes));
    });
  }

  private boolean isNotAnInstanceOfAny(Class<? extends Throwable>[] exceptionTypes) {
    if (actual == null) return false;
    return Arrays.stream(exceptionTypes).noneMatch(ex -> ex.isAssignableFrom(actual.getClass()));
  }

  /**
   * A shortcut for <code>extracting(Throwable::getMessage, as(InstanceOfAssertFactories.STRING))</code> which allows
   * to extract a throwable message and then execute assertions on it.
   * <p>
   * Note that once you have navigated to the throwable message you can't navigate back to the throwable.
   * <p>
   * Example :
   * <pre><code class='java'> Throwable throwable = new Throwable("boom!");
   *
   * // assertions succeed:
   * assertThat(throwable).message().startsWith("boo")
   *                                .endsWith("!");
   *
   * // assertion fails:
   * assertThat(throwable).message().isEmpty();</code></pre>
   *
   * @return the created {@link StringAssert} on the throwable message.
   * @since 3.22.0
   */
  public AbstractStringAssert<?> message() {
    return executeAssertionNavigation(() -> {
      isNotNull();
      return new StringAssert(actual.getMessage()).withAssertionState(myself);
    }, StringAssert::nullStringAssert);
  }

  // internal assertions

  private void assertHasMessage(String expectedMessage) {
    isNotNull();
    if (java.util.Objects.equals(actual.getMessage(), expectedMessage)) return;
    throw assertionError(shouldHaveMessage(actual, expectedMessage), actual.getMessage(), expectedMessage);
  }

  private void assertHasCause(Throwable expectedCause) {
    isNotNull();
    Throwable actualCause = actual.getCause();
    if (actualCause == expectedCause) return;
    if (expectedCause == null) {
      assertHasNoCause();
      return;
    }
    if (actualCause == null || !compareThrowable(actualCause, expectedCause))
      throw assertionError(shouldHaveCause(actual, expectedCause));
  }

  private void assertHasRootCause(Throwable expectedRootCause) {
    isNotNull();
    Throwable actualRootCause = getRootCause(actual);
    if (actualRootCause == expectedRootCause) return;
    if (expectedRootCause == null) {
      assertHasNoCause();
      return;
    }
    if (actualRootCause == null) throw assertionError(shouldHaveRootCause(actual, null, expectedRootCause));
    if (!compareThrowable(actualRootCause, expectedRootCause))
      throw assertionError(shouldHaveRootCause(actual, actualRootCause, expectedRootCause));
  }

  private void assertHasRootCauseMessage(String expectedMessage) {
    isNotNull();
    Throwable rootCause = getRootCause(actual);
    if (rootCause == null) throw assertionError(shouldHaveRootCauseWithMessage(actual, null, expectedMessage));
    if (java.util.Objects.equals(rootCause.getMessage(), expectedMessage)) return;
    throw assertionError(shouldHaveRootCauseWithMessage(actual, rootCause, expectedMessage), rootCause.getMessage(),
                         expectedMessage);
  }

  private void assertHasNoCause() {
    isNotNull();
    if (actual.getCause() != null) throw assertionError(shouldHaveNoCause(actual));
  }

  private void assertHasCause() {
    isNotNull();
    if (actual.getCause() == null) throw assertionError(shouldHaveCause(actual));
  }

  private void assertHasRootCause() {
    isNotNull();
    if (getRootCause(actual) == null) throw assertionError(shouldHaveRootCause(actual));
  }

  private void assertHasMessageStartingWith(String description) {
    isNotNull();
    if (actual.getMessage() != null && actual.getMessage().startsWith(description)) return;
    throw assertionError(shouldStartWith(actual.getMessage(), description));
  }

  private void assertHasMessageContaining(String description) {
    isNotNull();
    if (actual.getMessage() != null && actual.getMessage().contains(description)) return;
    throw assertionError(shouldContain(actual, description));
  }

  private void assertHasMessageContainingAll(CharSequence... values) {
    doCommonCheckForMessages(values);
    String actualMessage = actual.getMessage();
    Set<CharSequence> notFound = stream(values).filter(value -> actualMessage == null || !actualMessage.contains(value))
                                               .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty()) return;
    if (notFound.size() == 1 && values.length == 1) throw assertionError(shouldContain(actual, values[0]), actual, values[0]);
    throw assertionError(shouldContain(actual, values, notFound), actual, values);
  }

  private void assertHasMessageNotContaining(String content) {
    isNotNull();
    if (actual.getMessage() == null || !actual.getMessage().contains(content)) return;
    throw assertionError(shouldNotContain(actual, content), actual.getMessage(), content);
  }

  private void assertHasMessageNotContainingAny(CharSequence... values) {
    doCommonCheckForMessages(values);
    String actualMessage = actual.getMessage();
    Set<CharSequence> found = stream(values).filter(value -> actualMessage != null && actualMessage.contains(value))
                                            .collect(toCollection(LinkedHashSet::new));
    if (found.isEmpty()) return;
    if (found.size() == 1 && values.length == 1)
      throw assertionError(shouldNotContain(actual, values[0]), actualMessage, values[0]);
    throw assertionError(shouldNotContain(actual, values, found), actualMessage, values);
  }

  private void assertHasStackTraceContaining(String description) {
    isNotNull();
    String stackTrace = getStackTrace(actual);
    if (stackTrace != null && stackTrace.contains(description)) return;
    throw assertionError(shouldContain(stackTrace, description));
  }

  private void assertHasMessageMatching(Pattern regex) {
    requireNonNull(regex, "regex must not be null");
    isNotNull();
    if (actual.getMessage() != null && regex.matcher(actual.getMessage()).matches()) return;
    throw assertionError(shouldHaveMessageMatchingRegex(actual, regex.pattern()));
  }

  private void assertHasMessageMatching(String regex) {
    requireNonNull(regex, "regex must not be null");
    assertHasMessageMatching(Pattern.compile(regex));
  }

  private void assertHasMessageFindingMatch(String regex) {
    requireNonNull(regex, "regex must not be null");
    isNotNull();
    objects.assertNotNull(info, actual.getMessage(), "exception message of actual");
    if (Pattern.compile(regex, Pattern.DOTALL).asPredicate().test(actual.getMessage())) return;
    throw assertionError(shouldHaveMessageFindingMatchRegex(actual, regex));
  }

  private void assertHasMessageEndingWith(String description) {
    isNotNull();
    if (actual.getMessage() != null && actual.getMessage().endsWith(description)) return;
    throw assertionError(shouldEndWith(actual.getMessage(), description));
  }

  private void assertHasCauseInstanceOf(Class<? extends Throwable> type) {
    isNotNull();
    checkTypeIsNotNull(type);
    if (!type.isInstance(actual.getCause())) throw assertionError(shouldHaveCauseInstance(actual, type));
  }

  private void assertHasCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    isNotNull();
    checkTypeIsNotNull(type);
    Throwable cause = actual.getCause();
    if (cause == null || !type.equals(cause.getClass())) throw assertionError(shouldHaveCauseExactlyInstance(actual, type));
  }

  private void assertHasRootCauseInstanceOf(Class<? extends Throwable> type) {
    isNotNull();
    checkTypeIsNotNull(type);
    if (!type.isInstance(getRootCause(actual))) throw assertionError(shouldHaveRootCauseInstance(actual, type));
  }

  private void assertHasRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    isNotNull();
    checkTypeIsNotNull(type);
    Throwable rootCause = getRootCause(actual);
    if (rootCause == null || !type.equals(rootCause.getClass()))
      throw assertionError(shouldHaveRootCauseExactlyInstance(actual, type));
  }

  private void assertHasNoSuppressedExceptions() {
    isNotNull();
    if (actual.getSuppressed().length != 0) throw assertionError(shouldHaveNoSuppressedExceptions(actual));
  }

  private void assertHasSuppressedException(Throwable expectedSuppressedException) {
    isNotNull();
    requireNonNull(expectedSuppressedException, "The expected suppressed exception should not be null");
    for (Throwable throwable : actual.getSuppressed()) {
      if (compareThrowable(throwable, expectedSuppressedException)) return;
    }
    throw assertionError(shouldHaveSuppressedException(actual, expectedSuppressedException));
  }

  private void doCommonCheckForMessages(CharSequence[] values) {
    isNotNull();
    if (values == null) throw arrayOfValuesToLookForIsNull();
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
    if (values.length == 1) {
      requireNonNull(values[0], "The char sequence to look for should not be null");
    } else {
      for (int i = 0; i < values.length; i++) {
        requireNonNull(values[i], "Expecting CharSequence elements not to be null but found one at index " + i);
      }
    }
  }

  private static boolean compareThrowable(Throwable actual, Throwable expected) {
    return java.util.Objects.equals(actual.getMessage(), expected.getMessage())
           && java.util.Objects.equals(actual.getClass(), expected.getClass());
  }
}
