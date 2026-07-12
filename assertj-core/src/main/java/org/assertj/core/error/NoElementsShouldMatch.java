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
package org.assertj.core.error;

import org.assertj.core.presentation.PredicateDescription;

/** Creates errors for iterables containing an element matching a forbidden predicate. */
public class NoElementsShouldMatch extends BasicErrorMessageFactory {

  /**
   * Creates an error for an element matching a forbidden predicate.
   *
   * @param <T> the matching element type
   * @param actual the actual iterable
   * @param elementMatchingPredicate the matching element
   * @param predicateDescription the predicate description
   * @return the error message factory
   */
  public static <T> ErrorMessageFactory noElementsShouldMatch(Object actual, T elementMatchingPredicate,
                                                              PredicateDescription predicateDescription) {
    return new NoElementsShouldMatch(actual, elementMatchingPredicate, predicateDescription);
  }

  private NoElementsShouldMatch(Object actual, Object satisfies, PredicateDescription predicateDescription) {
    super("%nExpecting no elements of:%n  %s%nto match %s predicate but this element did:%n  %s", actual,
          predicateDescription, satisfies);
  }

}
