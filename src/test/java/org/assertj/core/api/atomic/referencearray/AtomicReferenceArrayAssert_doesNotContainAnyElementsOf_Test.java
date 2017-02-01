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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;

public class AtomicReferenceArrayAssert_doesNotContainAnyElementsOf_Test extends AtomicReferenceArrayAssertBaseTest {

  private final List<String> values = newArrayList("Yoda", "Luke");

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.doesNotContainAnyElementsOf(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContainAnyElementsOf(info(), internalArray(), values);
  }
}
