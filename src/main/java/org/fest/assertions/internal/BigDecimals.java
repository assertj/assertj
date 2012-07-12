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
package org.fest.assertions.internal;

import java.math.BigDecimal;

import org.fest.util.*;

/**
 * Reusable assertions for <code>{@link BigDecimal}</code>s.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigDecimals extends Numbers<BigDecimal> {

  private static final BigDecimals INSTANCE = new BigDecimals();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static BigDecimals instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  BigDecimals() {
    super();
  }

  public BigDecimals(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected BigDecimal zero() {
    return BigDecimal.ZERO;
  }
}
