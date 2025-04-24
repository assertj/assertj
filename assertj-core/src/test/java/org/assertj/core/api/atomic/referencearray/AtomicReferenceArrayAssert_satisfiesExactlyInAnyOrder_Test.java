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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.assertj.core.api.ThrowingConsumer;

/**
 * Tests for <code>{@link AtomicReferenceArrayAssert#satisfiesExactlyInAnyOrder(Consumer...)}</code>.
 *
 * @author Michael Grafl
 */
class AtomicReferenceArrayAssert_satisfiesExactlyInAnyOrder_Test extends AtomicReferenceArrayAssertBaseTest {

  private ThrowingConsumer<Object>[] requirements = array(element -> assertThat(element).isNotNull());

  @Override
  protected AtomicReferenceArrayAssert<Object> create_assertions() {
    return new AtomicReferenceArrayAssert<>(atomicArrayOf(new Object()));
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.satisfiesExactlyInAnyOrder(requirements);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertSatisfiesExactlyInAnyOrder(info(), list(internalArray()), array(requirements));
  }
}
