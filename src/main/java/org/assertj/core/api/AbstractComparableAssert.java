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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of <code>{@link ComparableAssert}</code>.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public abstract class AbstractComparableAssert<S extends AbstractComparableAssert<S, A>, A extends Comparable<? super A>>
    extends AbstractObjectAssert<S, A> implements ComparableAssert<S, A> {

  @VisibleForTesting
  Comparables comparables = Comparables.instance();

  public AbstractComparableAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public S isEqualByComparingTo(A other) {
    comparables.assertEqualByComparison(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotEqualByComparingTo(A other) {
    comparables.assertNotEqualByComparison(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isLessThan(A other) {
    comparables.assertLessThan(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isLessThanOrEqualTo(A other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isGreaterThan(A other) {
    comparables.assertGreaterThan(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isGreaterThanOrEqualTo(A other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isBetween(A startInclusive, A endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isStrictlyBetween(A startExclusive, A endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
  }

  @Override
  public S usingComparator(Comparator<? super A> customComparator) {
    super.usingComparator(customComparator);
    this.comparables = new Comparables(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    this.comparables = Comparables.instance();
    return myself;
  }

  @Override
  public S inHexadecimal() {
    return super.inHexadecimal();
  }

  @Override
  public S inBinary() {
    return super.inBinary();
  }
}
