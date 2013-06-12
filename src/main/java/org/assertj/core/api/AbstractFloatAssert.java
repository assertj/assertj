/*
 * Created on Oct 24, 2010
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
 * Base class for all implementations of assertions for {@link Float}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractFloatAssert<S extends AbstractFloatAssert<S>> extends AbstractComparableAssert<S, Float> implements FloatingPointNumberAssert<S, Float> {

	@VisibleForTesting
	Floats floats = Floats.instance();

	protected AbstractFloatAssert(Float actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/** {@inheritDoc} */
	@Override
	public S isNaN() {
		floats.assertIsNaN(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNaN() {
		floats.assertIsNotNaN(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isZero() {
		floats.assertIsZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotZero() {
		floats.assertIsNotZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isPositive() {
		floats.assertIsPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNegative() {
		floats.assertIsNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNegative() {
		floats.assertIsNotNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotPositive() {
		floats.assertIsNotPositive(info, actual);
		return myself;
	}

	/**
	 * Verifies that the actual value is equal to the given one.
	 * @param expected the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is not equal to the given one.
	 */
	public S isEqualTo(float expected) {
		floats.assertEqual(info, actual, expected);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isEqualTo(Float expected, Offset<Float> offset) {
		floats.assertEqual(info, actual, expected, offset);
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
	public S isEqualTo(float expected, Offset<Float> offset) {
		floats.assertEqual(info, actual, expected, offset);
		return myself;
	}

	/**
	 * Verifies that the actual value is not equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to the given one.
	 */
	public S isNotEqualTo(float other) {
		floats.assertNotEqual(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or greater than the given one.
	 */
	public S isLessThan(float other) {
		floats.assertLessThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is less than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is greater than the given one.
	 */
	public S isLessThanOrEqualTo(float other) {
		floats.assertLessThanOrEqualTo(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is equal to or less than the given one.
	 */
	public S isGreaterThan(float other) {
		floats.assertGreaterThan(info, actual, other);
		return myself;
	}

	/**
	 * Verifies that the actual value is greater than or equal to the given one.
	 * @param other the given value to compare the actual value to.
	 * @return {@code this} assertion object.
	 * @throws AssertionError if the actual value is {@code null}.
	 * @throws AssertionError if the actual value is less than the given one.
	 */
	public S isGreaterThanOrEqualTo(float other) {
		floats.assertGreaterThanOrEqualTo(info, actual, other);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isBetween(Float start, Float end) {
		floats.assertIsBetween(info, actual, start, end);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isStrictlyBetween(Float start, Float end) {
		floats.assertIsStrictlyBetween(info, actual, start, end);
		return myself;
	}

	@Override
	public S usingComparator(Comparator<? super Float> customComparator) {
		super.usingComparator(customComparator);
		floats = new Floats(new ComparatorBasedComparisonStrategy(customComparator));
		return myself;
	}

	@Override
	public S usingDefaultComparator() {
		super.usingDefaultComparator();
		floats = Floats.instance();
		return myself;
	}
}
