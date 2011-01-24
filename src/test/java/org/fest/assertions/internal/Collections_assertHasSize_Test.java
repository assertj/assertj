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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertHasSize(AssertionInfo, Collection, int)}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertHasSize_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Collections collections;

  @Before public void setUp() {
    failures = spy(new Failures());
    collections = new Collections();
    collections.failures = failures;
  }

  @Test public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    collections.assertHasSize(someInfo(), list("Luke", "Yoda"), 2);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertHasSize(someInfo(), null, 8);
  }

  @Test public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    Collection<String> actual = list("Yoda");
    try {
      collections.assertHasSize(info, actual, 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSize(actual, actual.size(), 8));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
