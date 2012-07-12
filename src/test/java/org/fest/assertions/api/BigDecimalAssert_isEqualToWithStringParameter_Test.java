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

import org.junit.Test;

import org.fest.assertions.internal.Objects;

/**
 * Tests for <code>{@link BigDecimalAssert#isEqualTo(String)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimalAssert_isEqualToWithStringParameter_Test {

  private static final String ONE_AS_STRING = "1";

  @Test
  public void should_verify_that_actual_is_negative() {
    Objects objects = mock(Objects.class);
    BigDecimalAssert assertions = new BigDecimalAssert(ONE);
    assertions.objects = objects;
    assertions.isEqualTo(ONE_AS_STRING);
    verify(objects).assertEqual(assertions.info, assertions.actual, new BigDecimal(ONE_AS_STRING));
  }

  @Test
  public void should_return_this() {
    BigDecimalAssert assertions = new BigDecimalAssert(ONE);
    BigDecimalAssert returned = assertions.isEqualTo(ONE_AS_STRING);
    assertSame(assertions, returned);
  }
}
