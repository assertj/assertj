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

import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;

import java.util.function.Predicate;

import org.assertj.core.internal.Iterables;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link Predicate}.
 *
 * @author Filip Hrisafov
 *
 * @param <PRIMITIVE_PREDICATE> the actual type
 * @param <PRIMITIVE> the type of the wrapped variable in one of the special predicates
 */
abstract class AbstractPredicateLikeAssert<SELF extends AbstractPredicateLikeAssert<SELF, PRIMITIVE_PREDICATE, PRIMITIVE>, PRIMITIVE_PREDICATE, PRIMITIVE>
    extends AbstractAssert<SELF, PRIMITIVE_PREDICATE> {

  @VisibleForTesting
  Iterables iterables = Iterables.instance();

  @VisibleForTesting
  Predicate<PRIMITIVE> primitivePredicate;

  protected AbstractPredicateLikeAssert(PRIMITIVE_PREDICATE actual, Predicate<PRIMITIVE> wrappedPredicate,
                                        Class<?> selfType) {
    super(actual, selfType);
    this.primitivePredicate = wrappedPredicate;
  }

  protected SELF acceptsInternal(PRIMITIVE value) {
    isNotNull();
    if (!primitivePredicate.test(value))
      throwAssertionError(shouldAccept(primitivePredicate, value, PredicateDescription.GIVEN));
    return myself;
  }

  protected SELF rejectsInternal(PRIMITIVE value) {
    isNotNull();
    if (primitivePredicate.test(value))
      throwAssertionError(shouldNotAccept(primitivePredicate, value, PredicateDescription.GIVEN));
    return myself;
  }

  protected SELF acceptsAllInternal(Iterable<? extends PRIMITIVE> values) {
    isNotNull();
    iterables.assertAllMatch(info, values, primitivePredicate, PredicateDescription.GIVEN);
    return myself;
  }

  protected SELF rejectsAllInternal(Iterable<? extends PRIMITIVE> values) {
    isNotNull();
    iterables.assertNoneMatch(info, values, primitivePredicate, PredicateDescription.GIVEN);
    return myself;
  }
}
