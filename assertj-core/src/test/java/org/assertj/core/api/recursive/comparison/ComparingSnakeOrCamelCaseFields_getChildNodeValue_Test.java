package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields.COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;
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
  })
  void getChildNodeValue_should_read_fields_given_their_camel_case_form(String fieldName, String expectedValue) {
    // GIVEN
    Fields node = new Fields();
    // WHEN
    Object value = COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS.getChildNodeValue(fieldName, node);
    // THEN
    then(value).isEqualTo(expectedValue);
  }

  @Test
  void getChildNodeValue_cannot_resolve_ambiguous_fields() {
    // GIVEN
    Fields node = new Fields();
    // WHEN
    Throwable throwable = catchThrowable(() -> COMPARING_SNAKE_OR_CAMEL_CASE_FIELDS.getChildNodeValue("ambiguousField1", node));
    // THEN
    then(throwable).isInstanceOf(IntrospectionError.class)
                   .rootCause()
                   .hasMessageContaining("is ambiguous relative to class")
                   .hasMessageContaining("possible candidates are: ambiguous_field_1, ambiguous_field1, _ambiguous_field1, ambiguous_field_1_, _ambiguous_field_1_, ambiguousField1");
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

    String ambiguous_field_1;
    String ambiguous_field1;
    String _ambiguous_field1;
    String ambiguous_field_1_;
    String _ambiguous_field_1_;
    String ambiguousField1;
  }

}