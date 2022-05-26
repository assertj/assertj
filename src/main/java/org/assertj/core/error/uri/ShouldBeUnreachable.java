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
package org.assertj.core.error.uri;

import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

//CS304 Issue link: https://github.com/assertj/assertj-core/issues/2196
public class ShouldBeUnreachable extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_UNREACHABLE = "%nExpecting actual:%n  <%s>%nbe unreachable but it's not";

  /** .
   * Verifies that the actual {@code URI} is unreachable.
   * @param actual the URI we want to test its unreachability
   * @throws AssertionError if the actual URI is reachable.
   */
  public static ErrorMessageFactory shouldBeUnreachable(URI actual) {
    return new ShouldBeUnreachable(actual);
  }

  private ShouldBeUnreachable(URI actual) {
    super(SHOULD_BE_UNREACHABLE, actual);
  }

  /** .
   * Verifies that the actual {@code URL} is unreachable.
   * <p>
   * Examples:
   * <pre><code class='java'> // These assertions succeed:
   * assertThat(new URL("http://www.baidu")).isUnreachable();
   * assertThat(new URL("http://www.hltv")).isUnreachable();
   *
   * // this assertion fails:
   * assertThat(new URL("http://www.hltv.org")).isUnreachable();
   *
   * @param actual the URL we want to test its unreachability
   * @throws AssertionError if the actual URL is reachable.
   */
  public static ErrorMessageFactory shouldBeUnreachable(URL actual) {
    return new ShouldBeUnreachable(actual);
  }

  private ShouldBeReachable(URL actual) {
    super(SHOULD_BE_UNREACHABLE, actual);
  }
}
