/*
 * Created on Oct 18, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains only a given set of values and
 * nothing else failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContainOnly extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldContainOnly}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnly(Object actual, Object expected, Object notFound, Object notExpected,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldContainOnly(actual, expected, notFound, notExpected, comparisonStrategy);
  }

  /**
   * Creates a new </code>{@link ShouldContainOnly}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnly(Object actual, Object expected, Object notFound, Object notExpected) {
    return new ShouldContainOnly(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance());
  }

  private ShouldContainOnly(Object actual, Object expected, Object notFound, Object notExpected,
      ComparisonStrategy comparisonStrategy) {
    super("expecting:\n<%s>\n to contain only:\n<%s>\n elements not found:\n<%s>\n and elements not expected:\n<%s>\n%s", actual,
        expected, notFound, notExpected, comparisonStrategy);
  }

}
