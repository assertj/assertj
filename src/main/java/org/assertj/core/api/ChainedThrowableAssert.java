package org.assertj.core.api;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

/**
 * Assertion methods for {@link java.lang.Throwable}.
 * <p>
 * This class is linked with the {@link ExpectThrowableAssert} and allow to check that an exception
 * type is thrown by several lambda.
 */
public class ChainedThrowableAssert<T extends Throwable>
    extends AbstractThrowableAssert<ChainedThrowableAssert<T>, T> {
  private final ExpectThrowableAssert<T> parent;

  ChainedThrowableAssert(final ExpectThrowableAssert<T> parent, final T actual) {
    super(actual, ChainedThrowableAssert.class);
    this.parent = parent;
  }

  /**
   * Assert that an exception of type T is actually thrown by the {@code throwingCallable}.
   *
   * @param throwingCallable
   * @return return a {@link ChainedThrowableAssert}.
   * @see ExpectThrowableAssert#isThrownBy(ThrowingCallable)
   */
  public ChainedThrowableAssert<T> isThrownBy(final ThrowingCallable throwingCallable) {
    return parent.isThrownBy(throwingCallable);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withMessage(String)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasMessage(String message) {
    return super.hasMessage(message);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withCause(Throwable)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasCause(Throwable cause) {
    return super.hasCause(cause);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withNoCause()}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasNoCause() {
    return super.hasNoCause();
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withMessageStartingWith(String)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasMessageStartingWith(String description) {
    return super.hasMessageStartingWith(description);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withMessageContaining(String)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasMessageContaining(String description) {
    return super.hasMessageContaining(description);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withMessageMatching(String)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasMessageMatching(String regex) {
    return super.hasMessageMatching(regex);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withMessageEndingWith(String)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasMessageEndingWith(String description) {
    return super.hasMessageEndingWith(description);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withCauseInstanceOf(Class)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasCauseInstanceOf(Class<? extends Throwable> type) {
    return super.hasCauseInstanceOf(type);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withCauseExactlyInstanceOf(Class)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    return super.hasCauseExactlyInstanceOf(type);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withRootCauseInstanceOf(Class)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasRootCauseInstanceOf(Class<? extends Throwable> type) {
    return super.hasRootCauseInstanceOf(type);
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated in this context, prefer {@link #withRootCauseExactlyInstanceOf(Class)}.
   */
  @Override
  @Deprecated
  public ChainedThrowableAssert<T> hasRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    return super.hasRootCauseExactlyInstanceOf(type);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} is equal to the given one.
   *
   * @param message the expected message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   * @see AbstractThrowableAssert#hasMessage(String)
   */
  public ChainedThrowableAssert<T> withMessage(String message) {
    return super.hasMessage(message);
  }

  /**
   * Verifies that the actual {@code Throwable} has a cause similar to the given one, that is with same type and message
   * (it does not use {@link Throwable#equals(Object) equals} method for comparison).
   *
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("invalid arg");
   * Throwable throwable = new Throwable(invalidArgException);
   *
   * // This assertion succeeds:
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCause(invalidArgException);
   *
   * // These assertions fail:
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCause(new IllegalArgumentException("bad arg"));
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCause(new NullPointerException());
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCause(null); // prefer withNoCause()</code>
   * </pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   * @see AbstractThrowableAssert#hasCause(Throwable)
   */
  public ChainedThrowableAssert<T> withCause(Throwable cause) {
    return super.hasCause(cause);
  }

  /**
   * Verifies that the actual {@code Throwable} does not have a cause.
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   * @see AbstractThrowableAssert#hasNoCause()
   */
  public ChainedThrowableAssert<T> withNoCause() {
    return super.hasNoCause();
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description.
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   * @see AbstractThrowableAssert#hasMessageStartingWith(String)
   */
  public ChainedThrowableAssert<T> withMessageStartingWith(String description) {
    return super.hasMessageStartingWith(description);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains with the given description.
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   * @see AbstractThrowableAssert#hasMessageContaining(String)
   */
  public ChainedThrowableAssert<T> withMessageContaining(String description) {
    return super.hasMessageContaining(description);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} matches with the given regular expression.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withMessageMatching("wrong amount [0-9]*");
   *
   * // assertion will fail
   * ssertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withMessageMatching("wrong amount [0-9]* euros");</code>
   * </pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   * @see AbstractThrowableAssert#hasMessageMatching(String)
   */
  public ChainedThrowableAssert<T> withMessageMatching(String regex) {
    return super.hasMessageMatching(regex);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description.
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   * @see AbstractThrowableAssert#hasMessageEndingWith(String)
   */
  public ChainedThrowableAssert<T> withMessageEndingWith(String description) {
    return super.hasMessageEndingWith(description);
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertion will pass
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCauseInstanceOf(NullPointerException.class);
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCauseInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   * @see AbstractThrowableAssert#hasCauseInstanceOf(Class)
   */
  public ChainedThrowableAssert<T> withCauseInstanceOf(Class<? extends Throwable> type) {
    return super.hasCauseInstanceOf(type);
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertion will pass
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCauseExactlyInstanceOf(RuntimeException.class);
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withCauseExactlyInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * </p>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *           type.
   * @see AbstractThrowableAssert#hasCauseExactlyInstanceOf(Class)          
   */
  public ChainedThrowableAssert<T> withCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    return super.hasCauseExactlyInstanceOf(type);
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new
   * NullPointerException()));
   *
   * // assertion will pass
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withRootCauseInstanceOf(NullPointerException.class);
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withRootCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withRootCauseInstanceOf(IllegalStateException.class);</code></pre>
   *
   * </p>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   * @see AbstractThrowableAssert#hasRootCauseInstanceOf(Class)
   */
  public ChainedThrowableAssert<T> withRootCauseInstanceOf(Class<? extends Throwable> type) {
    return super.hasRootCauseInstanceOf(type);
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new
   * NullPointerException()));
   *
   * // assertion will pass
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withRootCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertion will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withRootCauseExactlyInstanceOf(RuntimeException.class);
   * assertThatException(Throwable.class).isThrownBy(() -> {throw throwable;}).withRootCauseExactlyInstanceOf(IllegalStateException.class);</code></pre>
   *
   * </p>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *           given type.
   * @see AbstractThrowableAssert#hasRootCauseExactlyInstanceOf(Class)
   */  
  public ChainedThrowableAssert<T> withRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    return super.hasRootCauseExactlyInstanceOf(type);
  }

}
