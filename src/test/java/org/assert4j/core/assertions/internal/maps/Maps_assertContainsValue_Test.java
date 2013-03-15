/*
 * Created on Jun 3, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.assert4j.core.assertions.internal.maps;

import static org.assert4j.core.assertions.data.MapEntry.entry;
import static org.assert4j.core.assertions.error.ShouldContainValue.shouldContainValue;
import static org.assert4j.core.assertions.test.Maps.mapOf;
import static org.assert4j.core.assertions.test.TestData.someInfo;
import static org.assert4j.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assert4j.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assert4j.core.assertions.core.AssertionInfo;
import org.assert4j.core.assertions.internal.Maps;
import org.assert4j.core.assertions.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link Maps#assertContainsValue(AssertionInfo, Map, Object)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Maps_assertContainsValue_Test extends MapsBaseTest {

  @SuppressWarnings("unchecked")
  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = (Map<String, String>) mapOf(entry("name", "Yoda"), entry("color", "green"), entry(null, null));
  }

  @Test
  public void should_pass_if_actual_contains_given_value() {
    maps.assertContainsValue(someInfo(), actual, "Yoda");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsValue(someInfo(), null, "Yoda");
  }

  @Test
  public void should_success_if_value_is_null() {
    maps.assertContainsValue(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value() {
    AssertionInfo info = someInfo();
    String value = "veryOld";
    try {
      maps.assertContainsValue(info, actual, value);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainValue(actual, value));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
