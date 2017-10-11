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

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that an <code>Iterable</code> is a subset of an other set
 * <code>Iterable</code> failed.
 * 
 * @author Maciej Jaskowski
 */
public class ShouldBeSubsetOf extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeSubsetOf}</code>
   * @param actual the actual set
   * @param values the expected superset
   * @param unexpected the unexpected value
   * @param comparisonStrategy the <code>{@link ComparisonStrategy}</code> used
   * @return the created <code>{@link ErrorMessageFactory}</code>
   */
  public static ErrorMessageFactory shouldBeSubsetOf(Object actual, Object values, Iterable<?> unexpected,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldBeSubsetOf(actual, values, unexpected, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeSubsetOf}</code>.
   * @param actual the actual set
   * @param values the expected superset
   * @param unexpected the unexpected value
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeSubsetOf(Object actual, Object values, Iterable<?> unexpected) {
    return new ShouldBeSubsetOf(actual, values, unexpected, StandardComparisonStrategy.instance());
  }

  private ShouldBeSubsetOf(Object actual, Object values, Iterable<?> unexpected, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting %s:%n <%s>%nto be subset of%n <%s>%nbut found these extra elements:%n <%s>", comparisonStrategy, actual,
        values, unexpected);
  }
}
