/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a value is less than another one failed.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeLess extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeLess}</code>.
   * @param <T> guarantees that the values used in this factory have the same type.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeLess(T actual, T other) {
    return new ShouldBeLess(actual, other, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldBeLess}</code>.
   * @param <T> guarantees that the values used in this factory have the same type.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeLess(T actual, T other, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeLess(actual, other, comparisonStrategy);
  }

  private <T> ShouldBeLess(T actual, T other, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be less than:%n  %s %s", actual, other, comparisonStrategy);
  }

}
