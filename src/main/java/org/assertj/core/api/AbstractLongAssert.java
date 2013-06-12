/*
 * Created on Oct 20, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.internal.*;
import org.assertj.core.util.VisibleForTesting;


/**
 * Base class for all implementations of assertions for {@link Long}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractLongAssert<S extends AbstractLongAssert<S>> extends AbstractComparableAssert<S, Long> implements NumberAssert<S, Long> {

	@VisibleForTesting
	Longs longs = Longs.instance();

	protected AbstractLongAssert(Long actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the actual value is equal to the given one.
	 * @param expected the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is not equal to the given one.
	 */
	public S isEqualTo(long expected) {
		longs.assertEqual(info, actual, expected);
		return myself;
	}

	/**
	 * Verifies that the actual value is not equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to the given one.
	 */
	public S isNotEqualTo(long other) {
		longs.assertNotEqual(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isZero() {
		longs.assertIsZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotZero() {
		longs.assertIsNotZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isPositive() {
		longs.assertIsPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNegative() {
		longs.assertIsNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNegative() {
		longs.assertIsNotNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotPositive() {
		longs.assertIsNotPositive(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or greater than the given one.
	 */
	public S isLessThan(long other) {
		longs.assertLessThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is greater than the given one.
	 */
	public S isLessThanOrEqualTo(long other) {
		longs.assertLessThanOrEqualTo(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or less than the given one.
	 */
	public S isGreaterThan(long other) {
		longs.assertGreaterThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is less than the given one.
	 */
	public S isGreaterThanOrEqualTo(long other) {
		longs.assertGreaterThanOrEqualTo(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isBetween(Long start, Long end) {
		longs.assertIsBetween(info, actual, start, end);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isStrictlyBetween(Long start, Long end) {
		longs.assertIsStrictlyBetween(info, actual, start, end);
		return myself;
	}

	@Override
	public S usingComparator(Comparator<? super Long> customComparator) {
		super.usingComparator(customComparator);
		longs = new Longs(new ComparatorBasedComparisonStrategy(customComparator));
		return myself;
	}

	@Override
	public S usingDefaultComparator() {
		super.usingDefaultComparator();
		longs = Longs.instance();
		return myself;
	}
}
