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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.MatcherShouldHaveGroup.shouldHaveGroup;
import static org.assertj.core.error.MatcherShouldMatch.shouldMatch;

import java.util.function.Supplier;
import java.util.regex.Matcher;

import org.assertj.core.internal.Failures;

/**
 * Assertions for {@link java.util.regex.Matcher}
 *
 * @author Jiashu Zhang
 */
public abstract class AbstractMatcherAssert<SELF extends AbstractMatcherAssert<SELF>> extends
    AbstractAssert<SELF, Matcher> {

  protected AbstractMatcherAssert(Matcher actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the Matcher matches.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * Pattern pattern = Pattern.compile("a*");
   * Matcher matcher = pattern.matcher("aaa");
   * assertThat(matcher).matches();
   *
   * // Assertion fails:
   * Pattern pattern = Pattern.compile("a*");
   * Matcher matcher = pattern.matcher("abc");
   * assertThat(matcher).matches();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if actual does not match.
   * @throws AssertionError if actual is null.
   * @since 3.23.0
   */
  public SELF matches() {
    isNotNull();
    if (!actual.matches()) {
      throw Failures.instance().failure(info, shouldMatch(actual));
    }
    return myself;
  }

  /**
   * Verifies that the {@link Matcher} under test matches, and has the specified group, and allows to perform assertions on the input subsequence captured by the given group
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds
   * Matcher matcher = Pattern.compile("Assert(.)").matcher("AssertJ");
   * assertThat(matcher).group(1).isEqualTo("J");
   *
   * // Assertions fail
   * Matcher matcher = Pattern.compile("Assert(.)").matcher("AssertJ");
   * assertThat(matcher).group(1).isEqualTo("X");
   * assertThat(matcher).group(2).isEqualTo("J");
   * </code></pre>
   *
   * @param group The index of a capturing group
   * @return a new {@link Assert} instance for assertions chaining on the captured subsequence
   * @throws AssertionError if actual {@link Matcher} does not match or is null, or if the capture group does not exist
   */
  public AbstractStringAssert<?> group(int group) {
    isNotNull();
    matches();
    return assertThat(extractGroup(() -> actual.group(group), group));
  }

  /**
   * Verifies that the {@link Matcher} under test matches, and has the specified group, and allows to perform assertions on the input subsequence captured by the given group
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds
   * Matcher matcher = Pattern.compile("Assert(?&lt;g1&gt;.)").matcher("AssertJ");
   * assertThat(matcher).group("g1").isEqualTo("J");
   *
   * // Assertions fail
   * Matcher matcher = Pattern.compile("Assert(?&lt;g1&gt;.)").matcher("AssertJ");
   * assertThat(matcher).group("g1").isEqualTo("X");
   * assertThat(matcher).group("g2").isEqualTo("J");
   * </code></pre>
   *
   * @param group The name of a named capturing group
   * @return a new {@link Assert} instance for assertions chaining on the captured subsequence
   * @throws AssertionError if actual {@link Matcher} does not match or is null, or if the capture group does not exist
   */
  public AbstractStringAssert<?> group(String group) {
    isNotNull();
    matches();
    return assertThat(extractGroup(() -> actual.group(group), group));
  }

  /**
   * Verifies that the {@link Matcher} under test matches, and allows to perform assertions on the list of input subsequences captured the regex groups
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds
   * Matcher matcher = Pattern.compile("(.+) the (.+)").matcher("Mack the Knife");
   * assertThat(matcher).groups().containsExactly("Mack", "Knife");
   *
   * // Assertions fail
   * Matcher matcher = Pattern.compile("No re(.+)ds").matcher("No refunds");
   * assertThat(matcher).groups().doesNotContain("fun");
   * </code></pre>
   *
   * @return a new {@link Assert} instance for assertions chaining on the list of capture subsequences
   * @throws AssertionError if actual {@link Matcher} does not match or is null
   */
  public ListAssert<String> groups() {
    isNotNull();
    matches();
    return assertThat(rangeClosed(1, actual.groupCount()).mapToObj(actual::group).collect(toList()));
  }

  private String extractGroup(Supplier<String> extractor, Object groupIdentifier) {
    try {
      return extractor.get();
    } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
      throw Failures.instance().failure(info, shouldHaveGroup(actual, groupIdentifier));
    }
  }
}
