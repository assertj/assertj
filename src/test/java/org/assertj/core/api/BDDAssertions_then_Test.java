/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for <code>{@link org.assertj.core.api.BDDAssertions#then(String)}</code>.
 *
 * @author Mariusz Smykula
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Assertions.class)
public class BDDAssertions_then_Test {

  @Before
  public final void setUp() {
    mockStatic(Assertions.class);
  }

  @Test
  public void then_of_char_should_delegate_to_assertThat() {
    // GIVEN
    char actual = 'z';
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Character_should_delegate_to_assertThat() {
    // GIVEN
    Character actual = new Character('z');
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_char_array_should_delegate_to_assertThat() {
    // GIVEN
    char[] actual = new char[] { 'a', 'b', 'c' };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Charsequence_should_delegate_to_assertThat() {
    // GIVEN
    CharSequence actual = "abc".subSequence(0, 1);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Class_should_delegate_to_assertThat() {
    // GIVEN
    Class<? extends String> actual = "Foo".getClass();
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_double_should_delegate_to_assertThat() {
    // GIVEN
    double actual = 1d;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Double_should_delegate_to_assertThat() {
    // GIVEN
    Double actual = 1d;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_double_array_should_delegate_to_assertThat() {
    // GIVEN
    double[] actual = new double[] { 1d, 2d };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_float_should_delegate_to_assertThat() {
    // GIVEN
    float actual = 1f;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Float_should_delegate_to_assertThat() {
    // GIVEN
    Float actual = 1f;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_float_array_should_delegate_to_assertThat() {
    // GIVEN
    float[] actual = new float[] { 1f, 2f };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_long_should_delegate_to_assertThat() {
    // GIVEN
    long actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Long_should_delegate_to_assertThat() {
    // GIVEN
    Long actual = 1L;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_long_array_should_delegate_to_assertThat() {
    // GIVEN
    long[] actual = new long[] { 1, 2 };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Object_should_delegate_to_assertThat() {
    // GIVEN
    Object actual = new Object();
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Object_array_should_delegate_to_assertThat() {
    // GIVEN
    Object[] actual = new Object[] { new Object(), new Object() };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_short_should_delegate_to_assertThat() {
    // GIVEN
    short actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Short_should_delegate_to_assertThat() {
    // GIVEN
    Short actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_short_array_should_delegate_to_assertThat() {
    // GIVEN
    short[] actual = new short[] { 1, 2 };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Throwable_should_delegate_to_assertThat() {
    // GIVEN
    IllegalArgumentException actual = new IllegalArgumentException("Foo");
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void thenExceptionThrownBy_of_Callable_should_delegate_to_assertThatExceptionOfTypeThrownBy() {
    // GIVEN
    ThrowingCallable actual = () -> {
      throw new Exception("Boom !");
    };
    // WHEN
    thenThrownBy(actual);
    // THEN
    verifyStatic();
    assertThatThrownBy(actual);
  }

  @Test
  public void then_of_Optional_should_delegate_to_assertThat() {
    // GIVEN
    Optional<String> actual = Optional.of("Foo");
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_OptionalDouble_should_delegate_to_assertThat() {
    // GIVEN
    OptionalDouble actual = OptionalDouble.of(10.0);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_OptionalInt_should_delegate_to_assertThat() {
    // GIVEN
    OptionalInt actual = OptionalInt.of(10);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_OptionalLong_should_delegate_to_assertThat() {
    // GIVEN
    OptionalLong actual = OptionalLong.of(10l);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_BigDecimal_should_delegate_to_assertThat() {
    // GIVEN
    BigDecimal actual = BigDecimal.ONE;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_boolean_should_delegate_to_assertThat() {
    // GIVEN
    boolean actual = true;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Boolean_should_delegate_to_assertThat() {
    // GIVEN
    Boolean actual = true;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_boolean_array_should_delegate_to_assertThat() {
    // GIVEN
    boolean[] actual = new boolean[] { true, false };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_byte_should_delegate_to_assertThat() {
    // GIVEN
    byte actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Byte_should_delegate_to_assertThat() {
    // GIVEN
    Byte actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_byte_array_should_delegate_to_assertThat() {
    // GIVEN
    byte[] actual = new byte[] { 1, 2 };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_int_should_delegate_to_assertThat() {
    // GIVEN
    int actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Integer_should_delegate_to_assertThat() {
    // GIVEN
    Integer actual = 1;
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_int_array_should_delegate_to_assertThat() {
    // GIVEN
    int[] actual = new int[] { 1, 2 };
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_String_should_delegate_to_assertThat() {
    // GIVEN
    String actual = "foo";
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Date_should_delegate_to_assertThat() {
    // GIVEN
    Date actual = new Date();
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_Map_should_delegate_to_assertThat() {
    // GIVEN
    Map<String, String> actual = new HashMap<String, String>();
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_LocalDate_should_delegate_to_assertThat() {
    // GIVEN
    LocalDate actual = LocalDate.of(2015, 1, 1);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_LocalDateTime_should_delegate_to_assertThat() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2015, 1, 1, 23, 59, 59);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_ZonedDateTime_should_delegate_to_assertThat() {
    // GIVEN
    ZonedDateTime actual = ZonedDateTime.of(2015, 1, 1, 23, 59, 59, 0, UTC);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_LocalTime_should_delegate_to_assertThat() {
    // GIVEN
    LocalTime actual = LocalTime.of(23, 59, 59);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_OffsetTime_should_delegate_to_assertThat() {
    // GIVEN
    OffsetTime actual = OffsetTime.of(23, 59, 59, 0, ZoneOffset.UTC);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  public void then_of_OffsetDateTime_should_delegate_to_assertThat() {
    // GIVEN
    OffsetDateTime actual = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC);
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

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
  public void then_of_URI_should_delegate_to_assertThat() throws URISyntaxException {
    // GIVEN
    URI actual = new URI("http://assertj.org");
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_URL_should_delegate_to_assertThat() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("http://assertj.org");
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

  @Test
  public void then_of_CompletableFuture_should_delegate_to_assertThat() {
    // GIVEN
    CompletableFuture<String> actual = CompletableFuture.completedFuture("done");
    // WHEN
    then(actual);
    // THEN
    verifyStatic();
    assertThat(actual);
  }

}
