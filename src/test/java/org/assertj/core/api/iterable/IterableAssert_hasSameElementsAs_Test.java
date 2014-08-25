/*
 * Created on August 21, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.*;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.AbstractIterableAssert#hasSameElementsAs(Iterable)}</code>.
 *
 * @author Adam Ruka
 */
public class IterableAssert_hasSameElementsAs_Test extends IterableAssertBaseTest {
  private final Iterable<?> values = newArrayList("Yoda", "Luke");

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.hasSameElementsAs(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertHasSameElementsAs(getInfo(assertions), getActual(assertions), values);
  }
}
