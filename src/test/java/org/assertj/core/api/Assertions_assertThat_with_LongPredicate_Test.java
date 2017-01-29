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

import java.util.function.LongPredicate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for <code>{@link Assertions#assertThat(LongPredicate)}</code>.
 *
 * @author Filip Hrisafov
 */
public class Assertions_assertThat_with_LongPredicate_Test {

  private LongPredicate actual;

  @Before
  public void before() {
    actual = value -> value == 1;
  }

  @Test
  public void should_create_Assert() {
    LongPredicateAssert assertions = Assertions.assertThat(actual);
    assertNotNull(assertions);
  }

  @Test
  public void should_pass_actual() {
    LongPredicateAssert assertions = Assertions.assertThat(actual);
    assertSame(actual, assertions.actual);
  }
}
