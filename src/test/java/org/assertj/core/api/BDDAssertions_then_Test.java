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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.BDDAssertions#then(String)}</code>.
 *
 * @author Mariusz Smykula
 */
public class BDDAssertions_then_Test {

  AssertFactory<String, StringAssert> stringAssertFactory = new AssertFactory<String, StringAssert>() {
    @Override
    public StringAssert createAssert(String string) {
      return new StringAssert(string);
    }
  };
  
  AssertFactory<Integer, IntegerAssert> integerAssertFactory = new AssertFactory<Integer, IntegerAssert>() {
    @Override
    public IntegerAssert createAssert(Integer string) {
      return new IntegerAssert(string);
    }
  };
  
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
    then(new char[] { 'a', 'b', 'c' }).contains('b');
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
  public void should_delegate_to_assert_comparable() {

    class IntBox implements Comparable<IntBox> {

      private final Integer number;

      IntBox(Integer number) {
        this.number = number;
      }

      @Override
      public int compareTo(IntBox o) {
        return number.compareTo(o.number);
      }
    }

    then(new IntBox(1)).isLessThan(new IntBox(2));
  }

  @Test
  public void then_Iterable() {
    Iterable<String> iterable = Arrays.asList("1");
    then(iterable).contains("1");
    then(iterable, StringAssert.class).first().startsWith("1");
    then(iterable, stringAssertFactory).first().startsWith("1");
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
    then(new double[] { 1d, 2d }).contains(2d);
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
    then(new float[] { 1f, 2f }).contains(2f);
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
    then(new long[] { 1L, 2L }).contains(2L);
  }

  @Test
  public void then_Object() {
    then(new Object()).isNotNull();
  }

  @Test
  public void then_Object_array() {
    then(new Object[] { new Object(), new Object() }).hasSize(2);
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
    then(new short[] { (short) 1, (short) 2 }).contains((short) 2);
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
    then(new boolean[] { true, false }).isEqualTo(new boolean[] { true, false });
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
    then(new byte[] { 10, 11 }).contains((byte) 11);
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
    then(new int[] { 2, 3 }).isEqualTo(new int[] { 2, 3 });
  }

  @Test
  public void then_List() {
    List<Integer> list = asList(5, 6);
    then(list).hasSize(2);
    then(list, IntegerAssert.class).first().isLessThan(10);
    then(list, integerAssertFactory).first().isLessThan(10);
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
    then(new HashMap<>()).isEmpty();
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown() {
    thenThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        throw new Throwable("something was wrong");
      }
    }).isInstanceOf(Throwable.class)
      .hasMessage("something was wrong");
  }

  public void should_build_ThrowableAssert_with_throwable_thrown_with_format_string() {
    thenThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        throw new Throwable("something was wrong");
      }
    }).isInstanceOf(Throwable.class)
            .hasMessage("something was %s", "wrong");
  }

  @Test
  public void then_URI() throws URISyntaxException {
    then(new URI("http://assertj.org")).hasNoPort();
  }

}
