/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class Assertions_assertThat_asString_Test {

  @Test
  void should_allow_string_assertions() {
    // GIVEN
    Object stringAsObject = "hello world";
    // WHEN/THEN
    then(stringAsObject).asString()
                        .startsWith("hello")
                        .endsWith("world");
  }

  @Test
  void should_allow_string_assertions_on_object_string_representation() {
    // GIVEN
    Object nonString = new Object();
    // WHEN/THEN
    then(nonString).asString().startsWith(nonString.toString());
  }

  @Test
  void should_fail_if_actual_is_null() {
    expectAssertionError(() -> assertThat((Object) null).asString().isEqualTo("never gonna happen"));
  }

  @Test
  public void should_keep_existing_description_set_before_calling_asString() {
    // GIVEN
    String description = "My description";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("foo").as(description).asString().isEmpty());
    // THEN
    then(assertionError).hasMessageContaining(description);
  }
}
