/*
 * Created on Oct 17, 2010
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
 * Base class for all implementations of assertions for {@link Integer}s.
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
public abstract class AbstractIntegerAssert<S extends AbstractIntegerAssert<S>> extends AbstractComparableAssert<S, Integer> implements NumberAssert<S, Integer> {

	@VisibleForTesting
	Integers integers = Integers.instance();

	protected AbstractIntegerAssert(Integer actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the actual value is equal to the given one.
	 * @param expected the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is not equal to the given one.
	 */
	public S isEqualTo(int expected) {
		integers.assertEqual(info, actual, expected);
		return myself;
	}

	/**
	 * Verifies that the actual value is not equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to the given one.
	 */
	public S isNotEqualTo(int other) {
		integers.assertNotEqual(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isZero() {
		integers.assertIsZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotZero() {
		integers.assertIsNotZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isPositive() {
		integers.assertIsPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNegative() {
		integers.assertIsNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNegative() {
		integers.assertIsNotNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotPositive() {
		integers.assertIsNotPositive(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or greater than the given one.
	 */
	public S isLessThan(int other) {
		integers.assertLessThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is greater than the given one.
	 */
	public S isLessThanOrEqualTo(int other) {
		integers.assertLessThanOrEqualTo(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or less than the given one.
	 */
	public S isGreaterThan(int other) {
		integers.assertGreaterThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is less than the given one.
	 */
	public S isGreaterThanOrEqualTo(int other) {
		integers.assertGreaterThanOrEqualTo(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isBetween(Integer start, Integer end) {
		integers.assertIsBetween(info, actual, start, end);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isStrictlyBetween(Integer start, Integer end) {
		integers.assertIsStrictlyBetween(info, actual, start, end);
		return myself;
	}

	@Override
	public S usingComparator(Comparator<? super Integer> customComparator) {
		super.usingComparator(customComparator);
		integers = new Integers(new ComparatorBasedComparisonStrategy(customComparator));
		return myself;
	}

	@Override
	public S usingDefaultComparator() {
		super.usingDefaultComparator();
		integers = Integers.instance();
		return myself;
	}
}
