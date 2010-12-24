/*
 * Created on Dec 21, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.data.MapEntry.entry;
import static org.fest.assertions.error.Contains.contains;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.MapFactory.map;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.set;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.MapEntry;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Maps#assertDoesNotContain(AssertionInfo, Map, MapEntry[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Maps_assertDoesNotContain_Test {

  private static Map<?, ?> actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Maps maps;

  @BeforeClass public static void setUpOnce() {
    actual = map(entry("name", "Yoda"), entry("color", "green"));
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    maps = new Maps(failures);
  }

  @Test public void should_pass_if_actual_does_not_contain_given_values() {
    maps.assertDoesNotContain(someInfo(), actual, array(entry("job", "Jedi")));
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertDoesNotContain(someInfo(), actual, new MapEntry[0]);
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertDoesNotContain(someInfo(), actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    maps.assertDoesNotContain(someInfo(), null, array(entry("job", "Jedi")));
  }

  @Test public void should_fail_if_actual_contains_given_values() {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda"), entry("job", "Jedi") };
    try {
      maps.assertDoesNotContain(info, actual, expected);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, contains(actual, expected, set(entry("name", "Yoda"))));
  }
}
