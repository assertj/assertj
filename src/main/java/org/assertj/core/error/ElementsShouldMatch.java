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
package org.assertj.core.error;

import org.assertj.core.presentation.PredicateDescription;

public class ElementsShouldMatch extends BasicErrorMessageFactory {

  private static final String SINGLE_NON_MATCHING_ELEMENT = "%nExpecting all elements of:%n  <%s>%nto match %s predicate but this element did not:%n  <%s>";
  private static final String MULTIPLE_NON_MATCHING_ELEMENT = "%nExpecting all elements of:%n  <%s>%nto match %s predicate but these elements did not:%n  <%s>";

  public static <T> ErrorMessageFactory elementsShouldMatch(Object actual, T elementsNotMatchingPredicate, PredicateDescription predicateDescription) {
    return elementsNotMatchingPredicate instanceof Iterable
        ? new ElementsShouldMatch(actual, (Iterable<?>) elementsNotMatchingPredicate, predicateDescription)
        : new ElementsShouldMatch(actual, elementsNotMatchingPredicate, predicateDescription);
  }

  private ElementsShouldMatch(Object actual, Object notMatching, PredicateDescription predicateDescription) {
    super(SINGLE_NON_MATCHING_ELEMENT, actual, predicateDescription, notMatching);
  }

  private ElementsShouldMatch(Object actual, Iterable<?> notMatching, PredicateDescription predicateDescription) {
    super(MULTIPLE_NON_MATCHING_ELEMENT, actual, predicateDescription, notMatching);
  }

}
