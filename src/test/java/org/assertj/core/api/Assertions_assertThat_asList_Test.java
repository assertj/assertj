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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for Assert.asList() methods
 */
public class Assertions_assertThat_asList_Test {

  @Test
  public void should_pass_list_asserts_on_list_objects_with_asList() {
    Object listAsObject = asList(1, 2, 3);
    assertThat(listAsObject).asList().isSorted();
  }

  @Test
  public void should_pass_list_asserts_on_list_strings_with_asList() {
    List<String> listAsObject = asList("a", "b", "c");
    assertThat(listAsObject).asList().isSorted()
                            .last().isEqualTo("c");
  }

  @Test
  public void should_fail_list_asserts_on_non_list_objects_even_with_asList() {
    Object nonList = new Object();

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(nonList).asList().isSorted())
                                                   .withMessageContaining(format("an instance of:%n  <java.util.List>%nbut was instance of:%n  <java.lang.Object>"));
  }

  @Test
  public void should_keep_existing_description_set_before_calling_asList() {
    // GIVEN
    Object listAsObject = asList(1, 2, 3);
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(listAsObject).as("oops").asList().isEmpty());
    // THEN
    assertThat(error).hasMessageContaining("oops");
  }
}
