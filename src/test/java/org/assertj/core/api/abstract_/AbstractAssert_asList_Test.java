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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for Assert.asList() methods
 */
public class AbstractAssert_asList_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_allow_to_perform_List_assertions_on_List_variable_declared_as_Object() {
    Object listAsObject = Arrays.asList(1, 2, 3);
    assertThat(listAsObject).asList().isSorted();
  }

  @Test
  public void should_pass_list_assertions_on_list_strings_with_asList() {
    List<String> listAsObject = Arrays.asList("a", "b", "c");
    assertThat(listAsObject).asList()
                            .isSorted()
                            .last().isEqualTo("c");
  }

  @Test
  public void should_fail_as_variable_runtime_type_is_not_List() {
    // GIVEN
    Object greatAuthor = "Terry Pratchett";
    // WHEN
    try {
      assertThat(greatAuthor).asList().contains(1, 2);
    } catch (AssertionError e) {
      // THEN
      assertThat(e).hasMessageContaining(format("Expecting:%n" +
                                                " <\"Terry Pratchett\">%n" +
                                                "to be an instance of:%n" +
                                                " <java.util.List>%n" +
                                                "but was instance of:%n" +
                                                " <java.lang.String>"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
