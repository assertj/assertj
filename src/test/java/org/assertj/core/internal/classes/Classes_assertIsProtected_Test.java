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
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ClassModifierShouldBe.shouldBeProtected;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.Test;

public class Classes_assertIsProtected_Test extends ClassesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    classes.assertIsProtected(someInfo(), null);
  }

  @Test
  public void should_pass_if_actual_is_a_protected_class() {
    classes.assertIsProtected(someInfo(), MethodsClass.class);
  }

  @Test
  public void should_fail_if_actual_is_not_a_protected_class() {
    thrown.expectAssertionError(shouldBeProtected(Object.class));
    classes.assertIsProtected(someInfo(), Object.class);
  }
}
