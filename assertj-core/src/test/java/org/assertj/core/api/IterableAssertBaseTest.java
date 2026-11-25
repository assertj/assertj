/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.util.Collections.emptyList;

import java.util.Comparator;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;

/**
 * Tests for <code>{@link AbstractIterableAssert#contains(Object...)}</code>.
 * 
 * @author Joel Costigliola
 */
public abstract class IterableAssertBaseTest extends BaseTestTemplate<ConcreteIterableAssert<Object>, Iterable<Object>> {

  @Override
  protected ConcreteIterableAssert<Object> create_assertions() {
    return new ConcreteIterableAssert<>(emptyList());
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
  }

  protected ComparisonStrategy getElementComparisonStrategy(ConcreteIterableAssert<Object> assertions) {
    return assertions.getElementComparisonStrategy();
  }

  protected Comparator<?> getElementComparator(ConcreteIterableAssert<Object> assertions) {
    return ((ComparatorBasedComparisonStrategy) assertions.getElementComparisonStrategy()).getComparator();
  }
}
