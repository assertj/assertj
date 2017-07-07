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
 * Creates an error message indicating that an assertion that verifies that a {@link Object} is before another one failed.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeBefore extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeBefore}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeBefore(Object actual, Object other, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeBefore(actual, other, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeBefore}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeBefore(Object actual, Object other) {
    return new ShouldBeBefore(actual, other, StandardComparisonStrategy.instance());
  }

  private ShouldBeBefore(Object actual, Object other, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n  <%s>%nto be strictly before:%n  <%s>%s", actual, other, comparisonStrategy);
  }
}
