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
package org.fest.assertions.api;

import static java.lang.String.format;

import java.util.Comparator;

import org.fest.assertions.core.Assert;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

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
  final WritableAssertionInfo info;

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

  /** {@inheritDoc} */
  public S as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public S as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public S describedAs(String description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  public S describedAs(Description description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  public S isEqualTo(A expected) {
    objects.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotEqualTo(A other) {
    objects.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public S isNotNull() {
    objects.assertNotNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public S isSameAs(A expected) {
    objects.assertSame(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotSameAs(A other) {
    objects.assertNotSame(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public S isIn(A... values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotIn(A... values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S isIn(Iterable<? extends A> values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotIn(Iterable<? extends A> values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public S is(Condition<? super A> condition) {
    conditions.assertIs(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNot(Condition<? super A> condition) {
    conditions.assertIsNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S has(Condition<? super A> condition) {
    conditions.assertHas(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S doesNotHave(Condition<? super A> condition) {
    conditions.assertDoesNotHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public S isInstanceOf(Class<?> type) {
    objects.assertIsInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  public S isInstanceOfAny(Class<?>... types) {
    objects.assertIsInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotInstanceOf(Class<?> type) {
    objects.assertIsNotInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotInstanceOfAny(Class<?>... types) {
    objects.assertIsNotInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  public S hasSameClassAs(Object other) {
    objects.assertHasSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public S doesNotHaveSameClassAs(Object other) {
    objects.assertDoesNotHaveSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public S isExactlyInstanceOf(Class<?> type) {
    objects.assertIsExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotExactlyInstanceOf(Class<?> type) {
    objects.assertIsNotExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  public S isOfAnyClassIn(Class<?>... types) {
    objects.assertIsOfAnyClassIn(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotOfAnyClassIn(Class<?>... types) {
    objects.assertIsNotOfAnyClassIn(info, actual, types);
    return myself;
  }

  /**
   * The description of this assertion set with {@link #describedAs(String)} or {@link #describedAs(Description)}.
   * 
   * @return the description String representation of this assertion.
   */
  public String descriptionText() {
    return info.descriptionText();
  }

  /**
   * Overrides Fest default error message by the given one, the new error message can be built using
   * {@link String#format(String, Object...)}, hence the presence of args parameter.
   * <p>
   * Example :
   * 
   * <pre>
   * assertThat(player.isRookie()).overridingErrorMessage(&quot;Expecting Player &lt;%s&gt; to be a rookie but was not.&quot;, player)
   *     .isTrue();
   * </pre>
   * 
   * @param newErrorMessage the error message that will replace the default one provided by Fest.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   * @throws exception see {@link String#format(String, Object...)} exception clause.
   */
  public S overridingErrorMessage(String newErrorMessage, Object... args) {
    info.overridingErrorMessage(format(newErrorMessage, args));
    return myself;
  }

  /** {@inheritDoc} */
  public S usingComparator(Comparator<? super A> customComparator) {
    // using a specific strategy to compare actual with other objects.
    this.objects = new Objects(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  public S usingDefaultComparator() {
    // fall back to default strategy to compare actual with other objects.
    this.objects = Objects.instance();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
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
