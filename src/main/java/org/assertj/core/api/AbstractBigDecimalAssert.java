/*
 * Created on Feb 8, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;

import java.math.BigDecimal;
import java.util.Comparator;


/**
 * Base class for all implementations of assertions for {@link BigDecimal}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author David DIDIER
 * @author Ted M. Young
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public abstract class AbstractBigDecimalAssert<S extends AbstractBigDecimalAssert<S>> extends AbstractUnevenComparableAssert<S, BigDecimal> implements
		NumberAssert<S, BigDecimal> {

	@VisibleForTesting
	BigDecimals bigDecimals = BigDecimals.instance();

	protected AbstractBigDecimalAssert(BigDecimal actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/** {@inheritDoc} */
	@Override
	public S isZero() {
		bigDecimals.assertIsZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotZero() {
		bigDecimals.assertIsNotZero(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isPositive() {
		bigDecimals.assertIsPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNegative() {
		bigDecimals.assertIsNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotPositive() {
		bigDecimals.assertIsNotPositive(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isNotNegative() {
		bigDecimals.assertIsNotNegative(info, actual);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isBetween(BigDecimal start, BigDecimal end) {
		bigDecimals.assertIsBetween(info, actual, start, end);
		return myself;
	}

	/** {@inheritDoc} */
	@Override
	public S isStrictlyBetween(BigDecimal start, BigDecimal end) {
		bigDecimals.assertIsStrictlyBetween(info, actual, start, end);
		return myself;
	}

	/**
	 * Same as {@link AbstractAssert#isEqualTo(Object) isEqualTo(BigDecimal)} but takes care of converting given String to
	 * {@link BigDecimal} for you.
	 */
	public S isEqualTo(String expected) {
		return isEqualTo(new BigDecimal(expected));
	}

	/**
	 * Same as {@link AbstractUnevenComparableAssert#isEqualByComparingTo(Comparable) isEqualByComparingTo(BigDecimal)} but takes
	 * care of converting given String to {@link BigDecimal} for you.
	 */
	public S isEqualByComparingTo(String expected) {
		return isEqualByComparingTo(new BigDecimal(expected));
	}

	@Override
	public S usingComparator(Comparator<? super BigDecimal> customComparator) {
		super.usingComparator(customComparator);
		this.bigDecimals = new BigDecimals(new ComparatorBasedComparisonStrategy(customComparator));
		return myself;
	}

	@Override
	public S usingDefaultComparator() {
		super.usingDefaultComparator();
		this.bigDecimals = BigDecimals.instance();
		return myself;
	}
}
