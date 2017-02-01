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
 * Creates an error message indicating that an assertion that verifies that a group of elements contains a sequence of values
 * failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldContainSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(Object actual, Object sequence, ComparisonStrategy comparisonStrategy) {
    return new ShouldContainSequence(actual, sequence, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSequence(Object actual, Object sequence) {
    return new ShouldContainSequence(actual, sequence, StandardComparisonStrategy.instance());
  }

  private ShouldContainSequence(Object actual, Object sequence, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto contain sequence:%n <%s>%n%s", actual, sequence, comparisonStrategy);
  }

}
