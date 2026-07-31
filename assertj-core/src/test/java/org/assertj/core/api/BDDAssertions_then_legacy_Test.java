/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

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
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.api.BDDAssertions.thenWith;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.util.Lists.list;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Mariusz Smykula
 */
class BDDAssertions_then_legacy_Test {

  private AssertFactory<String, StringAssert> stringAssertFactory = StringAssert::new;

  private AssertFactory<Integer, IntegerAssert> integerAssertFactory = IntegerAssert::new;

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
  void then_List() {
    List<Integer> list = list(5, 6);
    then(list).hasSize(2);
    then(list, IntegerAssert.class).first().isLessThan(10);
    then(list, integerAssertFactory).first().isLessThan(10);
    then(list).first(as(INTEGER)).isEqualTo(5);
    then(list(5)).singleElement(as(INTEGER)).isEqualTo(5);
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

  @SuppressWarnings("static-access")
  @Test
  void and_then() {
    and.then(true).isNotEqualTo(false);
    and.then(1L).isEqualTo(1L);
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
