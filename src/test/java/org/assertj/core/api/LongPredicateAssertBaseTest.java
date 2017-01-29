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
package org.assertj.core.api;

import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.internal.Iterables;

import static org.mockito.Mockito.mock;

/**
 * Base class for {@link LongPredicateAssert} tests.
 *
 * @author Filip Hrisafov
 */
public abstract class LongPredicateAssertBaseTest extends BaseTestTemplate<LongPredicateAssert, LongPredicate> {

  protected Iterables iterables;
  protected Predicate<Long> wrapped;

  @Override
  protected LongPredicateAssert create_assertions() {
    return new LongPredicateAssert(value -> value <= 2);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    iterables = mock(Iterables.class);
    assertions.iterables = iterables;
    wrapped = assertions.primitivePredicate;
  }
}
