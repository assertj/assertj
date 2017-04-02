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
 * Creates an error message indicating that an assertion that verifies that a group of elements does not contain a
 * sequence of values failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Chris Arnott
 */
public class ShouldNotContainSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotContainSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param index the index where the sequence was found in actual.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSequence(Object actual, Object sequence, int index, ComparisonStrategy comparisonStrategy) {
    return new ShouldNotContainSequence(actual, sequence, index, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotContainSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param index the index where the sequence was found.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSequence(Object actual, Object sequence, int index) {
    return new ShouldNotContainSequence(actual, sequence, index, StandardComparisonStrategy.instance());
  }

  private ShouldNotContainSequence(Object actual, Object sequence, int index, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto not contain sequence:%n <%s>%nbut was found at index %s%n%s", actual, sequence, index, comparisonStrategy);
  }

}
