/*
 * Created on Nov 18, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Strings.formatIfArgs;

import java.util.Comparator;

import org.assertj.core.description.Description;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all assertions.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractAssert<S extends AbstractAssert<S, A>, A> implements Assert<S, A> {

  @VisibleForTesting
  Objects objects = Objects.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  @VisibleForTesting
  protected final WritableAssertionInfo info;

  // visibility is protected to allow us write custom assertions that need access to actual
  @VisibleForTesting
  protected final A actual;
  protected final S myself;

  // we prefer not to use Class<? extends S> selfType because it would force inherited
  // constructor to cast with a compiler warning
  // let's keep compiler warning internal to fest (when we can) and not expose them to our end users.
  @SuppressWarnings("unchecked")
  protected AbstractAssert(A actual, Class<?> selfType) {
    myself = (S) selfType.cast(this);
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.</br> When writing
   * your own assertion class, you can use the returned {@link WritableAssertionInfo} to change the error message and
   * still keep the description set by the assertion user.
   * 
   * @return the {@link WritableAssertionInfo} used in the current assertion
   */
  protected WritableAssertionInfo getWritableAssertionInfo() {
    return info;
  }

  /**
   * Utility method to ease write write custom assertions classes, you can use format specifiers in error message, they
   * will be replaced by the given arguments.
   * <p>
   * Moreover, this method honors any description ({@link #as(String, Object...)} or overridden error message defined by the user (
   * {@link #overridingErrorMessage(String, Object...)}.
   * <p>
   * Example :
   * 
   * <pre>
   * public TolkienCharacterAssert hasName(String name) {
   *   // check that actual TolkienCharacter we want to make assertions on is not null.
   *   isNotNull();
   * 
   *   // check condition
   *   if (!actual.getName().equals(name)) {
   *     failWithMessage(&quot;Expected character's name to be &lt;%s&gt; but was &lt;%s&gt;&quot;, name, actual.getName());
   *   }
   * 
   *   // return the current assertion for method chaining
   *   return this;
   * }
   * </pre>
   * 
   * @param errorMessage the error message to format
   * @param arguments the arguments referenced by the format specifiers in the errorMessage string.
   */
  protected void failWithMessage(String errorMessage, Object... arguments) {
    throw Failures.instance().failure(info, new BasicErrorMessageFactory(errorMessage, arguments));
  }

  /** {@inheritDoc} */
  @Override
  public S as(String description, Object... args) {
    return describedAs(description, args);
  }

  /** {@inheritDoc} */
  @Override
  public S as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  @Override
  public S describedAs(String description, Object... args) {
    info.description(description, args);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S describedAs(Description description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isEqualTo(Object expected) {
    objects.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotEqualTo(Object other) {
    objects.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public S isNotNull() {
    objects.assertNotNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isSameAs(Object expected) {
    objects.assertSame(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotSameAs(Object other) {
    objects.assertNotSame(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isIn(Object... values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotIn(Object... values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isIn(Iterable<?> values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotIn(Iterable<?> values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S is(Condition<? super A> condition) {
    conditions.assertIs(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNot(Condition<? super A> condition) {
    conditions.assertIsNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S has(Condition<? super A> condition) {
    conditions.assertHas(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotHave(Condition<? super A> condition) {
    conditions.assertDoesNotHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isInstanceOf(Class<?> type) {
    objects.assertIsInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isInstanceOfAny(Class<?>... types) {
    objects.assertIsInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotInstanceOf(Class<?> type) {
    objects.assertIsNotInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotInstanceOfAny(Class<?>... types) {
    objects.assertIsNotInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameClassAs(Object other) {
    objects.assertHasSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotHaveSameClassAs(Object other) {
    objects.assertDoesNotHaveSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isExactlyInstanceOf(Class<?> type) {
    objects.assertIsExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotExactlyInstanceOf(Class<?> type) {
    objects.assertIsNotExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isOfAnyClassIn(Class<?>... types) {
    objects.assertIsOfAnyClassIn(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotOfAnyClassIn(Class<?>... types) {
    objects.assertIsNotOfAnyClassIn(info, actual, types);
    return myself;
  }

  /**
   * The description of this assertion set with {@link #describedAs(String, Object...)} or {@link #describedAs(Description)}.
   * 
   * @return the description String representation of this assertion.
   */
  public String descriptionText() {
    return info.descriptionText();
  }

  /**
   * Overrides Fest default error message by the given one.
   * <p>
   * The new error message is built using {@link String#format(String, Object...)} if you provide args parameter (if you
   * don't, the error message is taken as it is).
   * <p>
   * Example :
   * 
   * <pre>
   * assertThat(player.isRookie()).overridingErrorMessage(&quot;Expecting Player &lt;%s&gt; to be a rookie but was not.&quot;, player)
   *                              .isTrue();
   * </pre>
   * 
   * @param newErrorMessage the error message that will replace the default one provided by Fest.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   * @throws exception see {@link String#format(String, Object...)} exception clause.
   */
  public S overridingErrorMessage(String newErrorMessage, Object... args) {
    info.overridingErrorMessage(formatIfArgs(newErrorMessage, args));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingComparator(Comparator<? super A> customComparator) {
    // using a specific strategy to compare actual with other objects.
    this.objects = new Objects(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingDefaultComparator() {
    // fall back to default strategy to compare actual with other objects.
    this.objects = Objects.instance();
    return myself;
  }

  /**
   * {@inheritDoc}
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final boolean equals(Object obj) {
    throw new UnsupportedOperationException("'equals' is not supported...maybe you intended to call 'isEqualTo'");
  }

  /**
   * Always returns 1.
   * 
   * @return 1.
   */
  @Override
  public final int hashCode() {
    return 1;
  }

}
