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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.classes;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePermittedSubclasses.shouldHavePermittedSubclasses;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.newArrayList;

class ClassAssert_hasPermittedSubclasses {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasPermittedSubclasses());
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_have_permitted_subclasses() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(Object.class).hasPermittedSubclasses(String.class));
    // THEN
    then(error).hasMessage(shouldHavePermittedSubclasses(Object.class,
                                                         newArrayList(String.class),
                                                         newArrayList(String.class)).create());
  }
}
