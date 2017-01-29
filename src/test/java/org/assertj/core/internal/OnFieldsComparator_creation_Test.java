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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class OnFieldsComparator_creation_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_create_comparator_using_fields() {
    OnFieldsComparator comparator = new OnFieldsComparator("a", "b");
    assertThat(comparator).isNotNull();
    assertThat(comparator.getFields()).containsExactly("a", "b");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_no_fields_are_given() {
    thrown.expectIllegalArgumentException("No fields/properties specified");
    new OnFieldsComparator();
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_null_array_fields_is_given() {
    thrown.expectIllegalArgumentException("No fields/properties specified");
    new OnFieldsComparator((String[]) null);
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_empty_array_fields_is_given() {
    thrown.expectIllegalArgumentException("No fields/properties specified");
    new OnFieldsComparator(new String[0]);
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_some_fields_are_null() {
    thrown.expectIllegalArgumentException("Null/blank fields/properties are invalid, fields/properties were [\"a\", null]");
    new OnFieldsComparator("a", null);
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_some_fields_are_empty() {
    thrown.expectIllegalArgumentException("Null/blank fields/properties are invalid, fields/properties were [\"a\", \"\"]");
    new OnFieldsComparator("a", "");
  }

  @SuppressWarnings("unused")
  @Test
  public void should_fail_if_some_fields_are_blank() {
    thrown.expectIllegalArgumentException("Null/blank fields/properties are invalid, fields/properties were [\"a\", \" \"]");
    new OnFieldsComparator("a", " ");
  }

}