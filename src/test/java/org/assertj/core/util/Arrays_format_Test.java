/*
 * Created on May 13, 2007
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2007-2011 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for <code>{@link Arrays#format(Object)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Arrays_format_Test {

  @Test
  public void should_return_null_if_array_is_null() {
    assertNull(Arrays.format(null));
  }

  @Test
  public void should_return_empty_brackets_if_array_is_empty() {
    assertEquals("[]", Arrays.format(new Object[0]));
  }

  @Test
  public void should_format_Object_array() {
    Object o = new Object[] { "First", 3 };
    assertEquals("['First', 3]", Arrays.format(o));
  }

  @Test
  public void should_format_boolean_array() {
    Object o = new boolean[] { true, false };
    assertEquals("[true, false]", Arrays.format(o));
  }

  @Test
  public void should_format_byte_array() {
    Object o = new byte[] { (byte) 3, (byte) 8 };
    assertEquals("[3, 8]", Arrays.format(o));
  }

  @Test
  public void should_format_char_array() {
    Object o = new char[] { 'a', 'b' };
    assertEquals("[a, b]", Arrays.format(o));
  }

  @Test
  public void should_format_double_array() {
    Object o = new double[] { 6.8, 8.3 };
    assertEquals("[6.8, 8.3]", Arrays.format(o));
  }

  @Test
  public void should_format_float_array() {
    Object o = new float[] { 6.1f, 8.6f };
    assertEquals("[6.1, 8.6]", Arrays.format(o));
  }

  @Test
  public void should_format_int_array() {
    Object o = new int[] { 78, 66 };
    assertEquals("[78, 66]", Arrays.format(o));
  }

  @Test
  public void should_format_long_array() {
    Object o = new long[] { 160l, 98l };
    assertEquals("[160, 98]", Arrays.format(o));
  }

  @Test
  public void should_format_short_array() {
    Object o = new short[] { (short) 5, (short) 8 };
    assertEquals("[5, 8]", Arrays.format(o));
  }
}
