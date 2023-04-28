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
package org.assertj.core.error;

import java.util.regex.Matcher;

/**
 * Build error message when an {@link Matcher} should have a named or numbered group.
 *
 * @author Harry Dent
 */
public class MatcherShouldHaveGroup extends BasicErrorMessageFactory {
  private MatcherShouldHaveGroup(Matcher matcher, Object groupIdentifier) {
    super("%nExpecting %s to have group %s.", matcher, groupIdentifier);
  }

  /**
   * Indicates that the provided {@link Matcher} was expected to have a named or numbered group.
   *
   * @param matcher the actual {@link Matcher} to test.
   * @return an error message factory.
   */
  public static MatcherShouldHaveGroup shouldHaveGroup(Matcher matcher, Object groupIdentifier) {
    return new MatcherShouldHaveGroup(matcher, groupIdentifier);
  }
}
