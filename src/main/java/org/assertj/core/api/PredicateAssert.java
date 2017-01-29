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

import java.util.function.Predicate;

/**
 * Assertions for {@link Predicate}.
 *
 * @param <T> type of the value contained in the {@link Predicate}.
 * @author Filip Hrisafov
 *
 * @since 3.5.0
 */
public class PredicateAssert<T> extends AbstractPredicateAssert<PredicateAssert<T>, T> {

  protected PredicateAssert(Predicate<T> actual) {
    super(actual, PredicateAssert.class);
  }

  @SafeVarargs
  @Override
  public final PredicateAssert<T> accepts(T... values) {
    return super.accepts(values);
  }

  @SafeVarargs
  @Override
  public final PredicateAssert<T> rejects(T... values) {
    return super.rejects(values);
  }

}
