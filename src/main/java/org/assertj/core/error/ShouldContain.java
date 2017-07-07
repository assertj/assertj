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
 * Creates an error message indicating that an assertion that verifies a group of elements contains a given set of values failed.
 * A group of elements can be a collection, an array or a {@code String}.<br>
 * It also mention the {@link ComparisonStrategy} used.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldContain extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContain}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be in {@code actual}.
   * @param notFound the values in {@code expected} not found in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(Object actual, Object expected, Object notFound,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldContain(actual, expected, notFound, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContain}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be in {@code actual}.
   * @param notFound the values in {@code expected} not found in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(Object actual, Object expected, Object notFound) {
    return shouldContain(actual, expected, notFound, StandardComparisonStrategy.instance());
  }

  private ShouldContain(Object actual, Object expected, Object notFound, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto contain:%n <%s>%nbut could not find:%n <%s>%n%s", actual, expected, notFound, comparisonStrategy);
  }

}
