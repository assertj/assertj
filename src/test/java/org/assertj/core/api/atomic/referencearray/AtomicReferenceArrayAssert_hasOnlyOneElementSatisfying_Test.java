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
package org.assertj.core.api.atomic.referencearray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AtomicReferenceArrayAssert_hasOnlyOneElementSatisfying_Test extends AtomicReferenceArrayAssertBaseTest {

  @Mock
  private Consumer<Object> consumer;

  @BeforeEach
  void beforeEach() {
    initMocks(this);
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> create_assertions() {
    return new AtomicReferenceArrayAssert<>(atomicArrayOf(new Object()));
  }

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method() {
    return assertions.hasOnlyOneElementSatisfying(consumer);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertHasOnlyOneElementSatisfying(info(), list(internalArray()), consumer);
  }
}
