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

import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Sets;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertIsAssignableFrom(org.assertj.core.api.AssertionInfo, Class, Class[])}</code>
 * .
 * 
 * @author William Delanoue
 */
public class Classes_assertIsAssignableFrom_Test extends ClassesBaseTest {

  @Test
  public void should_pass_if_actual_is_assignable_from() {
    actual = Jedi.class;
    classes.assertIsAssignableFrom(someInfo(), actual, HumanJedi.class);
  }

  @Test
  public void should_fail_if_given_classes_are_empty() {
    actual = Jedi.class;
    thrown.expectIllegalArgumentException("Expecting at least one Class to be specified");
    classes.assertIsAssignableFrom(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertIsAssignableFrom(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_assignable_from_int() {
    actual = int.class;
    classes.assertIsAssignableFrom(someInfo(), actual, int.class);
  }

  @Test
  public void should_fail_if_actual_is_not_assignable_from() {
    actual = HumanJedi.class;
    Class<?>[] expected = new Class[] { HumanJedi.class, Jedi.class };
    thrown.expectAssertionError(shouldBeAssignableFrom(actual, Sets.<Class<?>> newLinkedHashSet(expected),
                                                       Sets.<Class<?>> newLinkedHashSet(Jedi.class)));
    classes.assertIsAssignableFrom(someInfo(), actual, expected);
  }
}
