/*
 * Created on Aug 01, 2012
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
package org.fest.assertions.api;

import static java.math.BigDecimal.ONE;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.fest.assertions.internal.BigDecimals;
import org.fest.assertions.internal.Comparables;

/**
 * Base class for {@link BigDecimalAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class BigDecimalAssertBaseTest extends BaseTestTemplate<BigDecimalAssert, BigDecimal> {

  protected static final String ONE_AS_STRING = "1";
  protected BigDecimals bigDecimals;
  protected Comparables comparables;

  @Override
  protected BigDecimalAssert create_assertions() {
    return new BigDecimalAssert(ONE);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    bigDecimals = mock(BigDecimals.class);
    assertions.bigDecimals = bigDecimals;
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  protected BigDecimals getBigDecimals(BigDecimalAssert someAssertions) {
    return someAssertions.bigDecimals;
  }
}
