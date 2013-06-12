/*
 * Created on Oct 20, 2010
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

import java.util.Comparator;

import org.assertj.core.internal.*;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Short}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractShortAssert<S extends AbstractShortAssert<S>> extends AbstractComparableAssert<S, Short>
		implements NumberAssert<S, Short> {

	@VisibleForTesting
	Shorts shorts = Shorts.instance();

	protected AbstractShortAssert(Short actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the actual value is equal to the given one.
	 *
	 * @param expected the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is not equal to the given one.
	 */
	public S isEqualTo(short expected) {
		shorts.assertEqual(info, actual, expected);
		return myself;
	}

	/**
	 * Verifies that the actual value is not equal to the given one.
	 *
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to the given one.
	 */
	public S isNotEqualTo(short other) {
		shorts.assertNotEqual(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isZero() {
		shorts.assertIsZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotZero() {
		shorts.assertIsNotZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isPositive() {
		shorts.assertIsPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNegative() {
		shorts.assertIsNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNegative() {
		shorts.assertIsNotNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotPositive() {
		shorts.assertIsNotPositive(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than the given one.
	 *
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or greater than the given one.
	 */
	public S isLessThan(short other) {
		shorts.assertLessThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than or equal to the given one.
	 *
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is greater than the given one.
	 */
	public S isLessThanOrEqualTo(short other) {
		shorts.assertLessThanOrEqualTo(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than the given one.
	 *
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or less than the given one.
	 */
	public S isGreaterThan(short other) {
		shorts.assertGreaterThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than or equal to the given one.
	 *
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is less than the given one.
	 */
	public S isGreaterThanOrEqualTo(short other) {
		shorts.assertGreaterThanOrEqualTo(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isBetween(Short start, Short end) {
		shorts.assertIsBetween(info, actual, start, end);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isStrictlyBetween(Short start, Short end) {
		shorts.assertIsStrictlyBetween(info, actual, start, end);
		return myself;
	}

	@Override
	public S usingComparator(Comparator<? super Short> customComparator) {
		super.usingComparator(customComparator);
		shorts = new Shorts(new ComparatorBasedComparisonStrategy(customComparator));
		return myself;
	}

	@Override
	public S usingDefaultComparator() {
		super.usingDefaultComparator();
		shorts = Shorts.instance();
		return myself;
	}
}
