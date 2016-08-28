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
package org.assertj.core.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Strings.quote;

import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for <code>{@link StandardRepresentation#formatArray(org.assertj.core.presentation.Representation, Object)}</code>.
 * 
 * @author Alex Ruiz
 */
public class StandardRepresentation_array_format_Test {

  @Test
  public void should_return_null_if_array_is_null() {
    final Object array = null;
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_array_is_empty() {
    final Object[] array = new Object[0];
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[]");
  }

  @Test
  public void should_format_boolean_array() {
    Object array = new boolean[] { true, false };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[true, false]");
  }

  @Test
  public void should_format_byte_array() {
    Object array = new byte[] { (byte) 3, (byte) 8 };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[3, 8]");
  }

  @Test
  public void should_format_byte_array_in_hex_representation() {
    Object array = new byte[] { (byte) 3, (byte) 8 };
    assertThat(new HexadecimalRepresentation().formatArray(array)).isEqualTo("[0x03, 0x08]");
  }

  @Test
  public void should_format_char_array() {
    Object array = new char[] { 'a', 'b' };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("['a', 'b']");
  }

  @Test
  public void should_format_double_array() {
    Object array = new double[] { 6.8, 8.3 };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[6.8, 8.3]");
  }

  @Test
  public void should_format_float_array() {
    Object array = new float[] { 6.1f, 8.6f };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[6.1f, 8.6f]");
  }

  @Test
  public void should_format_int_array() {
    Object array = new int[] { 78, 66 };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[78, 66]");
  }

  @Test
  public void should_format_long_array() {
    Object array = new long[] { 160L, 98L };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[160L, 98L]");
  }

  @Test
  public void should_format_short_array() {
    Object array = new short[] { (short) 5, (short) 8 };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[5, 8]");
  }

  @Test
  public void should_return_null_if_parameter_is_not_array() {
    assertThat(STANDARD_REPRESENTATION.formatArray("Hello")).isNull();
  }

  @Test
  public void should_format_longArray() {
    assertThat(STANDARD_REPRESENTATION.formatArray(new long[] { 6L, 8L })).isEqualTo("[6L, 8L]");
  }

  @Test
  public void should_format_String_array() {
    assertThat(STANDARD_REPRESENTATION.formatArray(new Object[] { "Hello",
        "World" })).isEqualTo("[\"Hello\", \"World\"]");
  }

  @Test
  public void should_format_array_with_null_element() {
    assertThat(STANDARD_REPRESENTATION.formatArray(new Object[] { "Hello", null })).isEqualTo("[\"Hello\", null]");
  }

  @Test
  public void should_format_Object_array() {
    assertThat(STANDARD_REPRESENTATION.formatArray(new Object[] { "Hello",
        new Person("Anakin") })).isEqualTo("[\"Hello\", 'Anakin']");
  }

  @Test
  public void should_format_Object_array_that_has_primitive_array_as_element() {
    boolean booleans[] = { true, false };
    Object[] array = { "Hello", booleans };
    assertThat(STANDARD_REPRESENTATION.formatArray(array)).isEqualTo("[\"Hello\", [true, false]]");
  }

  @Test
  public void should_format_Object_array_having_itself_as_element() {
    Object[] array1 = { "Hello", "World" };
    Object[] array2 = { array1 };
    array1[1] = array2;
    assertThat(STANDARD_REPRESENTATION.formatArray(array2)).isEqualTo("[[\"Hello\", (this array)]]");
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
