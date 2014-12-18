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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.internal.maps;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainValues.shouldContainValues;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.internal.Maps#assertContainsValues(org.assertj.core.api.AssertionInfo, java.util.Map, Object[])}</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 * @author Alexander Bischof
 */
public class Maps_assertContainsValues_Test extends MapsBaseTest {

  @SuppressWarnings("unchecked")
  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = (Map<String, String>) mapOf(entry("name", "Yoda"), entry("type", "Jedi"), entry("color", "green"), entry(null, null));
  }

  @Test
  public void should_pass_if_actual_contains_given_values() {
    maps.assertContainsValues(someInfo(), actual, "Yoda", "Jedi");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsValues(someInfo(), null, "Yoda");
  }

  @Test
  public void should_success_if_values_is_null() {
    maps.assertContainsValues(someInfo(), actual, null);
  }

  @Test
  public void should_success_if_values_contains_null() {
	maps.assertContainsValues(someInfo(), actual, "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    String value = "veryOld";
    String value2 = "veryOld2";
    try {
      maps.assertContainsValues(info, actual, value, value2);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainValues(actual, newHashSet(Arrays.asList(value, value2))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
