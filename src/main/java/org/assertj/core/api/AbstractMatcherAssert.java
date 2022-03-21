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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.MatcherShouldMatch.shouldMatch;

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
   * <pre><code class='java'>
   * // Assertion will pass
   * Pattern p = Pattern.compile("a*");
   * String str = "aaa";
   * Matcher matcher = p.matcher(str);
   * assertThat(matcher).matches();
   * // Assertion will fail :
   * Pattern p = Pattern.compile("a*");
   * String str = "abc";
   * Matcher matcher = p.matcher(str);
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
}
