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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Strings.quote;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link StandardRepresentation#formatArray(Object)}</code>.
 */
public class StandardRepresentation_array_format_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  public void should_return_null_if_array_is_null() {
    // GIVEN
    final Object array = null;
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_array_is_empty() {
    // GIVEN
    final Object[] array = new Object[0];
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[]");
  }

  @ParameterizedTest(name = "{1} should be formatted as {2}")
  @MethodSource("should_format_primitive_array_source")
  public void should_format_primitive_array(Object array, String expectedDescription) {
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo(expectedDescription);
  }

  private static Stream<Arguments> should_format_primitive_array_source() {
    return Stream.of(Arguments.of(new boolean[] { true, false }, "[true, false]"),
                     Arguments.of(new char[] { 'a', 'b' }, "['a', 'b']"),
                     Arguments.of(new double[] { 6.8, 8.3 }, "[6.8, 8.3]"),
                     Arguments.of(new float[] { 6.1f, 8.6f }, "[6.1f, 8.6f]"),
                     Arguments.of(new int[] { 78, 66 }, "[78, 66]"),
                     Arguments.of(new long[] { 160L, 98L }, "[160L, 98L]"),
                     Arguments.of(new short[] { (short) 5, (short) 8 }, "[5, 8]"),
                     Arguments.of(new int[] { 78, 66 }, "[78, 66]"),
                     Arguments.of(new int[] { 78, 66 }, "[78, 66]"),
                     Arguments.of(new int[] { 78, 66 }, "[78, 66]"),
                     Arguments.of(new boolean[] { true, false }, "[true, false]"));
  }

  @Test
  public void should_format_byte_array_in_hex_representation() {
    // GIVEN
    Object array = new byte[] { (byte) 3, (byte) 8 };
    // WHEN
    String formatted = new HexadecimalRepresentation().formatArray(array);
    // THEN
    then(formatted).isEqualTo("[0x03, 0x08]");
  }

  @Test
  public void should_return_null_if_parameter_is_not_array() {
    // GIVEN
    String string = "Hello";
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(string);
    // THEN
    then(formatted).isNull();
  }

  @Test
  public void should_format_String_array() {
    // GIVEN
    Object[] array = { "Hello", "World" };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo(format("[\"Hello\", \"World\"]"));
  }

  @Test
  public void should_format_Object_array() {
    // GIVEN
    Object[] array = { "Hello", new Person("Anakin") };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo(format("[\"Hello\", 'Anakin']"));
  }

  @Test
  public void should_format_Object_array_on_new_line_smart() {
    // GIVEN
    StandardRepresentation.setMaxLengthForSingleLineDescription(11);
    Object[] array = { "Hello", new Person("Anakin") };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo(format("[\"Hello\",%n"
                                     + "    'Anakin']"));
  }

  @Test
  public void should_format_Object_array_that_has_primitive_array_as_element() {
    // GIVEN
    boolean[] booleans = { true, false };
    Object[] array = { "Hello", booleans };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[\"Hello\", [true, false]]");
  }

  @Test
  public void should_format_Object_array_with_itself_as_element() {
    // GIVEN
    Object[] array = { "Hello", null };
    array[1] = array;
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[\"Hello\", (this array)]");
  }

  @Test
  public void should_format_self_referencing_Object_array() {
    // GIVEN
    Object[] array = { null, null };
    array[0] = array;
    array[1] = array;
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[(this array), (this array)]");
  }

  @Test
  public void should_format_Object_array_having_with_primitive_array() {
    // GIVEN
    Object[] array = { "Hello", new int[] {} };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[\"Hello\", []]");
  }

  @Test
  public void should_format_Object_array_with_null_element() {
    // GIVEN
    Object[] array = { "Hello", null };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[\"Hello\", null]");
  }

  @Test
  public void should_format_array_up_to_the_maximum_allowed_elements() {
    // GIVEN
    StandardRepresentation.setMaxElementsForPrinting(3);
    Object[] array = { "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh" };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    then(formatted).isEqualTo("[\"First\", \"Second\", ... \"Seventh\"]");
  }

  @Test
  public void should_format_array_with_one_element_per_line() {
    // GIVEN
    StandardRepresentation.setMaxLengthForSingleLineDescription(25);
    Object[] array = { "1234567890", "1234567890", "1234567890", "1234567890" };
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    String formattedAfterNewLine = "  <" + formatted + ">";
    then(formattedAfterNewLine).isEqualTo(format("  <[\"1234567890\",%n" +
                                                 "    \"1234567890\",%n" +
                                                 "    \"1234567890\",%n" +
                                                 "    \"1234567890\"]>"));
  }

  @ParameterizedTest(name = "with printing {0} max, {1} should be formatted as {2}")
  @MethodSource("should_format_array_source")
  public void should_format_array_honoring_display_configuration(int maxElementsForPrinting, Object[] array,
                                                                 String expectedDescription) {
    // GIVEN
    StandardRepresentation.setMaxElementsForPrinting(maxElementsForPrinting);
    StandardRepresentation.setMaxLengthForSingleLineDescription(15);
    // WHEN
    String formatted = STANDARD_REPRESENTATION.formatArray(array);
    // THEN
    // formattedAfterNewLine is built to show we align values on the first element.
    String formattedAfterNewLine = "  <" + formatted + ">";
    then(formattedAfterNewLine).isEqualTo(format(expectedDescription));
  }

  private static Stream<Arguments> should_format_array_source() {
    return Stream.of(Arguments.of(12, array(1, 2, 3, 4, 5), "  <[1, 2, 3, 4, 5]>"),
                     Arguments.of(12, array("First", 3, "foo", "bar"), "  <[\"First\",%n" +
                                                                       "    3,%n" +
                                                                       "    \"foo\",%n" +
                                                                       "    \"bar\"]>"),
                     Arguments.of(12, array("First", 3, 4, "foo", "bar", 5, "another", 6), "  <[\"First\",%n" +
                                                                                           "    3,%n" +
                                                                                           "    4,%n" +
                                                                                           "    \"foo\",%n" +
                                                                                           "    \"bar\",%n" +
                                                                                           "    5,%n" +
                                                                                           "    \"another\",%n" +
                                                                                           "    6]>"),
                     Arguments.of(12, array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), "  <[1,%n" +
                                                                            "    2,%n" +
                                                                            "    3,%n" +
                                                                            "    4,%n" +
                                                                            "    5,%n" +
                                                                            "    6,%n" +
                                                                            "    7,%n" +
                                                                            "    8,%n" +
                                                                            "    9,%n" +
                                                                            "    10]>"),
                     Arguments.of(12, array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), "  <[1,%n" +
                                                                                    "    2,%n" +
                                                                                    "    3,%n" +
                                                                                    "    4,%n" +
                                                                                    "    5,%n" +
                                                                                    "    6,%n" +
                                                                                    "    7,%n" +
                                                                                    "    8,%n" +
                                                                                    "    9,%n" +
                                                                                    "    10,%n" +
                                                                                    "    11,%n" +
                                                                                    "    12]>"),
                     Arguments.of(11, array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19), "  <[1,%n" +
                                                                                                                "    2,%n" +
                                                                                                                "    3,%n" +
                                                                                                                "    4,%n" +
                                                                                                                "    5,%n" +
                                                                                                                "    6,%n" +
                                                                                                                "    ...%n" +
                                                                                                                "    15,%n" +
                                                                                                                "    16,%n" +
                                                                                                                "    17,%n" +
                                                                                                                "    18,%n" +
                                                                                                                "    19]>"),
                     Arguments.of(12, array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20), "  <[1,%n" +
                                                                                                                    "    2,%n" +
                                                                                                                    "    3,%n" +
                                                                                                                    "    4,%n" +
                                                                                                                    "    5,%n" +
                                                                                                                    "    6,%n" +
                                                                                                                    "    ...%n" +
                                                                                                                    "    15,%n" +
                                                                                                                    "    16,%n" +
                                                                                                                    "    17,%n" +
                                                                                                                    "    18,%n" +
                                                                                                                    "    19,%n" +
                                                                                                                    "    20]>"));
  }

  private static class Person {
    private final String name;

    Person(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return quote(name);
    }
  }

}
