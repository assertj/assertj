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

import org.assertj.core.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies that a group of elements ends with a given value or
 * sequence of values failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldEndWith extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldEndWith}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the value or sequence of values that {@code actual} is expected to end with.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldEndWith(Object actual, Object expected, ComparisonStrategy comparisonStrategy) {
    return new ShouldEndWith(actual, expected, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldEndWith}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the value or sequence of values that {@code actual} is expected to end with.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldEndWith(Object actual, Object expected) {
    return new ShouldEndWith(actual, expected, StandardComparisonStrategy.instance());
  }

  private ShouldEndWith(Object actual, Object expected, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto end with:%n <%s>%n%s", actual, expected, comparisonStrategy);
  }
}
