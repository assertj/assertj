/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.biginteger;

import org.assertj.core.api.BigIntegerAssert;
import org.assertj.core.api.BigIntegerAssertBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.math.BigInteger;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;


public class BigIntegerAssert_usingComparator_Test extends BigIntegerAssertBaseTest {

  @Mock
  private Comparator<BigInteger> comparator;

  @BeforeEach
  public void before() {
    initMocks(this);
  }

  @Override
  protected BigIntegerAssert invoke_api_method() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    return assertions.usingComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(comparator).isSameAs(getObjects(assertions).getComparator());
    assertThat(comparator).isSameAs(getComparables(assertions).getComparator());
  }
}
