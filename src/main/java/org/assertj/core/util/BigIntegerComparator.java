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

import java.math.BigInteger;
import java.util.Comparator;

/**
 *
 * A {@link BigInteger} {@link Comparator} based on {@link BigInteger#compareTo(BigInteger)}.<br>
 * Is useful if ones wants to use BigInteger assertions based on {@link BigInteger#compareTo(BigInteger)} instead of
 * {@link BigInteger#equals(Object)} method.
 */
public class BigIntegerComparator extends AbstractComparableNumberComparator<BigInteger> {

  /**
   * an instance of {@link BigIntegerComparator}.
   */
  public static final BigIntegerComparator BIG_INTEGER_COMPARATOR = new BigIntegerComparator();

}
