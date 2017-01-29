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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 
 * A {@link BigDecimal} {@link Comparator} based on {@link BigDecimal#compareTo(BigDecimal)}.<br>
 * Is useful if ones wants to use BigDecimal assertions based on {@link BigDecimal#compareTo(BigDecimal)} instead of
 * {@link BigDecimal#equals(Object)} method.
 * 
 * @author Joel Costigliola
 */
public class BigDecimalComparator extends AbstractComparableNumberComparator<BigDecimal> {

  /**
   * an instance of {@link BigDecimalComparator}.
   */
  public static final BigDecimalComparator BIG_DECIMAL_COMPARATOR = new BigDecimalComparator();

}
