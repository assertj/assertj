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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.error.GroupTypeDescription;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;

class Objects_assertHasOnlyFields_Test extends ObjectsBaseTest {

  private static final AssertionInfo INFO = someInfo();
  private static final GroupTypeDescription FIELDS_GROUP_DESCRIPTION = new GroupTypeDescription("non static/synthetic fields of",
                                                                                                "fields");

  @Test
  void should_pass_if_actual_has_all_given_instance_level_fields_and_nothing_else() {
    // GIVEN
    Object actual = new Data();
    // WHEN/THEN
    objects.assertHasOnlyFields(INFO, actual, "field1", "field2"); // static fields ignored
  }

  @Test
  void should_fail_if_some_fields_are_missing() {
    // GIVEN
    Object actual = new Data();
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasOnlyFields(INFO, actual, "field1", "field2", "field3"));
    // THEN
    assertThat(error).hasMessage(shouldContainOnly(actual, list("field1", "field2", "field3"), list("field3"), emptyList(),
                                                   FIELDS_GROUP_DESCRIPTION).create());
  }

  @Test
  void should_fail_if_actual_has_extra_fields() {
    // GIVEN
    Object actual = new Data();
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasOnlyFields(INFO, actual, "field1"));
    // THEN
    assertThat(error).hasMessage(shouldContainOnly(actual, list("field1"), emptyList(), list("field2"),
                                                   FIELDS_GROUP_DESCRIPTION).create());
  }

  @Test
  void should_fail_if_actual_has_missing_and_extra_fields() {
    // GIVEN
    Object actual = new Data();
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasOnlyFields(INFO, actual, "field1", "field3"));
    // THEN
    assertThat(error).hasMessage(shouldContainOnly(actual, list("field1", "field3"), list("field3"), list("field2"),
                                                   FIELDS_GROUP_DESCRIPTION).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Object actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertHasOnlyFields(INFO, actual));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_field_names_are_null() {
    // GIVEN
    Object actual = new Data();
    // WHEN
    Throwable exception = catchThrowable(() -> objects.assertHasOnlyFields(INFO, actual, (String[]) null));
    // THEN
    assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage("Given fields/properties are null");
  }

  @SuppressWarnings("unused")
  private static class Data {

    public static Object publicStaticField;
    private static Object privateStaticField;
    private Object field1;
    private Object field2;

    @Override
    public String toString() {
      return "data";
    }

  }
}
