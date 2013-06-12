/*
 * Created on Oct 25, 2010
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

import org.assertj.core.data.Offset;
import org.assertj.core.internal.*;
import org.assertj.core.util.VisibleForTesting;


/**
 * Base class for all implementations of assertions for {@link Double}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractDoubleAssert<S extends AbstractDoubleAssert<S>> extends AbstractComparableAssert<S, Double> implements FloatingPointNumberAssert<S, Double> {

	@VisibleForTesting
	Doubles doubles = Doubles.instance();

	protected AbstractDoubleAssert(Double actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/** {@inheritDoc} */
	@Override
	public S isNaN() {
		doubles.assertIsNaN(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNaN() {
		doubles.assertIsNotNaN(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isZero() {
		doubles.assertIsZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotZero() {
		doubles.assertIsNotZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isPositive() {
		doubles.assertIsPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNegative() {
		doubles.assertIsNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNegative() {
		doubles.assertIsNotNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotPositive() {
		doubles.assertIsNotPositive(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual value is equal to the given one.
	 * @param expected the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is not equal to the given one.
	 */
	public S isEqualTo(double expected) {
		doubles.assertEqual(info, actual, expected);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isEqualTo(Double expected, Offset<Double> offset) {
		doubles.assertEqual(info, actual, expected, offset);
		return myself;
	}

	/**
	 * Verifies that the actual value is equal to the given one, within a positive offset.
	 * @param expected the given value to compare the actual value to.
	 * @param offset the given positive offset.
	 * @return {@code this} assertion object.
	 * @throws NullPointerException if the given offset is {@code null}.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is not equal to the given one.
	 */
	public S isEqualTo(double expected, Offset<Double> offset) {
		doubles.assertEqual(info, actual, expected, offset);
		return myself;
	}

	/**
	 * Verifies that the actual value is not equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to the given one.
	 */
	public S isNotEqualTo(double other) {
		doubles.assertNotEqual(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or greater than the given one.
	 */
	public S isLessThan(double other) {
		doubles.assertLessThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is greater than the given one.
	 */
	public S isLessThanOrEqualTo(double other) {
		doubles.assertLessThanOrEqualTo(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or less than the given one.
	 */
	public S isGreaterThan(double other) {
		doubles.assertGreaterThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is less than the given one.
	 */
	public S isGreaterThanOrEqualTo(double other) {
		doubles.assertGreaterThanOrEqualTo(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isBetween(Double start, Double end) {
		doubles.assertIsBetween(info, actual, start, end);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isStrictlyBetween(Double start, Double end) {
		doubles.assertIsStrictlyBetween(info, actual, start, end);
		return myself;
	}

	@Override
	public S usingComparator(Comparator<? super Double> customComparator) {
		super.usingComparator(customComparator);
		doubles = new Doubles(new ComparatorBasedComparisonStrategy(customComparator));
		return myself;
	}

	@Override
	public S usingDefaultComparator() {
		super.usingDefaultComparator();
		doubles = Doubles.instance();
		return myself;
	}
}
