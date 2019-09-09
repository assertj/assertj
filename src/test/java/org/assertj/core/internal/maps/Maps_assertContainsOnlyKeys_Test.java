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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertContainsOnlyKeys(org.assertj.core.api.AssertionInfo, java.util.Map, java.lang.Object...)}</code>
 * .
 *
 * @author Christopher Arnott
 */
public class Maps_assertContainsOnlyKeys_Test extends MapsBaseTest {

  private static final String ARRAY_OF_KEYS = "array of keys";

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertContainsOnlyKeys(someInfo(), null,
                                                                                                 "name"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_given_keys_array_is_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertContainsOnlyKeys(someInfo(), actual, (String[]) null))
                                    .withMessage(keysToLookForIsNull(ARRAY_OF_KEYS));
  }

  @Test
  public void should_fail_if_given_keys_array_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> maps.assertContainsOnlyKeys(someInfo(), actual, emptyKeys()))
                                        .withMessage(keysToLookForIsEmpty(ARRAY_OF_KEYS));
  }

  @Test
  public void should_pass_if_actual_and_given_keys_are_empty() {
    maps.assertContainsOnlyKeys(someInfo(), emptyMap(), (Object[]) emptyKeys());
  }

  @Test
  public void should_pass_if_actual_contains_only_expected_keys() {
    maps.assertContainsOnlyKeys(someInfo(), actual, "color", "name");
    maps.assertContainsOnlyKeys(someInfo(), actual, "name", "color");
  }

  @Test
  public void should_fail_if_actual_contains_an_unexpected_key() {
    //GIVEN
    AssertionInfo info = someInfo();
    String[] expectedKeys = {"name"};
    //WHEN
    expectAssertionError(() -> maps.assertContainsOnlyKeys(info, actual, expectedKeys));
    //THEN
    verify(failures).failure(info, shouldContainOnlyKeys(actual, expectedKeys, emptySet(),
      newHashSet("color")));
  }

  @Test
  public void should_fail_if_actual_does_not_contains_all_expected_keys() {
    //GIVEN
    AssertionInfo info = someInfo();
    String[] expectedKeys = {"name", "color"};
    Map<String, String> underTest = mapOf(entry("name", "Yoda"));
    //WHEN
    expectAssertionError(() -> maps.assertContainsOnlyKeys(info, underTest, expectedKeys));
    //THEN
    verify(failures).failure(info, shouldContainOnlyKeys(underTest, expectedKeys, newHashSet("color"),
      emptySet()));
  }

  @Test
  public void should_fail_if_actual_does_not_contains_all_expected_keys_and_contains_unexpected_one() {
    //GIVEN
    AssertionInfo info = someInfo();
    String[] expectedKeys = {"name", "color"};
    Map<String, String> underTest = mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    //WHEN
    expectAssertionError(() -> maps.assertContainsOnlyKeys(info, underTest, expectedKeys));
    //THEN
    verify(failures).failure(info, shouldContainOnlyKeys(underTest, expectedKeys, newHashSet("color"), newHashSet("job")));
  }

  private static HashSet<String> newHashSet(String entry) {
    HashSet<String> notExpected = new HashSet<>();
    notExpected.add(entry);
    return notExpected;
  }
}
