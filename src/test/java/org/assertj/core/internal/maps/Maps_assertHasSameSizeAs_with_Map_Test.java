/*
 * Created on Aug 20, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.internal.maps;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldHaveSameSizeAs.*;
import static org.assertj.core.test.Maps.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.*;
import static org.assertj.core.util.FailureMessages.*;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertHasSameSizeAs(org.assertj.core.api.AssertionInfo, java.util.Map, java.util.Map)}</code>
 * .
 *
 * @author Adam Ruka
 */
public class Maps_assertHasSameSizeAs_with_Map_Test extends MapsBaseTest {
  @SuppressWarnings("unchecked")
  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = (Map<String, String>) mapOf(entry("name", "Yoda"), entry("job", "Yedi Master"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertHasSameSizeAs(someInfo(), null, mapOf(entry("name", "Solo")));
  }

  @Test
  public void should_fail_if_other_is_null() {
    thrown.expectNullPointerException("The Map to compare actual size with should not be null");
    maps.assertHasSameSizeAs(someInfo(), actual, (Map<?, ?>) null);
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_size_of_other() {
    AssertionInfo info = someInfo();
    Map<?, ?> other = mapOf(entry("name", "Solo"));
    try {
      maps.assertHasSameSizeAs(info, actual, other);
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError e) {
      assertThat(e).hasMessage(
          shouldHaveSameSizeAs(actual, actual.size(), other.size()).create(null, info.representation()));
    }
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    maps.assertHasSameSizeAs(someInfo(), actual, mapOf(entry("name", "Solo"), entry("job", "Smuggler")));
  }
}
