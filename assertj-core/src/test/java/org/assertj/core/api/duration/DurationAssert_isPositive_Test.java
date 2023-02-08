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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.duration;

import static org.mockito.Mockito.verify;

import java.time.Duration;

import org.assertj.core.api.DurationAssert;
import org.assertj.core.api.DurationBaseAssertTest;
import org.junit.jupiter.api.DisplayName;

/**
 * @author Filip Hrisafov
 */
@DisplayName("DurationAssert isPositive")
class DurationAssert_isPositive_Test extends DurationBaseAssertTest {

  @Override
  protected DurationAssert create_assertions() {
    return new DurationAssert(Duration.ofMinutes(10));
  }

  @Override
  protected DurationAssert invoke_api_method() {
    return assertions.isPositive();
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertNotNull(getInfo(assertions), Duration.ofMinutes(10));
    verify(comparables).assertGreaterThan(getInfo(assertions), Duration.ofMinutes(10), Duration.ZERO);
  }
}
