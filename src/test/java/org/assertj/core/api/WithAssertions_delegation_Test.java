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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.mock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.data.MapEntry;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link WithAssertions}</code>, to verify that delegate calls happen.
 *
 * @author Alan Rothkopf
 */
public class WithAssertions_delegation_Test implements WithAssertions {

  private static final String VALUE_1 = "value1";
  private static final String KEY_1 = "key1";

  private static final Condition<String> JEDI = new Condition<String>("jedi") {
    private final Set<String> jedis = newLinkedHashSet("Luke", "Yoda", "Obiwan");

    @Override
    public boolean matches(final String value) {
      return jedis.contains(value);
    }
  };

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_offset_Float_Test() {
    assertThat(8.1f).isEqualTo(8.0f, offset(0.2f));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_offset_Double_Test() {
    assertThat(8.1).isEqualTo(8.0, offset(0.1));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_entry_MapEntry_Test() {
    MapEntry<String, String> result = entry(KEY_1, VALUE_1);
    assertThat(result.key).isEqualTo(KEY_1);
    assertThat(result.value).isEqualTo(VALUE_1);
  }

  /**
   *
   * Simple data object class for ObjectAssertion tests
   *
   */
  private static final class TestItem {
    private final String name;
    private final String value;

    public TestItem(final String name, final String value) {
      super();
      this.name = name;
      this.value = value;
    }

    @SuppressWarnings("unused")
    public String getName() {
      return name;
    }

    @SuppressWarnings("unused")
    public String getValue() {
      return value;
    }

  }

  private static final TestItem[] ITEMS = { new TestItem("n1", "v1"), new TestItem("n2", "v2") };

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_filter_array_Test() {
    assertThat(filter(ITEMS).with("name").equalsTo("n1").get()).containsExactly(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_filter_iterable_Test() {
    assertThat(filter(Arrays.asList(ITEMS)).with("name").equalsTo("n1").get()).containsExactly(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_fail_Test() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> fail("Failed"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_fail_with_throwable_Test() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> fail("Failed", new RuntimeException("expected")));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_not_Test() {
    assertThat("Solo").is(not(JEDI));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_object_Test() {
    assertThat(ITEMS[0]).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_Test() {
    assertThat(ITEMS[0]).isNotNull();
  }

  private static class TestAssertDelegate implements AssertDelegateTarget, WithAssertions {
    public void isOk() {
      assertThat(Boolean.TRUE).isNotNull();
    }
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_AssertDelegateTarget_Test() {
    assertThat(new TestAssertDelegate()).isOk();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_object_array_Test() {
    assertThat(ITEMS).isNotEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_map_Test() {
    assertThat(new HashMap<>()).isEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_list_Test() {
    assertThat(new ArrayList<>()).isEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void withAssertions_assertThat_list_assert_class_Test() {
    assertThat(Arrays.asList(ITEMS), ObjectAssert.class).first().isEqualTo(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_list_assert_factory_Test() {
    assertThat(Arrays.asList(ITEMS), ObjectAssert::new).first().isEqualTo(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_stream_Test() {
    assertThat(Stream.of("")).hasSize(1);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_long_Test() {
    assertThat(111L).isEqualTo(111L);
    assertThat(Long.valueOf(111L)).isEqualTo(Long.valueOf(111L));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_long_array_Test() {
    long[] testArray = new long[10];
    assertThat(testArray).hasSize(10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_string_Test() {
    assertThat("Hello world").startsWith("Hello")
                             .isLessThanOrEqualTo("Hi World");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_date_Test() {
    assertThat(new Date()).isAfter("2000-01-01");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_throwable_Test() {
    assertThat(new RuntimeException("test")).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_big_decimal_Test() {
    assertThat(new BigDecimal(100.22)).isGreaterThan(new BigDecimal(-100000));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_short_Test() {
    assertThat((short) 1).isLessThan((short) 2);
    assertThat(Short.valueOf("1")).isLessThan(Short.valueOf("2"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_short_array_Test() {
    short[] testArray = new short[10];
    assertThat(testArray).hasSize(10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_char_sequence_Test() {
    assertThat((CharSequence) "Hello world").startsWith("Hello");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_char_Test() {
    assertThat('a').isLowerCase();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_char_array_Test() {
    char[] chars = { 'a', 'b' };
    assertThat(chars).containsOnlyOnce('a');
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_character_Test() {
    assertThat(Character.valueOf('a')).isLowerCase();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_class_Test() {
    assertThat(WithAssertions.class).isInterface();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_comparable_Test() {
    assertThat((Comparable<String>) o -> 0).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_iterable_Test() {
    assertThat((Iterable<TestItem>) Arrays.asList(ITEMS)).contains(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void withAssertions_assertThat_iterable_assert_class_Test() {
    assertThat((Iterable<TestItem>) Arrays.asList(ITEMS), ObjectAssert.class).first().isEqualTo(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_iterable_assert_factory_Test() {
    assertThat((Iterable<TestItem>) Arrays.asList(ITEMS), ObjectAssert::new).first().isEqualTo(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_iterator_Test() {
    assertThat(Lists.list(ITEMS).iterator()).hasNext();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_boolean_Test() {
    assertThat(true).isTrue();
    assertThat(Boolean.TRUE).isTrue();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_boolean_array_Test() {
    assertThat(new boolean[10]).hasSize(10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_byte_Test() {
    assertThat((byte) 1).isGreaterThan((byte) 0);
    assertThat(Byte.valueOf((byte) 1)).isGreaterThan(Byte.valueOf((byte) 0));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_byte_array_Test() {
    assertThat("Hello".getBytes()).isNotEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_int_array_Test() {
    assertThat(new int[5]).hasSize(5);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_int_Test() {
    assertThat(10).isGreaterThan(-10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_float_Test() {
    assertThat(10f).isGreaterThan(0.1f);
    assertThat(Float.valueOf(10f)).isGreaterThan(Float.valueOf(0.1f));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_integer_Test() {
    assertThat(Integer.valueOf(10)).isGreaterThan(Integer.valueOf(0));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_input_stream_Test() {
    assertThat(new BufferedInputStream(System.in)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_float_array_Test() {
    assertThat(new float[5]).isNotEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_double_Test() {
    assertThat(111.11).isGreaterThan(-111.11);
    assertThat(Double.valueOf(111.11)).isGreaterThan(Double.valueOf(-111.11));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_file_Test() {
    assertThat(new File(".")).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_path_Test() {
    assertThat(Paths.get(".")).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_double_array_Test() {
    assertThat(new double[3]).hasSize(3);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_extractProperty_string_Test() {
    assertThat(extractProperty("name").from(ITEMS).contains("n1")).isTrue();
    assertThat(extractProperty("name", String.class).from(ITEMS).contains("n1")).isTrue();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_tuple_Test() {
    assertThat(tuple(ITEMS[0]).toArray()[0]).isEqualTo(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_atIndex_Test() {
    assertThat(atIndex(0)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_within_double_Test() {
    assertThat(within(Double.valueOf(111))).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_within_float_Test() {
    assertThat(within(Float.valueOf(111))).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_within_big_decimal_Test() {
    assertThat(within(BigDecimal.valueOf(111))).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_anyOf_iterable_Test() {
    assertThat(anyOf(new ArrayList<>())).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void withAssertions_anyOf_condition_array_Test() {
    assertThat(anyOf(JEDI)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_doesNotHave_condition_Test() {
    assertThat(doesNotHave(JEDI).matches("Solo")).isTrue();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_contentOf_Test() {
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> contentOf(new File("/non-existent file")).contains("a"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_contentOf_with_charset_Test() {
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> contentOf(new File("/non-existent file",
                                                                                              "UTF-8")).contains("a"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_linesOf_Test() {
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> linesOf(new File("/non-existent file")).contains("a"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_linesOf_with_charsetTest() {
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> linesOf(new File("/non-existent file",
                                                                                            "UTF-8")).contains("a"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_allOf_iterable_Test() {
    assertThat(allOf(new ArrayList<>())).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void withAssertions_allOf_condition_array_Test() {
    assertThat(allOf(JEDI)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_setRemoveAssertJRelatedElementsFromStackTrace_Test() {
    setRemoveAssertJRelatedElementsFromStackTrace(true);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_failBecauseExceptionWasNotThrown_Test() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> failBecauseExceptionWasNotThrown(Throwable.class));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_setAllowExtractingPrivateFields_Test() {
    setAllowExtractingPrivateFields(false);
    // reset to default
    setAllowExtractingPrivateFields(true);
  }

  /**
   * Test that the delegate method is called without error.
   */
  @Test
  public void withAssertions_registerCustomDateFormat_Test() {
    registerCustomDateFormat("YYYY-MMMM-dddd");
    registerCustomDateFormat(DateFormat.getInstance());
  }

  /**
   * Test that the delegate method is called without error.
   */
  @Test
  public void withAssertions_useDefaultDateFormatsOnly_Test() {
    useDefaultDateFormatsOnly();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_zoned_date_time_Test() {
    assertThat(ZonedDateTime.now()).isAfter("2000-12-03T10:15:30+01:00");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_optional_Test() {
    assertThat(Optional.of("Not empty")).isPresent();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_optionalDouble_Test() {
    assertThat(OptionalDouble.of(1.0)).isPresent();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_optionalLong_Test() {
    assertThat(OptionalLong.of(1L)).isPresent();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_optionalInt_Test() {
    assertThat(OptionalInt.of(1)).isPresent();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_local_date_time_Test() {
    assertThat(LocalDateTime.now()).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_local_date_Test() {
    assertThat(LocalDate.now()).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThat_offset_date_time_Test() {
    assertThat(OffsetDateTime.now()).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_assertThatThrownBy_Test() {
    assertThatThrownBy(() -> {
      throw new IOException("message");
    }).hasMessage("message");

    assertThatThrownBy(() -> {
      throw new IOException("message");
    }, "Test").hasMessage("message");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void withAssertions_catchThrowable_Test() {
    Throwable t = catchThrowable(() -> {
      throw new IOException("message");
    });
    assertThat(t).hasMessage("message");
  }

  @Test
  public void withAssertions_assertThat_predicate_Test() {
    Predicate<Boolean> predicate = b -> b;
    assertThat(predicate).accepts(true);
  }

  @Test
  public void withAssertions_assertThat_intPredicate_Test() {
    IntPredicate predicate = i -> i == 0;
    assertThat(predicate).accepts(0);
  }

  @Test
  public void withAssertions_assertThat_longPredicate_Test() {
    LongPredicate predicate = l -> l == 0;
    assertThat(predicate).accepts(0L);
  }

  @Test
  public void withAssertions_assertThat_doublePredicate_Test() {
    DoublePredicate predicate = d -> d > 0;
    assertThat(predicate).accepts(1.0);
  }

  @Test
  public void withAssertions_assertThat_url_Test() throws MalformedURLException {
    assertThat(new URL("https://github.com/joel-costigliola/assertj-core")).hasHost("github.com");
  }

  @Test
  public void withAssertions_assertThat_uri_Test() {
    assertThat(java.net.URI.create("https://github.com/joel-costigliola/assertj-core")).hasHost("github.com");
  }

  @Test
  void withAssertions_assertThat_spliterator_Test() {
    assertThat(Stream.of(1, 2).spliterator()).hasCharacteristics(Spliterator.SIZED);
  }

  @Test
  void withAssertions_from_function_Test() {
    // GIVEN
    Function<?, ?> extractor = mock(Function.class);
    // WHEN
    Function<?, ?> result = from(extractor);
    // THEN
    then(result).isSameAs(extractor);
  }

  @Test
  @SuppressWarnings("unchecked")
  void withAssertions_as_instanceOfAssertFactory_Test() {
    // GIVEN
    InstanceOfAssertFactory<?, AbstractAssert<?, ?>> assertFactory = mock(InstanceOfAssertFactory.class);
    // WHEN
    InstanceOfAssertFactory<?, AbstractAssert<?, ?>> result = as(assertFactory);
    // THEN
    then(result).isSameAs(assertFactory);
  }

}
