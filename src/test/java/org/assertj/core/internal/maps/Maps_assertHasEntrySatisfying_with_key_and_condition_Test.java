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
package org.assertj.core.internal.maps;

import static org.assertj.core.condition.Not.not;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Maps#assertHasEntrySatisfying(AssertionInfo, Map, Object, Condition)}</code>.
 *
 * @author Valeriy Vyrva
 */
public class Maps_assertHasEntrySatisfying_with_key_and_condition_Test extends MapsBaseTest {

  private static final Pattern IS_DIGITS = Pattern.compile("^\\d+$");

  private Condition<String> isDigits;
  private Condition<String> isNotDigits;
  private Condition<Object> isNull;
  private Condition<Object> nonNull;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = mapOf(entry("name", "Yoda"), entry("color", "green"), entry((String) null, (String) null));
    isDigits = new Condition<String>("isDigits") {
      @Override
      public boolean matches(String value) {
        return IS_DIGITS.matcher(value).matches();
      }
    };
    isNotDigits = not(isDigits);
    isNull = new Condition<Object>("isNull") {
      @Override
      public boolean matches(Object value) {
        return value == null;
      }
    };
    nonNull = not(isNull);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertHasEntrySatisfying(someInfo(), null, 8, isDigits);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_key() {
    AssertionInfo info = someInfo();
    String key = "id";
    try {
      maps.assertHasEntrySatisfying(info, actual, key, isDigits);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainKeys(actual, newLinkedHashSet(key)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_key_with_value_not_matching_condition() {
    AssertionInfo info = someInfo();
    String key = "name";
    try {
      maps.assertHasEntrySatisfying(info, actual, key, isDigits);
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsShouldBe(actual, actual.get(key), isDigits));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_null_key_with_value_not_matching_condition() {
    AssertionInfo info = someInfo();
    String key = null;
    try {
      maps.assertHasEntrySatisfying(info, actual, key, nonNull);
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsShouldBe(actual, actual.get(key), nonNull));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_null_key_with_value_match_condition() {
    AssertionInfo info = someInfo();
    maps.assertHasEntrySatisfying(info, actual, null, isNull);
  }

  @Test
  public void should_pass_if_actual_contains_key_with_value_match_condition() {
    AssertionInfo info = someInfo();
    String key = "name";
    maps.assertHasEntrySatisfying(info, actual, key, isNotDigits);
  }
}
