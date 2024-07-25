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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of <code>{@link ComparableAssert}</code>.
 *
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public abstract class AbstractComparableAssert<SELF extends AbstractComparableAssert<SELF, ACTUAL>, ACTUAL extends Comparable<? super ACTUAL>>
    extends AbstractObjectAssert<SELF, ACTUAL> implements ComparableAssert<SELF, ACTUAL> {

  @VisibleForTesting
  Comparables comparables = new Comparables();

  protected AbstractComparableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isEqualByComparingTo(ACTUAL other) {
    comparables.assertEqualByComparison(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotEqualByComparingTo(ACTUAL other) {
    comparables.assertNotEqualByComparison(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isLessThan(ACTUAL other) {
    comparables.assertLessThan(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isLessThanOrEqualTo(ACTUAL other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isGreaterThan(ACTUAL other) {
    comparables.assertGreaterThan(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isGreaterThanOrEqualTo(ACTUAL other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isBetween(ACTUAL startInclusive, ACTUAL endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isStrictlyBetween(ACTUAL startExclusive, ACTUAL endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
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
