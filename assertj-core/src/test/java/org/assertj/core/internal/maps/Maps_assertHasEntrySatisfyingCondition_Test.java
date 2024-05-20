/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.maps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.condition.Not.not;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.regex.Pattern;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Maps#assertHasEntrySatisfying(AssertionInfo, Map, Object, Condition)}</code>.
 *
 * @author Valeriy Vyrva
 */
class Maps_assertHasEntrySatisfyingCondition_Test extends MapsBaseTest {

  private static final Pattern IS_DIGITS = Pattern.compile("^\\d+$");

  private Condition<String> isDigits;
  private Condition<String> isNotDigits;
  private Condition<Object> isNull;
  private Condition<Object> nonNull;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = mapOf(entry("name", "Yoda"), entry("color", "green"), entry(null, null));
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
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertHasEntrySatisfying(someInfo(), null, 8, isDigits))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_key() {
    AssertionInfo info = someInfo();
    String key = "id";

    Throwable error = catchThrowable(() -> maps.assertHasEntrySatisfying(info, actual, key, isDigits));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainKeys(actual, newLinkedHashSet(key)));
  }

  @Test
  void should_fail_if_actual_contains_key_with_value_not_matching_condition() {
    AssertionInfo info = someInfo();
    String key = "name";

    Throwable error = catchThrowable(() -> maps.assertHasEntrySatisfying(info, actual, key, isDigits));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsShouldBe(actual, actual.get(key), isDigits));
  }

  @Test
  void should_fail_if_actual_contains_null_key_with_value_not_matching_condition() {
    AssertionInfo info = someInfo();
    String key = null;

    Throwable error = catchThrowable(() -> maps.assertHasEntrySatisfying(info, actual, key, nonNull));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsShouldBe(actual, actual.get(key), nonNull));
  }

  @Test
  void should_pass_if_actual_contains_null_key_with_value_match_condition() {
    AssertionInfo info = someInfo();
    maps.assertHasEntrySatisfying(info, actual, null, isNull);
  }

  @Test
  void should_pass_if_actual_contains_key_with_value_match_condition() {
    AssertionInfo info = someInfo();
    String key = "name";
    maps.assertHasEntrySatisfying(info, actual, key, isNotDigits);
  }
}
