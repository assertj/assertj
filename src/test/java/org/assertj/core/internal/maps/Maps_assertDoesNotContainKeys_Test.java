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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Maps#assertDoesNotContainKeys(AssertionInfo, java.util.Map, Object[])}</code>.
 *
 * @author dorzey
 */
class Maps_assertDoesNotContainKeys_Test extends MapsBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual.put(null, null);
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_keys() {
    maps.assertDoesNotContainKeys(someInfo(), actual, array("age"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertDoesNotContainKeys(someInfo(), null,
                                                                                             array("name", "color")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_key_is_null() {
    maps.assertDoesNotContainKeys(someInfo(), actual, new String[] { null });
  }

  @Test
  void should_fail_if_actual_contains_key() {
    AssertionInfo info = someInfo();
    String key = "name";

    Throwable error = catchThrowable(() -> maps.assertDoesNotContainKeys(info, actual, new String[] { key }));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainKeys(actual, newLinkedHashSet(key)));
  }

  @Test
  void should_fail_if_actual_contains_keys() {
    AssertionInfo info = someInfo();
    String key1 = "name";
    String key2 = "color";

    Throwable error = catchThrowable(() -> maps.assertDoesNotContainKeys(info, actual, new String[] { key1, key2 }));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainKeys(actual, newLinkedHashSet(key1, key2)));
  }
}
