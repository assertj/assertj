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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.and;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.assertj.core.api.BDDAssertions.thenIOException;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.thenIllegalStateException;
import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.api.BDDAssertions.thenObject;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.api.BDDAssertions.thenWith;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.util.Lists.list;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BDDAssertions#then(String)}</code>.
 *
 * @author Mariusz Smykula
 */
class BDDAssertions_then_Test {

  private AssertFactory<String, StringAssert> stringAssertFactory = StringAssert::new;

  private AssertFactory<Integer, IntegerAssert> integerAssertFactory = IntegerAssert::new;

  @Test
  void then_char() {
    then('z').isGreaterThan('a');
  }

  @Test
  void then_Character() {
    then(Character.valueOf('A')).isEqualTo(Character.valueOf('A'));
  }

  @Test
  void then_char_array() {
    then(new char[] { 'a', 'b', 'c' }).contains('b');
  }

  @Test
  void then_CharSequence() {
    then("abc".subSequence(0, 1)).contains("a");
  }

  @Test
  void then_Class() {
    then("Foo".getClass()).isEqualTo(String.class);
  }

  @Test
  void then_ClassLoader_succeeds() {
    then("Foo".getClass().getClassLoader()).isEqualTo(String.class.getClassLoader());
  }

  @Test
  void should_delegate_to_assert_comparable() {

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
  void then_Iterable() {
    Iterable<String> iterable = Arrays.asList("1");
    then(iterable).contains("1");
    then(iterable, StringAssert.class).first().startsWith("1");
    then(iterable, stringAssertFactory).first().startsWith("1");
    then(iterable).first(as(STRING)).startsWith("1");
    then(iterable).singleElement(as(STRING)).startsWith("1");
  }

  @Test
  void then_Iterator() {
    Iterator<String> iterator = singletonList("1").iterator();
    then(iterator).hasNext();
  }

  @Test
  void then_double() {
    then(1d).isNotZero();
  }

  @Test
  void then_Double() {
    then(Double.valueOf(1d)).isNotZero();
  }

  @Test
  void then_double_array() {
    then(new double[] { 1d, 2d }).contains(2d);
  }

  @Test
  void then_float() {
    then(1f).isEqualTo(1f);
  }

  @Test
  void then_Float() {
    then(Float.valueOf(1f)).isEqualTo(1f);
  }

  @Test
  void then_float_array() {
    then(new float[] { 1f, 2f }).contains(2f);
  }

  @Test
  void then_long() {
    then(1L).isEqualTo(1L);
  }

  @Test
  void then_Long() {
    then(Long.valueOf(1L)).isEqualTo(1L);
  }

  @Test
  void then_long_array() {
    then(new long[] { 1L, 2L }).contains(2L);
  }

  @Test
  void then_Object() {
    then(new Object()).isNotNull();
  }

  @Test
  void then_Object_array() {
    then(new Object[] { new Object(), new Object() }).hasSize(2);
  }

  @Test
  void then_short() {
    then((short) 1).isEqualTo((short) 1);
  }

  @Test
  void then_Short() {
    then(Short.valueOf("1")).isEqualTo((short) 1);
  }

  @Test
  void then_short_array() {
    then(new short[] { (short) 1, (short) 2 }).contains((short) 2);
  }

  @Test
  void then_Throwable() {
    then(new IllegalArgumentException("Foo")).hasMessage("Foo");
  }

  @Test
  void then_BigDecimal() {
    then(BigDecimal.ONE).isEqualTo(BigDecimal.valueOf(1));
  }

  @Test
  void then_boolean() {
    then(true).isEqualTo(Boolean.TRUE);
  }

  @Test
  void then_Boolean() {
    then(Boolean.TRUE).isEqualTo(true);
  }

  @Test
  void then_boolean_array() {
    then(new boolean[] { true, false }).isEqualTo(new boolean[] { true, false });
  }

  @Test
  void then_byte() {
    then((byte) 7).isEqualTo((byte) 0x07);
  }

  @Test
  void then_Byte() {
    then(Byte.valueOf((byte) 8)).isEqualTo((byte) 0x08);
  }

  @Test
  void then_byte_array() {
    then(new byte[] { 10, 11 }).contains((byte) 11);
  }

  @Test
  void then_int() {
    then(1).isEqualTo(1);
  }

  @Test
  void then_Integer() {
    then(Integer.valueOf(4)).isEqualTo(4);
  }

  @Test
  void then_BigInteger() {
    then(BigInteger.valueOf(4)).isEqualTo(4);
  }

  @Test
  void then_int_array() {
    then(new int[] { 2, 3 }).isEqualTo(new int[] { 2, 3 });
  }

  @Test
  void then_List() {
    List<Integer> list = list(5, 6);
    then(list).hasSize(2);
    then(list, IntegerAssert.class).first().isLessThan(10);
    then(list, integerAssertFactory).first().isLessThan(10);
    then(list).first(as(INTEGER)).isEqualTo(5);
    then(list(5)).singleElement(as(INTEGER)).isEqualTo(5);
  }

  @Test
  void then_String() {
    then("Foo").isEqualTo("Foo").isGreaterThan("Bar");
  }

  @Test
  void then_Date() {
    then(new Date()).isNotNull();
  }

  @Test
  void then_Map() {
    then(new HashMap<>()).isEmpty();
  }

  @Test
  void should_build_ThrowableAssert_with_throwable_thrown() {
    thenThrownBy(() -> {
      throw new Throwable("something was wrong");
    }).isInstanceOf(Throwable.class)
      .hasMessage("something was wrong");
  }

  @Test
  void should_build_ThrowableAssert_with_throwable_thrown_with_format_string() {
    thenThrownBy(() -> {
      throw new Throwable("something was wrong");
    }).isInstanceOf(Throwable.class)
      .hasMessage("something was %s", "wrong");
  }

  @Test
  void then_explicit_Object() {
    thenObject(new LinkedList<>()).matches(l -> l.peek() == null);
  }

  @Test
  void then_with() {
    thenWith("foo", string -> assertThat(string).startsWith("f"));
  }

  @Test
  void then_with_multiple_requirements() {
    thenWith("foo",
             string -> assertThat(string).startsWith("f"),
             string -> assertThat(string).endsWith("o"));
  }

  @Test
  void then_URI() {
    then(URI.create("http://assertj.org")).hasNoPort();
  }

  @Test
  void then_Optional() {
    then(Optional.of("foo")).hasValue("foo");
  }

  @Test
  void then_OptionalInt() {
    then(OptionalInt.of(1)).hasValue(1);
  }

  @Test
  void then_OptionalDouble() {
    then(OptionalDouble.of(1)).hasValue(1);
  }

  @Test
  void then_Predicate() {
    Predicate<String> actual = String::isEmpty;
    then(actual).accepts("");
  }

  @Test
  void then_IntPredicate() {
    IntPredicate predicate = val -> val <= 2;
    then(predicate).accepts(1);
  }

  @Test
  void then_LongPredicate() {
    LongPredicate predicate = val -> val <= 2;
    then(predicate).accepts(1);
  }

  @Test
  void then_DoublePredicate() {
    DoublePredicate predicate = val -> val <= 2;
    then(predicate).accepts(1);
  }

  @Test
  void then_OptionalLong() {
    then(OptionalLong.of(1)).hasValue(1);
  }

  @Test
  void then_Spliterator() {
    Spliterator<Integer> spliterator = Stream.of(1, 2).spliterator();
    then(spliterator).hasCharacteristics(Spliterator.SIZED);
  }

  @Test
  void then_Duration() {
    then(Duration.ofHours(1)).isNotNull().isPositive();
  }

  @SuppressWarnings("static-access")
  @Test
  void and_then() {
    and.then(true).isNotEqualTo(false);
    and.then(1L).isEqualTo(1L);
  }

  @Test
  void should_build_ThrowableTypeAssert_with_throwable_thrown() {
    thenExceptionOfType(Throwable.class).isThrownBy(() -> methodThrowing(new Throwable("boom")))
                                        .withMessage("boom");
  }

  @Test
  void should_build_NotThrownAssert_with_throwable_not_thrown() {
    thenNoException().isThrownBy(() -> methodNotThrowing());
  }

  @Test
  void should_build_ThrowableTypeAssert_with_NullPointerException_thrown() {
    thenNullPointerException().isThrownBy(() -> methodThrowing(new NullPointerException("something was wrong")))
                              .withMessage("something was wrong");
  }

  @Test
  void should_build_ThrowableTypeAssert_with_IllegalArgumentException_thrown() {
    thenIllegalArgumentException().isThrownBy(() -> methodThrowing(new IllegalArgumentException("something was wrong")))
                                  .withMessage("something was wrong");
  }

  @Test
  void should_build_ThrowableTypeAssert_with_IllegalStateException_thrown() {
    thenIllegalStateException().isThrownBy(() -> methodThrowing(new IllegalStateException("something was wrong")))
                               .withMessage("something was wrong");
  }

  @Test
  void should_build_ThrowableTypeAssert_with_IOException_thrown() {
    thenIOException().isThrownBy(() -> methodThrowing(new IOException("something was wrong")))
                     .withMessage("something was wrong");
  }

  private static void methodThrowing(Throwable throwable) throws Throwable {
    throw throwable;
  }

  private static void methodNotThrowing() {}

}
