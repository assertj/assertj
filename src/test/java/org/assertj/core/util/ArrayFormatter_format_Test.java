/*
 * Created on Mar 29, 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2009-2012 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Strings.quote;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link ArrayFormatter#format(Object)}.
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
    assertNull(formatter.format(null));
  }

  @Test
  public void should_return_null_if_parameter_is_not_array() {
    assertNull(formatter.format("Hello"));
  }

  @Test
  public void should_format_boolean_array() {
    assertEquals("[true, false, true]", formatter.format(new boolean[] { true, false, true }));
  }

  @Test
  public void should_format_char_array() {
    assertEquals("[a, b, c]", formatter.format(new char[] { 'a', 'b', 'c' }));
  }

  @Test
  public void should_format_byte_array() {
    assertEquals("[6, 8]", formatter.format(new byte[] { 6, 8 }));
  }

  @Test
  public void should_format_short_array() {
    assertEquals("[6, 8]", formatter.format(new short[] { 6, 8 }));
  }

  @Test
  public void should_format_int_array() {
    assertEquals("[6, 8]", formatter.format(new int[] { 6, 8 }));
  }

  @Test
  public void should_format_longArray() {
    assertEquals("[6, 8]", formatter.format(new long[] { 6l, 8l }));
  }

  @Test
  public void should_format_float_array() {
    assertEquals("[6.0, 8.0]", formatter.format(new float[] { 6f, 8f }));
  }

  @Test
  public void should_format_double_array() {
    assertEquals("[6.0, 8.0]", formatter.format(new double[] { 6d, 8d }));
  }

  @Test
  public void should_format_String_array() {
    assertEquals("['Hello', 'World']", formatter.format(new Object[] { "Hello", "World" }));
  }

  @Test
  public void should_format_array_with_null_element() {
    assertEquals("['Hello', null]", formatter.format(new Object[] { "Hello", null }));
  }

  @Test
  public void should_format_Object_array() {
    assertEquals("['Hello', 'Anakin']", formatter.format(new Object[] { "Hello", new Person("Anakin") }));
  }

  @Test
  public void should_format_Object_array_that_has_primitive_array_as_element() {
    boolean booleans[] = { true, false };
    Object[] array = { "Hello", booleans };
    assertEquals("['Hello', [true, false]]", formatter.format(array));
  }

  @Test
  public void should_format_Object_array_having_itself_as_element() {
    Object[] array1 = { "Hello", "World" };
    Object[] array2 = { array1 };
    array1[1] = array2;
    assertEquals("[['Hello', [...]]]", formatter.format(array2));
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
