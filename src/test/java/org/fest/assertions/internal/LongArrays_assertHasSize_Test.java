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

import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.test.LongArrayFactory.array;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link LongArrays#assertHasSize(AssertionInfo, long[], int)}</code>.
 *
 * @author Alex Ruiz
 */
public class LongArrays_assertHasSize_Test {

  private static WritableAssertionInfo info;
  private static long[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private LongArrays arrays;


  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = array(6L, 8L);
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new LongArrays(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertHasSize(info, null, 3);
  }

  @Test public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    try {
      arrays.assertHasSize(info, actual, 3);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotHaveSize(wrap(actual), 3));
  }

  @Test public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSize(info, actual, 2);
  }
}
