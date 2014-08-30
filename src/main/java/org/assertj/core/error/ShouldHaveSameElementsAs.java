/*
 * Created on August 21, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.util.Strings;

/**
 * Creates an error message indicating that two {@code Iterables} did not contain the same elements.
 * 
 * @author Adam Ruka
 */
public final class ShouldHaveSameElementsAs extends BasicErrorMessageFactory {
  /**
   * Creates a new instance of {@link ShouldHaveSameElementsAs}.
   *
   * @param actual the actual {@link Iterable} being asserted
   * @param expected the {@link Iterable} expected to contain the same elements
   * @param comparisonStrategy the {@link org.assertj.core.internal.ComparisonStrategy} used
   *                           to compare the elements of the {@code Iterables}
   * @return the newly created {@code ErrorMessageFactory} instance
   */
  public static ErrorMessageFactory shouldHaveSameElementsAs(Iterable<?> actual, Iterable<?> expected,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldHaveSameElementsAs(actual, expected, comparisonStrategy);
  }

  private ShouldHaveSameElementsAs(Iterable<?> actual, Iterable<?> expected, ComparisonStrategy comparisonStrategy) {
    super("\nActual and expected do not have the same elements, actual was:\n <%s>\nwhereas expected was:\n <%s>\n%s",
        Strings.join(actual).with(", "), Strings.join(expected).with(", "), comparisonStrategy);
  }
}
