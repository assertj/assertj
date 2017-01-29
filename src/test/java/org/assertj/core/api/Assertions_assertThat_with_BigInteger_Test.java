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
package org.assertj.core.api;

import org.junit.Test;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for <code>{@link Assertions#assertThat(BigInteger)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Assertions_assertThat_with_BigInteger_Test {

  @Test
  public void should_create_Assert() {
    AbstractBigIntegerAssert<?> assertions = Assertions.assertThat(ZERO);
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() {
    AbstractBigIntegerAssert<?> assertions = Assertions.assertThat(ONE);
    assertThat(assertions.actual).isSameAs(ONE);
  }
}
