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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields.COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS;

import java.util.Set;

import org.junit.jupiter.api.Test;

class ComparingSnakeOrCamelCaseFields_getChildrenNodeNamesOf_Test {

  @Test
  void getChildrenNodeNamesOf_return_all_fields_names_in_camel_case() {
    // GIVEN
    Fields node = new Fields();
    // WHEN
    Set<String> childrenNodeNames = COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS.getChildrenNodeNamesOf(node);
    // THEN
    then(childrenNodeNames).containsExactlyInAnyOrder("field", "fieldTwo", "fieldThree", "fieldFour",
                                                      "field5", "field6", "fieldSeven", "fieldDto");
  }

  @SuppressWarnings("unused")
  static class Fields {
    String field = "field value";
    String field_two = "field_two value";
    String _field_three = "_field_three value";
    String field_four_ = "field_four_ value";
    String field_5 = "field_5 value";
    String _field_6_ = "_field_6_ value";
    String fieldSeven = "fieldSeven value";
    String fieldDTO = "fieldDTO value";
  }

}