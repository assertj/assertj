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

import java.util.Date;

import org.assertj.core.internal.*;


/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is in the future failed.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeInTheFuture extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeInTheFuture}</code>.
   * @param actual the actual value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInTheFuture(Date actual, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeInTheFuture(actual, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeInTheFuture}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInTheFuture(Date actual) {
    return new ShouldBeInTheFuture(actual, StandardComparisonStrategy.instance());
  }

  private ShouldBeInTheFuture(Date actual, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto be in the future%s but was not.", actual, comparisonStrategy);
  }
}
