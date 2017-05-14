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
package org.assertj.core.api.localdatetime;

import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.assertj.core.api.LocalDateTimeAssert;

public class LocalDateTimeAssert_isStrictlyBetween_Test extends org.assertj.core.api.LocalDateTimeAssertBaseTest {

  private LocalDateTime before = now.minusSeconds(1);
  private LocalDateTime after = now.plusSeconds(1);

  @Override
  protected LocalDateTimeAssert invoke_api_method() {
    return assertions.isStrictlyBetween(before, after);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertIsBetween(getInfo(assertions), getActual(assertions), before, after, false, false);
  }

}
