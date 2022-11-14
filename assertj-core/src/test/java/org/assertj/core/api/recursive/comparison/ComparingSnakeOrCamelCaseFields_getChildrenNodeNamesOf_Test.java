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