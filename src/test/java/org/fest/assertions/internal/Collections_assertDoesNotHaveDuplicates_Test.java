/*
 * Created on Oct 12, 2010
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

import static java.util.Collections.emptyList;
import static org.fest.assertions.error.ErrorWhenGroupHasDuplicates.errorWhenHavingDuplicates;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.util.Collections.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.core.*;
import org.fest.assertions.error.AssertionErrorFactory;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertDoesHaveDuplicates(AssertionInfo, Collection)}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertDoesNotHaveDuplicates_Test {

  private static WritableAssertionInfo info;
  private static List<String> actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Collections collections;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = list("Luke", "Yoda", "Leia");
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    collections = new Collections(failures);
  }

  @Test public void should_pass_if_actual_does_not_have_duplicates() {
    collections.assertDoesHaveDuplicates(info, actual);
  }

  @Test public void should_pass_if_actual_is_empty() {
    collections.assertDoesHaveDuplicates(info, emptyList());
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertDoesHaveDuplicates(info, null);
  }

  @Test public void should_fail_if_actual_contains_duplicates() {
    Collection<String> duplicates = set("Luke", "Yoda");
    actual.addAll(duplicates);
    thrown.expectAssertionErrorButNotFromMockito();
    collections.assertDoesHaveDuplicates(info, actual);
    AssertionErrorFactory errorFactory = errorWhenHavingDuplicates(actual, duplicates);
    verify(failures).failure(info, errorFactory);
  }
}
