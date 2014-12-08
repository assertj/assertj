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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.*;

import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for <code>{@link Arrays#format(org.assertj.core.presentation.Representation, Object)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Arrays_format_Test {

  @Test
  public void should_return_null_if_array_is_null() {
    final Object array = null;
    assertNull(Arrays.format(new StandardRepresentation(), array));
    assertNull(Arrays.format(array));
  }

  @Test
  public void should_return_empty_brackets_if_array_is_empty() {
    final Object[] array = new Object[0];
    assertEquals("[]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[]", Arrays.format(array));
  }

  @Test
  public void should_format_Object_array() {
    Object array = new Object[] { "First", 3 };
    assertEquals("[\"First\", 3]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[\"First\", 3]", Arrays.format(array));
  }

  @Test
  public void should_format_boolean_array() {
    Object array = new boolean[] { true, false };
    assertEquals("[true, false]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[true, false]", Arrays.format(array));
  }

  @Test
  public void should_format_byte_array() {
    Object array = new byte[] { (byte) 3, (byte) 8 };
    assertEquals("[3, 8]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[3, 8]", Arrays.format(array));
  }

  @Test
  public void should_format_byte_array_in_hex_representation() {
    Object array = new byte[] { (byte) 3, (byte) 8 };
    assertEquals("[0x03, 0x08]", Arrays.format(new HexadecimalRepresentation(), array));
  }

  @Test
  public void should_format_char_array() {
    Object array = new char[] { 'a', 'b' };
    assertEquals("['a', 'b']", Arrays.format(new StandardRepresentation(), array));
    assertEquals("['a', 'b']", Arrays.format(array));
  }

  @Test
  public void should_format_double_array() {
    Object array = new double[] { 6.8, 8.3 };
    assertEquals("[6.8, 8.3]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[6.8, 8.3]", Arrays.format(array));
  }

  @Test
  public void should_format_float_array() {
    Object array = new float[] { 6.1f, 8.6f };
    assertEquals("[6.1f, 8.6f]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[6.1f, 8.6f]", Arrays.format(array));
  }

  @Test
  public void should_format_int_array() {
    Object array = new int[] { 78, 66 };
    assertEquals("[78, 66]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[78, 66]", Arrays.format(array));
  }

  @Test
  public void should_format_long_array() {
    Object array = new long[] { 160l, 98l };
    assertEquals("[160L, 98L]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[160L, 98L]", Arrays.format(array));
  }

  @Test
  public void should_format_short_array() {
    Object array = new short[] { (short) 5, (short) 8 };
    assertEquals("[5, 8]", Arrays.format(new StandardRepresentation(), array));
    assertEquals("[5, 8]", Arrays.format(array));
  }
}
