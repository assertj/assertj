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

import static org.assertj.core.api.Assertions.*;

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
    assertThat(Arrays.format(new StandardRepresentation(), array)).isNull();
    assertThat(Arrays.format(array)).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_array_is_empty() {
    final Object[] array = new Object[0];
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[]");
    assertThat(Arrays.format(array)).isEqualTo("[]");
  }

  @Test
  public void should_format_Object_array() {
    Object array = new Object[] { "First", 3 };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[\"First\", 3]");
    assertThat(Arrays.format(array)).isEqualTo("[\"First\", 3]");
  }

  @Test
  public void should_format_boolean_array() {
    Object array = new boolean[] { true, false };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[true, false]");
    assertThat(Arrays.format(array)).isEqualTo("[true, false]");
  }

  @Test
  public void should_format_byte_array() {
    Object array = new byte[] { (byte) 3, (byte) 8 };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[3, 8]");
    assertThat(Arrays.format(array)).isEqualTo("[3, 8]");
  }

  @Test
  public void should_format_byte_array_in_hex_representation() {
    Object array = new byte[] { (byte) 3, (byte) 8 };
    assertThat(Arrays.format(new HexadecimalRepresentation(), array)).isEqualTo("[0x03, 0x08]");
  }

  @Test
  public void should_format_char_array() {
    Object array = new char[] { 'a', 'b' };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("['a', 'b']");
    assertThat(Arrays.format(array)).isEqualTo("['a', 'b']");
  }

  @Test
  public void should_format_double_array() {
    Object array = new double[] { 6.8, 8.3 };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[6.8, 8.3]");
    assertThat(Arrays.format(array)).isEqualTo("[6.8, 8.3]");
  }

  @Test
  public void should_format_float_array() {
    Object array = new float[] { 6.1f, 8.6f };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[6.1f, 8.6f]");
    assertThat(Arrays.format(array)).isEqualTo("[6.1f, 8.6f]");
  }

  @Test
  public void should_format_int_array() {
    Object array = new int[] { 78, 66 };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[78, 66]");
    assertThat(Arrays.format(array)).isEqualTo("[78, 66]");
  }

  @Test
  public void should_format_long_array() {
    Object array = new long[] { 160l, 98l };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[160L, 98L]");
    assertThat(Arrays.format(array)).isEqualTo("[160L, 98L]");
  }

  @Test
  public void should_format_short_array() {
    Object array = new short[] { (short) 5, (short) 8 };
    assertThat(Arrays.format(new StandardRepresentation(), array)).isEqualTo("[5, 8]");
    assertThat(Arrays.format(array)).isEqualTo("[5, 8]");
  }
}
