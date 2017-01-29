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
package org.assertj.core.internal.objects;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Objects#assertThat(AssertionInfo, Object).isNull()}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_assertNull_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_if_object_is_null() {
    objects.assertNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_object_is_not_null() {
    AssertionInfo info = someInfo();
    Object actual = new Object();
    try {
      objects.assertNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, null, info.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
