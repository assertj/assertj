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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.util.function.Predicate;

/**
 * Assertions for {@link Predicate}.
 *
 * @author Filip Hrisafov
 * @param <T> type of the value contained in the {@link Predicate}.
 *
 * @since 3.5.0
 */
public class PredicateAssert<T> extends AbstractPredicateAssert<PredicateAssert<T>, T> {

  public static <T> PredicateAssert<T> assertThatPredicate(Predicate<T> actual) {
    return new PredicateAssert<>(actual);
  }

  protected PredicateAssert(Predicate<T> actual) {
    super(actual, PredicateAssert.class);
  }

}
