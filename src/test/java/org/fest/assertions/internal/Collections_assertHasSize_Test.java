/*
 * Created on Sep 26, 2010
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

import static org.fest.assertions.error.WhenSizeNotEqualErrorFactory.errorWhenSizeNotEqual;
import static org.fest.assertions.test.Exceptions.assertionFailingOnPurpose;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertHasSize(AssertionInfo, Collection, int)}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertHasSize_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Collections collections;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = mock(Failures.class);
    collections = new Collections(failures);
  }

  @Test public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    collections.assertHasSize(info, list("Luke", "Yoda"), 2);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertHasSize(info, null, 8);
  }

  @Test public void should_fail_if_actual_has_elements() {
    AssertionError expectedError = assertionFailingOnPurpose();
    Collection<String> actual = list("Yoda");
    int expectedSize = 8;
    when(failures.failure(info, errorWhenSizeNotEqual(actual, actual.size(), expectedSize))).thenReturn(expectedError);
    thrown.expect(expectedError);
    collections.assertHasSize(info, actual, expectedSize);
  }
}
