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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for Assert.asList() methods
 */
public class Assertions_assertThat_asList_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_pass_list_asserts_on_list_objects_with_asList() {
    Object listAsObject = Arrays.asList(1, 2, 3);
    assertThat(listAsObject).asList().isSorted();
  }

  @Test
  public void should_pass_list_asserts_on_list_strings_with_asList() {
    List<String> listAsObject = Arrays.asList("a", "b", "c");
    assertThat(listAsObject).asList().isSorted()
                            .last().isEqualTo("c");
  }

  @Test
  public void should_fail_list_asserts_on_non_list_objects_even_with_asList() {
    Object nonList = new Object();

    thrown.expectAssertionErrorWithMessageContaining("an instance of:%n  <java.util.List>%nbut was instance of:%n  <java.lang.Object>");
    assertThat(nonList).asList().isSorted();
  }

}
