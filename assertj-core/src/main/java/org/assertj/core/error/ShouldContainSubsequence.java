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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Arrays.sizeOf;

import java.lang.reflect.Array;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.IterableUtil;

/**
 * Creates an error message indicating that an assertion that verifies that a group of elements contains a subsequence
 * of values failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Marcin Mikosik
 */
public class ShouldContainSubsequence extends BasicErrorMessageFactory {

  public static ShouldContainSubsequence actualDoesNotHaveEnoughElementsToContainSubsequence(Object actual, Object subsequence) {
    return new ShouldContainSubsequence(actual, subsequence);
  }

  private ShouldContainSubsequence(Object actual, Object subsequence) {
    super("%nExpecting actual to contain the specified subsequence but actual does not have enough elements to contain it, actual size is %s when subsequence size is %s%nactual:%n  %s%nsubsequence:%n  %s",
          sizeOfArrayOrIterable(actual), sizeOf(subsequence), actual, subsequence);
  }

  public static ShouldContainSubsequence actualDoesNotHaveEnoughElementsLeftToContainSubsequence(Object actual,
                                                                                                 Object subsequence,
                                                                                                 int actualIndex,
                                                                                                 int subsequenceIndex) {
    return new ShouldContainSubsequence(actual, subsequence, actualIndex, subsequenceIndex);
  }

  private ShouldContainSubsequence(Object actual, Object subsequence, int actualIndex, int subsequenceIndex) {
    super("%nExpecting actual to contain the specified subsequence but actual does not have enough elements left to compare after reaching element %s out of %s with %s subsequence element(s) still to find."
          + "%nactual:%n  %s%nsubsequence:%n  %s",
          actualIndex + 1, sizeOfArrayOrIterable(actual), sizeOf(subsequence) - subsequenceIndex, actual, subsequence);
  }

  private static Object sizeOfArrayOrIterable(Object actual) {
    return isArray(actual) ? Arrays.sizeOf(actual) : IterableUtil.sizeOf((Iterable<?>) actual);
  }

  /**
   * Creates a new <code>{@link ShouldContainSubsequence}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param subsequence the subsequence of values expected to be in {@code actual}.
   * @param subsequenceIndex the index of the first token in {@code subsequence} that was not found in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ShouldContainSubsequence shouldContainSubsequence(Object actual, Object subsequence, int subsequenceIndex,
                                                                  ComparisonStrategy comparisonStrategy) {
    return new ShouldContainSubsequence(actual, subsequence, subsequenceIndex, comparisonStrategy);
  }

  private ShouldContainSubsequence(Object actual, Object subsequence, int subsequenceIndex,
                                   ComparisonStrategy comparisonStrategy) {
    // Failed to find token at subsequence index %s in actual:%n %s
    super("%nExpecting actual to contain the specified subsequence but failed to find the element at subsequence index %s in actual"
          + describeComparisonStrategy(comparisonStrategy) + ":%n"
          + "subsequence element not found in actual:%n"
          + "  %s%n"
          + "actual:%n"
          + "  %s%n"
          + "subsequence:%n  %s",
          subsequenceIndex, Array.get(subsequence, subsequenceIndex), actual, subsequence);
  }

  private static String describeComparisonStrategy(ComparisonStrategy comparisonStrategy) {
    return comparisonStrategy == StandardComparisonStrategy.instance() ? ""
        : " when comparing elements using " + comparisonStrategy;
  }
}
