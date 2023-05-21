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
package org.assertj.core.api.iterable;

import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.AbstractIterableAssert#satisfiesOnlyOnce(Consumer)}</code>.
 *
 * @author Stefan Bratanov
 */
class IterableAssert_satisfiesOnlyOnce_Test extends IterableAssertBaseTest {

  @SuppressWarnings("unchecked")
  private final Consumer<Object> consumer = mock(Consumer.class);

  @Override
  protected ConcreteIterableAssert<Object> create_assertions() {
    return new ConcreteIterableAssert<>(list(new Object()));
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.satisfiesOnlyOnce(consumer);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertSatisfiesOnlyOnce(getInfo(assertions), getActual(assertions), consumer);
  }
}
