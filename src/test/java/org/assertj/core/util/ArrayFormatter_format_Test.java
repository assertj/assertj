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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Strings.quote;

import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link ArrayFormatter#format(org.assertj.core.presentation.Representation, Object)}.
 * 
 * @author Alex Ruiz
 */
public class ArrayFormatter_format_Test {
  private static ArrayFormatter formatter;

  @BeforeClass
  public static void setUpOnce() {
    formatter = new ArrayFormatter();
  }

  @Test
  public void should_return_null_if_array_is_null() {
    assertThat(formatter.format(new StandardRepresentation(), null)).isNull();
  }

  @Test
  public void should_return_null_if_parameter_is_not_array() {
    assertThat(formatter.format(new StandardRepresentation(), "Hello")).isNull();
  }

  @Test
  public void should_format_boolean_array() {
    assertThat(formatter.format(new StandardRepresentation(), new boolean[]{true, false, true}))
        .isEqualTo("[true, false, true]");
  }

  @Test
  public void should_format_char_array() {
    assertThat(formatter.format(new StandardRepresentation(), new char[] { 'a', 'b', 'c' }))
        .isEqualTo("['a', 'b', 'c']");
  }

  @Test
  public void should_format_byte_array() {
    assertThat(formatter.format(new StandardRepresentation(), new byte[] { 6, 8 }))
        .isEqualTo("[6, 8]");
  }

  @Test
  public void should_format_byte_array_in_hex_representation() {
    assertThat(formatter.format(new HexadecimalRepresentation(), new byte[] { 6, 8 }))
        .isEqualTo("[0x06, 0x08]");
  }

  @Test
  public void should_format_short_array() {
    assertThat(formatter.format(new StandardRepresentation(), new short[] { 6, 8 }))
        .isEqualTo("[6, 8]");
  }

  @Test
  public void should_format_int_array() {
    assertThat(formatter.format(new StandardRepresentation(), new int[] { 6, 8 }))
        .isEqualTo("[6, 8]");
  }

  @Test
  public void should_format_longArray() {
    assertThat(formatter.format(new StandardRepresentation(), new long[] { 6l, 8l }))
        .isEqualTo("[6L, 8L]");
  }

  @Test
  public void should_format_float_array() {
    assertThat(formatter.format(new StandardRepresentation(), new float[] { 6f, 8f }))
        .isEqualTo("[6.0f, 8.0f]");
  }

  @Test
  public void should_format_double_array() {
    assertThat(formatter.format(new StandardRepresentation(), new double[] { 6d, 8d }))
        .isEqualTo("[6.0, 8.0]");
  }

  @Test
  public void should_format_String_array() {
    assertThat(formatter.format(new StandardRepresentation(), new Object[] { "Hello", "World" }))
        .isEqualTo("[\"Hello\", \"World\"]");
  }

  @Test
  public void should_format_array_with_null_element() {
    assertThat(formatter.format(new StandardRepresentation(), new Object[] { "Hello", null }))
        .isEqualTo("[\"Hello\", null]");
  }

  @Test
  public void should_format_Object_array() {
    assertThat(formatter.format(new StandardRepresentation(), new Object[] { "Hello", new Person("Anakin") }))
        .isEqualTo("[\"Hello\", 'Anakin']");
  }

  @Test
  public void should_format_Object_array_that_has_primitive_array_as_element() {
    boolean booleans[] = { true, false };
    Object[] array = { "Hello", booleans };
    assertThat(formatter.format(new StandardRepresentation(), array))
        .isEqualTo("[\"Hello\", [true, false]]");
  }

  @Test
  public void should_format_Object_array_having_itself_as_element() {
    Object[] array1 = { "Hello", "World" };
    Object[] array2 = { array1 };
    array1[1] = array2;
    assertThat(formatter.format(new StandardRepresentation(), array2))
        .isEqualTo("[[\"Hello\", [...]]]");
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
