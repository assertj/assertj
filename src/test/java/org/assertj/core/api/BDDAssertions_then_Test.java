/*
 * Created on Dec 26, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for <code>{@link org.assertj.core.api.BDDAssertions#then(String)}</code>.
 *
 * @author Mariusz Smykula
 */
public class BDDAssertions_then_Test {

  @Test
  public void then_char() {
    then('z').isGreaterThan('a');
  }

  @Test
  public void then_Character() {
    then(new Character('A')).isEqualTo(new Character('A'));
  }


  @Test
  public void then_char_array() {
    then(new char[]{'a', 'b', 'c'}).contains('b');
  }


  @Test
  public void then_Charsequence() {
    then("abc".subSequence(0, 1)).contains("a");
  }

  @Test
  public void then_Class() {
    then("Foo".getClass()).isEqualTo(String.class);
  }

  @Test
  public void then_Iterable() {
    Iterable<String> iterable = Arrays.asList("1");
    then(iterable).contains("1");
  }

  @Test
  public void then_Iterator() {
    Iterator<String> iterator = Arrays.asList("1").iterator();
    then(iterator).contains("1");
  }

  @Test
  public void then_double() {
    then(1d).isNotZero();
  }

  @Test
  public void then_Double() {
    then(Double.valueOf(1d)).isNotZero();
  }

  @Test
  public void then_double_array() {
    then(new double[]{1d, 2d}).contains(2d);
  }

  @Test
  public void then_float() {
    then(1f).isEqualTo(1f);
  }

  @Test
  public void then_Float() {
    then(Float.valueOf(1f)).isEqualTo(1f);
  }

  @Test
  public void then_float_array() {
    then(new float[]{1f, 2f}).contains(2f);
  }

  @Test
  public void then_long() {
    then(1L).isEqualTo(1L);
  }

  @Test
  public void then_Long() {
    then(Long.valueOf(1L)).isEqualTo(1L);
  }

  @Test
  public void then_long_array() {
    then(new long[]{1L, 2L}).contains(2L);
  }

  @Test
  public void then_Object() {
    then(new Object()).isNotNull();
  }

  @Test
  public void then_Object_array() {
    then(new Object[]{new Object(), new Object()}).hasSize(2);
  }

  @Test
  public void then_short() {
    then((short) 1).isEqualTo((short) 1);
  }

  @Test
  public void then_Short() {
    then(Short.valueOf("1")).isEqualTo((short) 1);
  }

  @Test
  public void then_short_array() {
    then(new short[]{(short) 1, (short) 2}).contains((short) 2);
  }

  @Test
  public void then_Throwable() {
    then(new IllegalArgumentException("Foo")).hasMessage("Foo");
  }

  @Test
  public void then_BigDecimal() {
    then(BigDecimal.ONE).isEqualTo(BigDecimal.valueOf(1));
  }

  @Test
  public void then_boolean() {
    then(true).isEqualTo(Boolean.TRUE);
  }

  @Test
  public void then_Boolean() {
    then(Boolean.TRUE).isEqualTo(true);
  }

  @Test
  public void then_boolean_array() {
    then(new boolean[]{true, false}).isEqualTo(new boolean[]{true, false});
  }

  @Test
  public void then_byte() {
    then((byte) 7).isEqualTo((byte) 0x07);
  }

  @Test
  public void then_Byte() {
    then(Byte.valueOf((byte) 8)).isEqualTo((byte) 0x08);
  }

  @Test
  public void then_byte_array() {
    then(new byte[]{10, 11}).contains((byte) 11);
  }

  @Test
  public void then_int() {
    then(1).isEqualTo(1);
  }

  @Test
  public void then_Integer() {
    then(Integer.valueOf(4)).isEqualTo(4);
  }

  @Test
  public void then_int_array() {
    then(new int[]{2, 3}).isEqualTo(new int[]{2, 3});
  }

  @Test
  public void then_List() {
    then(Arrays.asList(5, 6)).hasSize(2);
  }

  @Test
  public void then_String() {
    then("Foo").isEqualTo("Foo");
  }


  @Test
  public void then_Date() {
    then(new Date()).isNotNull();
  }

  @Test
  public void then_Map() {
    then(new HashMap<String, String>()).isEmpty();
  }
}
