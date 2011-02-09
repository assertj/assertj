/*
 * Created on Feb 8, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api;

import java.math.BigDecimal;

import org.fest.assertions.core.NumberAssert;
import org.fest.assertions.internal.BigDecimals;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for <code>{@link BigDecimal}</code>s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(BigDecimal)}</code>.
 * </p>
 *
 * @author David DIDIER
 * @author Ted M. Young
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class BigDecimalAssert extends AbstractUnevenComparableAssert<BigDecimalAssert, BigDecimal> implements
    NumberAssert<BigDecimal> {

  @VisibleForTesting BigDecimals bigDecimals = BigDecimals.instance();

  protected BigDecimalAssert(BigDecimal actual) {
    super(actual, BigDecimalAssert.class);
  }

  /** {@inheritDoc} */
  public BigDecimalAssert isZero() {
    bigDecimals.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public BigDecimalAssert isNotZero() {
    bigDecimals.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public BigDecimalAssert isPositive() {
    bigDecimals.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public BigDecimalAssert isNegative() {
    bigDecimals.assertIsNegative(info, actual);
    return myself;
  }
}
