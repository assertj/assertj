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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ComparingSnakeOrCamelCaseFields_getChildNodeValue_Test {

  @ParameterizedTest
  @CsvSource({
      "field, field value",
      "fieldTwo, field_two value",
      "fieldThree, _field_three value",
      "fieldFour, field_four_ value",
      "field5, field_5 value",
      "field6, _field_6_ value",
      "fieldSeven, fieldSeven value",
      "fieldDto, fieldDTO value",
      "ambiguous_field_1,ambiguous_field_1 value",
      "ambiguous_field1,ambiguous_field1 value",
      "_ambiguous_field1,_ambiguous_field1 value",
      "ambiguous_field_1_,ambiguous_field_1_ value",
      "_ambiguous_field_1_,_ambiguous_field_1_ value",
      "ambiguousField1,ambiguousField1 value"
  })
  @SuppressWarnings("ignored")
  void getChildNodeValue_should_read_fields_given_their_camel_case_form(String fieldName, String expectedValue) {
    // GIVEN
    Fields node = new Fields();
    ComparingSnakeOrCamelCaseFields comparingSnakeOrCamelCaseFields = new ComparingSnakeOrCamelCaseFields();
    // comparingSnakeOrCamelCaseFields.getChildrenNodeNamesOf(node);
    // WHEN
    Object value = comparingSnakeOrCamelCaseFields.getChildNodeValue(fieldName, node);
    // THEN
    then(value).isEqualTo(expectedValue);
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

    String ambiguous_field_1 = "ambiguous_field_1 value";
    String ambiguous_field1 = "ambiguous_field1 value";
    String _ambiguous_field1 = "_ambiguous_field1 value";
    String ambiguous_field_1_ = "ambiguous_field_1_ value";
    String _ambiguous_field_1_ = "_ambiguous_field_1_ value";
    String ambiguousField1 = "ambiguousField1 value";
  }

}
