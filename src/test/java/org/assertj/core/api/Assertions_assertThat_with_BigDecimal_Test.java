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

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(BigDecimal)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Assertions_assertThat_with_BigDecimal_Test {

  @Test
  public void should_create_Assert() {
    AbstractBigDecimalAssert<?> assertions = Assertions.assertThat(ZERO);
    assertNotNull(assertions);
  }

  @Test
  public void should_pass_actual() {
    AbstractBigDecimalAssert<?> assertions = Assertions.assertThat(ONE);
    assertSame(ONE, assertions.actual);
  }

  @Test
  public void isCloseTo_within_offset_should_pass() {
    final BigDecimal actual = new BigDecimal("8.1");
    final BigDecimal other = new BigDecimal("8.0");
    assertThat(actual).isCloseTo(other, within(new BigDecimal("0.2")));
    // if difference is exactly equals to offset value, it's ok
    assertThat(actual).isCloseTo(other, within(new BigDecimal("0.1")));
  }

}
