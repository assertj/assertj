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

import java.util.Comparator;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Comparables;

/// Base class for all implementations of [ComparableAssert].
///
/// @param <SELF> the self type of this assertion class.
/// @param <ACTUAL> the type of the actual value.
/// @param <COMPARABLE_TYPE> the type accepted by comparable assertion methods.
///
/// @author Alex Ruiz
/// @author Mikhail Mazursky
public abstract class AbstractComparableAssert<SELF extends AbstractComparableAssert<SELF, ACTUAL, COMPARABLE_TYPE>, ACTUAL extends Comparable<? super COMPARABLE_TYPE>, COMPARABLE_TYPE>
    extends AbstractObjectAssert<SELF, ACTUAL> implements ComparableAssert<SELF, COMPARABLE_TYPE> {

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Comparables comparables = new Comparables();

  /**
   * Creates a new comparable assertion.
   *
   * @param actual the actual value to verify
   * @param selfType the type of the concrete assertion
   */
  protected AbstractComparableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isEqualByComparingTo(COMPARABLE_TYPE other) {
    return executeAssertion(() -> comparables.assertEqualByComparison(info, actual, other));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotEqualByComparingTo(COMPARABLE_TYPE other) {
    return executeAssertion(() -> comparables.assertNotEqualByComparison(info, actual, other));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isLessThan(COMPARABLE_TYPE other) {
    return executeAssertion(() -> comparables.assertLessThan(info, actual, other));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isLessThanOrEqualTo(COMPARABLE_TYPE other) {
    return executeAssertion(() -> comparables.assertLessThanOrEqualTo(info, actual, other));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isGreaterThan(COMPARABLE_TYPE other) {
    return executeAssertion(() -> comparables.assertGreaterThan(info, actual, other));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isGreaterThanOrEqualTo(COMPARABLE_TYPE other) {
    return executeAssertion(() -> comparables.assertGreaterThanOrEqualTo(info, actual, other));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isBetween(COMPARABLE_TYPE startInclusive, COMPARABLE_TYPE endInclusive) {
    return executeAssertion(() -> comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true));
  }

  /** {@inheritDoc} */
  @Override
  public SELF isStrictlyBetween(COMPARABLE_TYPE startExclusive, COMPARABLE_TYPE endExclusive) {
    return executeAssertion(() -> comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false));
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    this.comparables = new Comparables(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.comparables = new Comparables();
    return super.usingDefaultComparator();
  }

  @Override
  @CheckReturnValue
  public SELF inHexadecimal() {
    return super.inHexadecimal();
  }

  @Override
  @CheckReturnValue
  public SELF inBinary() {
    return super.inBinary();
  }
}
