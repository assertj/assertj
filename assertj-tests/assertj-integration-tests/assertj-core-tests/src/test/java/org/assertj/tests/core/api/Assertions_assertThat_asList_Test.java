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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class Assertions_assertThat_asList_Test {

  @Test
  void should_pass_list_asserts_on_list_objects_with_asList() {
    // GIVEN
    Object listAsObject = list(1, 2, 3);
    // WHEN/THEN
    then(listAsObject).asList().isSorted();
  }

  @Test
  void should_pass_list_asserts_on_list_strings_with_asList() {
    // GIVEN
    List<String> listAsObject = list("a", "b", "c");
    // WHEN/THEN
    then(listAsObject).asList()
                      .isSorted()
                      .last().isEqualTo("c");
  }

  @Test
  void should_fail_list_asserts_on_non_list_objects_even_with_asList() {
    // GIVEN
    Object nonList = new Object();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(nonList).asList().isSorted());
    // THEN
    then(error).hasMessageContainingAll("an instance of", "java.util.List", "but was instance of", "java.lang.Object");
  }

  @Test
  void should_keep_existing_description_set_before_calling_asList() {
    // GIVEN
    Object listAsObject = list(1, 2, 3);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(listAsObject).as("oops").asList().isEmpty());
    // THEN
    then(error).hasMessageContaining("oops");
  }

}
