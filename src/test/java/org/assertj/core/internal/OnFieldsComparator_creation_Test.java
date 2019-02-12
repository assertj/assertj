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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

public class OnFieldsComparator_creation_Test {

  @Test
  public void should_create_comparator_using_fields() {
    OnFieldsComparator comparator = new OnFieldsComparator("a", "b");
    assertThat(comparator).isNotNull();
    assertThat(comparator.getFields()).containsExactly("a", "b");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_no_fields_are_given() {
    assertThatIllegalArgumentException().isThrownBy(() -> new OnFieldsComparator())
                                        .withMessage("No fields/properties specified");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_null_array_fields_is_given() {
    assertThatIllegalArgumentException().isThrownBy(() -> new OnFieldsComparator((String[]) null))
                                        .withMessage("No fields/properties specified");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_empty_array_fields_is_given() {
    assertThatIllegalArgumentException().isThrownBy(() -> new OnFieldsComparator(new String[0]))
                                        .withMessage("No fields/properties specified");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_some_fields_are_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> new OnFieldsComparator("a", null))
                                        .withMessage("Null/blank fields/properties are invalid, fields/properties were [\"a\", null]");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_some_fields_are_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> new OnFieldsComparator("a", ""))
                                        .withMessage("Null/blank fields/properties are invalid, fields/properties were [\"a\", \"\"]");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_some_fields_are_blank() {
    assertThatIllegalArgumentException().isThrownBy(() -> new OnFieldsComparator("a", " "))
                                        .withMessage("Null/blank fields/properties are invalid, fields/properties were [\"a\", \" \"]");
  }

}