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

import java.util.regex.Matcher;

/**
 * Build error message when an {@link java.util.regex.Matcher} should match.
 *
 * @author Jiashu Zhang
 */
public class MatcherShouldMatch extends BasicErrorMessageFactory {
  private MatcherShouldMatch(Matcher matcher) {
    super("%nExpecting %s to match.", matcher);
  }

  /**
   * Indicates that the provided {@link java.util.regex.Matcher} should match.
   *
   * @param matcher the actual {@link Matcher} to test.
   * @return an error message factory.
   */
  public static MatcherShouldMatch shouldMatch(Matcher matcher) {
    return new MatcherShouldMatch(matcher);
  }
}
