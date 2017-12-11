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
 * Copyright 2017 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.presentation.PredicateDescription;

public class AnyElementShouldMatch extends BasicErrorMessageFactory {
  private static final String SINGLE_NON_MATCHING_ELEMENT = "%nExpecting any element of:%n  <%s>%nto match %s predicate but this element did not:%n  <%s>";
  private static final String MULTIPLE_NON_MATCHING_ELEMENT = "%nExpecting any element of:%n  <%s>%nto match %s predicate but these elements did not:%n  <%s>";

  public static <T> ErrorMessageFactory anyElementShouldMatch(Object actual, T elementsNotMatchingPredicate, PredicateDescription predicateDescription) {
    return elementsNotMatchingPredicate instanceof Iterable
        ? new AnyElementShouldMatch(actual, (Iterable<?>) elementsNotMatchingPredicate, predicateDescription)
        : new AnyElementShouldMatch(actual, elementsNotMatchingPredicate, predicateDescription);
  }

  private AnyElementShouldMatch(Object actual, Object notMatching, PredicateDescription predicateDescription) {
    super(SINGLE_NON_MATCHING_ELEMENT, actual, predicateDescription, notMatching);
  }

  private AnyElementShouldMatch(Object actual, Iterable<?> notMatching, PredicateDescription predicateDescription) {
    super(MULTIPLE_NON_MATCHING_ELEMENT, actual, predicateDescription, notMatching);
  }
}
