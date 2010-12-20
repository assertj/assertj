/*
 * Created on Dec 20, 2010
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

import static org.fest.assertions.error.HasDuplicates.hasDuplicates;
import static org.fest.assertions.test.DoubleArrayFactory.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.fest.util.Collections.set;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link DoubleArrays#assertDoesNotHaveDuplicates(AssertionInfo, double[])}</code>.
 *
 * @author Alex Ruiz
 */
public class DoubleArrays_assertDoesNotHaveDuplicates_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private double[] actual;
  private DoubleArrays collections;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    actual = array(6d, 8d);
    collections = new DoubleArrays(failures);
  }

  @Test public void should_pass_if_actual_does_not_have_duplicates() {
    collections.assertDoesNotHaveDuplicates(info, actual);
  }

  @Test public void should_pass_if_actual_is_empty() {
    collections.assertDoesNotHaveDuplicates(info, emptyArray());
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertDoesNotHaveDuplicates(info, null);
  }

  @Test public void should_fail_if_actual_contains_duplicates() {
    Collection<Double> duplicates = set(6d, 8d);
    actual = array(6d, 8d, 6d, 8d);
    try {
      collections.assertDoesNotHaveDuplicates(info, actual);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, hasDuplicates(wrap(actual), duplicates));
  }
}
