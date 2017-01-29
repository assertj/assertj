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

import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Maps#assertDoesNotContainKeys(org.assertj.core.api.AssertionInfo, java.util.Map, Object...)}</code>.
 *
 * @author dorzey
 */
public class Maps_assertDoesNotContainKeys_Test extends MapsBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual.put(null, null);
  }

  @Test
  public void should_pass_if_actual_does_not_contain_given_keys() {
    maps.assertDoesNotContainKeys(someInfo(), actual, "age");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertDoesNotContainKeys(someInfo(), null, "name", "color");
  }

  @Test
  public void should_pass_if_key_is_null() {
    maps.assertDoesNotContainKeys(someInfo(), actual, (String) null);
  }

  @Test
  public void should_fail_if_actual_contains_key() {
    AssertionInfo info = someInfo();
    String key = "name";
    try {
      maps.assertDoesNotContainKeys(info, actual, key);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainKeys(actual, newLinkedHashSet(key)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_keys() {
    AssertionInfo info = someInfo();
    String key1 = "name";
    String key2 = "color";
    try {
      maps.assertDoesNotContainKeys(info, actual, key1, key2);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainKeys(actual, newLinkedHashSet(key1, key2)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
