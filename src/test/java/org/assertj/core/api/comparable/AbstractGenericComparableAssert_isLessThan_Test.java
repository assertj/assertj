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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.comparable;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.core.api.AbstractGenericComparableAssertBaseTest;
import org.assertj.core.api.RawComparableAssert;


/**
 * Tests for <code>{@link AbstractComparableAssert#isLessThan(Comparable)}</code>.
 * 
 * @author Alex Ruiz
 */
class AbstractGenericComparableAssert_isLessThan_Test extends AbstractGenericComparableAssertBaseTest {

  @Override
  protected RawComparableAssert invoke_api_method() {
    return assertions.isLessThan("foo");
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void verify_internal_effects() {
    verify(comparables).assertLessThan(getInfo(assertions), getActual(assertions), (Comparable) "foo");
  }
}