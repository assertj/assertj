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
 * Creates an error message indicating that an assertion that verifies a group of elements is does not have duplicates failed. A
 * group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldNotHaveDuplicates extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotHaveDuplicates}</code>.
   * @param actual the actual value in the failed assertion.
   * @param duplicates the duplicate values found in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return an instance of {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotHaveDuplicates(Object actual, Object duplicates,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldNotHaveDuplicates(actual, duplicates, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotHaveDuplicates}</code>.
   * @param actual the actual value in the failed assertion.
   * @param duplicates the duplicate values found in {@code actual}.
   * @return an instance of {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotHaveDuplicates(Object actual, Object duplicates) {
    return new ShouldNotHaveDuplicates(actual, duplicates, StandardComparisonStrategy.instance());
  }

  private ShouldNotHaveDuplicates(Object actual, Object duplicates, ComparisonStrategy comparisonStrategy) {
    super("%nFound duplicate(s):%n <%s>%nin:%n <%s>%n%s", duplicates, actual, comparisonStrategy);
  }

}
