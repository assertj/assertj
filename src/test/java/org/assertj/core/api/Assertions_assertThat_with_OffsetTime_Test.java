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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(OffsetTime)}</code>.
 *
 * @author Alexander Bischof
 */
public class Assertions_assertThat_with_OffsetTime_Test {

  private OffsetTime actual;

  @BeforeEach
  public void before() {
    actual = OffsetTime.now();
  }

  @Test
  public void should_create_Assert() {
    AbstractOffsetTimeAssert<?> assertions = Assertions.assertThat(actual);
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() {
    AbstractOffsetTimeAssert<?> assertions = Assertions.assertThat(actual);
    assertThat(actual).isSameAs(assertions.actual);
  }
}
