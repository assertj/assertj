/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveToString.shouldHaveToString;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Objects_assertHasToString_Test extends ObjectsBaseTest {

  private Person actual;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    actual = mock(Person.class);
    when(actual.toString()).thenReturn("foo");
  }

  @Test
  void should_pass_if_actual_toString_is_the_expected_String() {
    objects.assertHasToString(someInfo(), actual, "foo");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Object object = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasToString(someInfo(), object, "foo"));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_toString_is_not_the_expected_String() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> objects.assertHasToString(info, actual, "bar"));
    // THEN
    verify(failures).failure(info, shouldHaveToString("foo", "bar"), "foo", "bar");
  }
}
