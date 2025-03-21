/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.classes;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;

import org.assertj.tests.core.internal.ClassesBaseTest;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertIsAssignableFrom(org.assertj.core.api.AssertionInfo, Class, Class[])}</code>
 * .
 * 
 * @author William Delanoue
 */
class Classes_assertIsAssignableFrom_Test extends ClassesBaseTest {

  @Test
  void should_pass_if_actual_is_assignable_from() {
    actual = Jedi.class;
    classes.assertIsAssignableFrom(someInfo(), actual, HumanJedi.class);
  }

  @Test
  void should_fail_if_given_classes_are_empty() {
    actual = Jedi.class;
    assertThatIllegalArgumentException().isThrownBy(() -> classes.assertIsAssignableFrom(someInfo(), actual))
                                        .withMessage("Expecting at least one Class to be specified");
  }

  @Test
  void should_fail_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertIsAssignableFrom(someInfo(), actual))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_assignable_from_int() {
    actual = int.class;
    classes.assertIsAssignableFrom(someInfo(), actual, int.class);
  }

  @Test
  void should_fail_if_actual_is_not_assignable_from() {
    actual = HumanJedi.class;
    Class<?>[] expected = new Class[] { HumanJedi.class, Jedi.class };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertIsAssignableFrom(someInfo(), actual, expected))
                                                   .withMessage(shouldBeAssignableFrom(actual,
                                                                                       Sets.newLinkedHashSet(expected),
                                                                                       Sets.newLinkedHashSet(Jedi.class)).create());
  }
}
