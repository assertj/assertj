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

import org.assertj.core.data.Index;
import org.assertj.core.internal.*;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements does not contain a value at a given
 * index failed. A group of elements can be a collection, an array or a {@code String}.<br>
 * It also mention the {@link ComparisonStrategy} if the default one is not used.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldNotContainAtIndex extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotContainAtIndex}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected value expected to be in {@code actual}.
   * @param index the index of the expected value.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainAtIndex(Object actual, Object expected, Index index,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldNotContainAtIndex(actual, expected, index, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotContainAtIndex}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected value expected to be in {@code actual}.
   * @param index the index of the expected value.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainAtIndex(Object actual, Object expected, Index index) {
    return new ShouldNotContainAtIndex(actual, expected, index, StandardComparisonStrategy.instance());
  }

  private ShouldNotContainAtIndex(Object actual, Object expected, Index index, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nnot to contain:%n <%s>%nat index <%s>%n%s", actual, expected, index.value, comparisonStrategy);
  }
}
