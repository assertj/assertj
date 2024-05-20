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
package org.assertj.tests.core.api.class_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAbstract.shouldBeAbstract;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.AbstractCollection;
import org.junit.jupiter.api.Test;

class ClassAssert_isAbstract_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAbstract());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_not_abstract() {
    // GIVEN
    Class<?> actual = Object.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAbstract());
    // THEN
    then(assertionError).hasMessage(shouldBeAbstract(actual).create());
  }

  @Test
  void should_pass_if_actual_is_abstract() {
    // GIVEN
    Class<?> actual = AbstractCollection.class;
    // WHEN/THEN
    assertThat(actual).isAbstract();
  }

}
