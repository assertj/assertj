/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.comparable;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractUniversalComparableAssertBaseTest;
import org.assertj.core.api.UniversalComparableAssert;

class AbstractUniversalComparableAssert_isGreaterThan_Test extends AbstractUniversalComparableAssertBaseTest {

  @Override
  protected UniversalComparableAssert<String> invoke_api_method() {
    return assertions.isGreaterThan("bcd");
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertGreaterThan(getInfo(assertions), getActual(assertions), "bcd");
  }

}
