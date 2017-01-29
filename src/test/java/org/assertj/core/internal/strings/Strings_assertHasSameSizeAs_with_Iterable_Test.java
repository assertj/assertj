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
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Strings#assertHasSameSizeAs(AssertionInfo, CharSequence, Iterable)}</code>.
 * 
 * @author Nicolas François
 */
public class Strings_assertHasSameSizeAs_with_Iterable_Test extends StringsBaseTest {

  private static String actual = "Han";

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertHasSameSizeAs(someInfo(), null, newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    List<String> other = newArrayList("Solo", "Leia");

    thrown.expectAssertionError(shouldHaveSameSizeAs(actual, actual.length(), other.size())
        .create(null, info.representation()));

    strings.assertHasSameSizeAs(info, actual, other);
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    strings.assertHasSameSizeAs(someInfo(), actual, newArrayList("Solo", "Leia", "Yoda"));
  }
}
