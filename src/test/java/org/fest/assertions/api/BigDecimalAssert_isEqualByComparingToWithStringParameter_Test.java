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
package org.fest.assertions.api;

import static java.math.BigDecimal.ONE;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.internal.Comparables;

/**
 * Tests for <code>{@link BigDecimalAssert#isEqualByComparingTo(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimalAssert_isEqualByComparingToWithStringParameter_Test {

  private static final String ONE_AS_STRING = "1";
  private BigDecimalAssert assertions;

  @Before
  public void setUp() {}

  @Test
  public void should_verify_that_actual_is_negative() {
    Comparables comparables = mock(Comparables.class);
    assertions = new BigDecimalAssert(ONE);
    assertions.comparables = comparables;
    assertions.isEqualByComparingTo(ONE_AS_STRING);
    verify(comparables).assertEqualByComparison(assertions.info, assertions.actual, new BigDecimal(ONE_AS_STRING));
  }

  @Test
  public void should_return_this() {
    assertions = new BigDecimalAssert(ONE);
    BigDecimalAssert returned = assertions.isEqualByComparingTo(ONE_AS_STRING);
    assertSame(assertions, returned);
  }
}
