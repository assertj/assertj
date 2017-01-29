/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.error.ShouldHaveMessageMatchingRegex.shouldHaveMessageMatchingRegex;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link ThrowableAssert#hasMessageMatching(String)}</code>.
 * 
 * @author Libor Ondrusek
 */
public class Throwables_assertHasMessageMatching_Test extends ThrowablesBaseTest {

  public static final String REGEX = "Given id='\\d{2,4}' not exists";

  @Test
  public void should_pass_if_throwable_message_matches_given_regex() {
    actual = new RuntimeException("Given id='259' not exists");
    throwables.assertHasMessageMatching(someInfo(), actual, REGEX);
  }

  @Test
  public void should_pass_if_throwable_message_is_empty_and_regex_is_too() {
    actual = new RuntimeException("");
    throwables.assertHasMessageMatching(someInfo(), actual, "");
  }

  @Test
  public void should_fail_if_throwable_message_does_not_match_given_regex() {
    AssertionInfo info = someInfo();
    try {
      throwables.assertHasMessageMatching(info, actual, REGEX);
      fail("AssertionError expected");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveMessageMatchingRegex(actual, REGEX));
    }
  }

  @Test
  public void should_fail_if_given_regex_is_null() {
    thrown.expectNullPointerException("regex must not be null");
    throwables.assertHasMessageMatching(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_throwable_does_not_have_a_message() {
    actual = new RuntimeException();
    AssertionInfo info = someInfo();
    try {
      throwables.assertHasMessageMatching(info, actual, REGEX);
      fail("AssertionError expected");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveMessageMatchingRegex(actual, REGEX));
    }
  }

}
