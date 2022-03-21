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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.MatcherShouldMatch.shouldMatch;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.assertj.core.internal.TestDescription;
import org.junit.Test;

public class MatcherShouldMatch_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    Pattern pattern = Pattern.compile("a*");
    String expectedValue = "abc";
    Matcher actual = pattern.matcher(expectedValue);
    // WHEN
    String message = shouldMatch(actual).create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting %s to match.", actual));
  }
}
