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

import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;

import java.util.function.Predicate;

import org.assertj.core.internal.Iterables;
import org.assertj.core.presentation.PredicateDescription;

/**
 * Assertions for {@link Predicate}.
 *
 * @param <SELF>  the "self" type of this assertion class.
 * @param <PRIMITIVE_PREDICATE> the actual type
 * @param <PRIMITIVE> the type of the wrapped variable in one of the special predicates
 *
 * @author Filip Hrisafov
 */
abstract class AbstractPredicateLikeAssert<SELF extends AbstractPredicateLikeAssert<SELF, PRIMITIVE_PREDICATE, PRIMITIVE>, PRIMITIVE_PREDICATE, PRIMITIVE>
    extends AbstractAssertWithComparator<SELF, PRIMITIVE_PREDICATE> {

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Iterables iterables = Iterables.instance();

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Predicate<PRIMITIVE> primitivePredicate;

  protected AbstractPredicateLikeAssert(PRIMITIVE_PREDICATE actual, Predicate<PRIMITIVE> wrappedPredicate,
                                        Class<?> selfType) {
    super(actual, selfType);
    this.primitivePredicate = wrappedPredicate;
  }

  /**
   * Verifies that the wrapped predicate accepts the given value.
   *
   * @param value the value expected to be accepted
   * @return this assertion object
   */
  protected SELF acceptsInternal(PRIMITIVE value) {
    return executeAssertion(() -> {
      isNotNull();
      if (!primitivePredicate.test(value))
        throwAssertionError(shouldAccept(primitivePredicate, value, PredicateDescription.GIVEN));
    });
  }

  /**
   * Verifies that the wrapped predicate rejects the given value.
   *
   * @param value the value expected to be rejected
   * @return this assertion object
   */
  protected SELF rejectsInternal(PRIMITIVE value) {
    return executeAssertion(() -> {
      isNotNull();
      if (primitivePredicate.test(value))
        throwAssertionError(shouldNotAccept(primitivePredicate, value, PredicateDescription.GIVEN));
    });
  }

  /**
   * Verifies that the wrapped predicate accepts all the given values.
   *
   * @param values the values expected to be accepted
   * @return this assertion object
   */
  protected SELF acceptsAllInternal(Iterable<? extends PRIMITIVE> values) {
    return executeAssertion(() -> {
      isNotNull();
      iterables.assertAllMatch(info, values, primitivePredicate, PredicateDescription.GIVEN);
    });
  }

  /**
   * Verifies that the wrapped predicate rejects all the given values.
   *
   * @param values the values expected to be rejected
   * @return this assertion object
   */
  protected SELF rejectsAllInternal(Iterable<? extends PRIMITIVE> values) {
    return executeAssertion(() -> {
      isNotNull();
      iterables.assertNoneMatch(info, values, primitivePredicate, PredicateDescription.GIVEN);
    });
  }
}
