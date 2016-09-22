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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.presentation.PredicateDescription;

public class ElementsShouldMatch extends BasicErrorMessageFactory {

  public static <T> ErrorMessageFactory elementsShouldMatch(Object actual, T elementNotMatchingPredicate, PredicateDescription predicateDescription) {
    return new ElementsShouldMatch(actual, elementNotMatchingPredicate, predicateDescription);
  }

  private ElementsShouldMatch(Object actual, Object notSatisfies, PredicateDescription predicateDescription) {
    super("%nExpecting all elements of:%n  <%s>%nto match %s predicate but this element did not:%n  <%s>", actual, predicateDescription, notSatisfies);
  }

}
