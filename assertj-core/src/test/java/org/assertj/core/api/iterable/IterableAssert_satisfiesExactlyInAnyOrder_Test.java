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
package org.assertj.core.api.iterable;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.AbstractIterableAssert#satisfiesExactlyInAnyOrder(Consumer...)}</code>.
 *
 * @author Michael Grafl
 */
class IterableAssert_satisfiesExactlyInAnyOrder_Test extends IterableAssertBaseTest {

  @SuppressWarnings("unchecked")
  private final Consumer<Object> consumer = mock(Consumer.class);

  @Override
  protected ConcreteIterableAssert<Object> create_assertions() {
    return new ConcreteIterableAssert<>(list(new Object()));
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.satisfiesExactlyInAnyOrder(consumer);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertSatisfiesExactlyInAnyOrder(getInfo(assertions), getActual(assertions), array(consumer));
  }
}
