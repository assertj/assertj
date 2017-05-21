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

import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

import java.util.Set;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains only a given set of
 * values and nothing else failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author William Delanoue
 */
public class ShouldContainsOnlyOnce extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainsOnlyOnce}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notOnlyOnce values in {@code actual} that were not only once in {@code expected}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainsOnlyOnce(Object actual, Object expected, Set<?> notFound,
      Set<?> notOnlyOnce, ComparisonStrategy comparisonStrategy) {
    if (!isNullOrEmpty(notFound) && !isNullOrEmpty(notOnlyOnce))
      return new ShouldContainsOnlyOnce(actual, expected, notFound, notOnlyOnce, comparisonStrategy);
    if (!isNullOrEmpty(notFound))
      return new ShouldContainsOnlyOnce(actual, expected, notFound, comparisonStrategy);
    // case where no elements were missing but some appeared more than once.
    return new ShouldContainsOnlyOnce(notOnlyOnce, actual, expected, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainsOnlyOnce}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notOnlyOnce values in {@code actual} that were found not only once in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainsOnlyOnce(Object actual, Object expected, Set<?> notFound,
      Set<?> notOnlyOnce) {
    return shouldContainsOnlyOnce(actual, expected, notFound, notOnlyOnce, StandardComparisonStrategy.instance());
  }

  private ShouldContainsOnlyOnce(Object actual, Object expected, Set<?> notFound, Set<?> notOnlyOnce,
      ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto contain only once:%n <%s>%n"
        + "but some elements were not found:%n <%s>%n"
        + "and others were found more than once:%n <%s>%n%s",
          actual, expected, notFound, notOnlyOnce, comparisonStrategy);
  }

  private ShouldContainsOnlyOnce(Object actual, Object expected, Set<?> notFound, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto contain only once:%n <%s>%nbut some elements were not found:%n <%s>%n%s",
          actual, expected, notFound, comparisonStrategy);
  }

  // change the order of parameters to avoid confusion with previous constructor
  private ShouldContainsOnlyOnce(Set<?> notOnlyOnce, Object actual, Object expected,
      ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto contain only once:%n <%s>%nbut some elements were found more than once:%n <%s>%n%s",
          actual, expected, notOnlyOnce, comparisonStrategy);
  }

}
