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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import static org.assertj.core.error.GroupTypeDescription.getGroupTypeDescription;
import static org.assertj.core.util.Strings.escapePercent;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains a given set of values failed.
 * A group of elements can be a collection, an array or a {@code String}.<br>
 * It also mention the {@link ComparisonStrategy} used.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldContain extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContain}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be in {@code actual}.
   * @param notFound the values in {@code expected} not found in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(Object actual, Object expected, Object notFound,
                                                  ComparisonStrategy comparisonStrategy) {
    GroupTypeDescription groupTypeDescription = getGroupTypeDescription(actual);
    return new ShouldContain(actual, expected, notFound, comparisonStrategy, groupTypeDescription);
  }

  /**
   * Creates a new <code>{@link ShouldContain}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be in {@code actual}.
   * @param notFound the values in {@code expected} not found in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(Object actual, Object expected, Object notFound) {
    return shouldContain(actual, expected, notFound, StandardComparisonStrategy.instance());
  }

  public static ErrorMessageFactory directoryShouldContain(File actual, List<String> directoryContent, String filterDescription) {
    return new ShouldContain(actual, directoryContent, filterDescription);
  }

  public static ErrorMessageFactory directoryShouldContain(Path actual, List<String> directoryContent, String filterDescription) {
    return new ShouldContain(actual, directoryContent, filterDescription);
  }

  private ShouldContain(Object actual, Object expected, Object notFound, ComparisonStrategy comparisonStrategy,
                        GroupTypeDescription groupTypeDescription) {
    super("%nExpecting " + groupTypeDescription.getGroupTypeName()
          + ":%n <%s>%nto contain:%n <%s>%nbut could not find the following " + groupTypeDescription.getElementTypeName()
          + ":%n <%s>%n%s", actual, expected, notFound,
          comparisonStrategy);
  }

  private ShouldContain(Object actual, List<String> directoryContent, String filterDescription) {
    // not passing directoryContent and filterDescription as parameter to avoid AssertJ default String formatting
    super("%nExpecting directory:%n" +
          "  <%s>%n" +
          "to contain at least one file matching " + escapePercent(filterDescription) + " but there was none.%n" +
          "The directory content was:%n  " + escapePercent(directoryContent.toString()),
          actual);
  }

}
