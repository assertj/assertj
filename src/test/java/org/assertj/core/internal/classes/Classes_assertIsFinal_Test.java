/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ShouldBeFinal.shouldBeFinal;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Classes#assertIsFinal(AssertionInfo, Class)}</code>.
 *
 * @author Michal Kordas
 */
public class Classes_assertIsFinal_Test extends ClassesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertIsAnnotation(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_final_class() {
    actual = Math.class;
    classes.assertIsFinal(someInfo(), actual);
  }

  @Test()
  public void should_fail_if_actual_is_not_final_class() {
    AssertionInfo info = someInfo();
    actual = Object.class;
    try {
      classes.assertIsFinal(someInfo(), actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeFinal(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
