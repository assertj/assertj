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
package org.assertj.core.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.assertj.core.condition.AnyOf;
import org.assertj.core.data.MapEntry;
import org.assertj.core.util.FilesException;
import org.junit.Test;

/**
 * Tests for <code>{@link WithAssertions}</code>, to verify that delegate calls happen.
 *
 * @author Alan Rothkopf
 */
public class WithAssertions_delegation_Test implements WithAssertions {

  private static final String VALUE_1 = "value1";
  private static final String KEY_1 = "key1";

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_offset_Float_Test() {
	this.assertThat(8.1f).isEqualTo(8.0f, this.offset(0.2f));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_offset_Double_Test() {
	this.assertThat(8.1).isEqualTo(8.0, this.offset(0.1));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_entry_MapEntry_Test() {
	MapEntry result = this.entry(KEY_1, VALUE_1);
	this.assertThat(result.key).isEqualTo(KEY_1);
	this.assertThat(result.value).isEqualTo(VALUE_1);
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
	  return this.name;
	}

	@SuppressWarnings("unused")
	public String getValue() {
	  return this.value;
	}

  }

  private static final TestItem[] ITEMS = { new TestItem("n1", "v1"), new TestItem("n2", "v2") };

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_filter_array_Test() {
	this.assertThat(this.filter(ITEMS).with("name").equalsTo("n1").get()).containsExactly(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_filter_iterable_Test() {
	this.assertThat(this.filter(Arrays.asList(ITEMS)).with("name").equalsTo("n1").get()).containsExactly(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test(expected = AssertionError.class)
  public void WithAssertions_fail_Test() {
	this.fail("Failed");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test(expected = AssertionError.class)
  public void WithAssertions_fail_with_throwable_Test() {
	this.fail("Failed", new RuntimeException("expected"));
  }

  private static final Condition<String> JEDI = new Condition<String>("jedi") {
	private final Set<String> jedis = new LinkedHashSet<String>(Arrays.asList("Luke", "Yoda", "Obiwan"));

	@Override
	public boolean matches(final String value) {
	  return this.jedis.contains(value);
	}
  };

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_not_Test() {
	this.assertThat("Solo").is(this.not(JEDI));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_object_Test() {
	this.assertThat(ITEMS[0]).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_Test() {
	this.assertThat(ITEMS[0]).isNotNull();
  }

  private static class TestAssertDelegate implements AssertDelegateTarget, WithAssertions {
	public void isOk() {
	  this.assertThat(Boolean.TRUE).isNotNull();
	}
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_AssertDelegateTarget_Test() {
	this.assertThat(new TestAssertDelegate()).isOk();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_object_array_Test() {
	this.assertThat(ITEMS).isNotEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_map_Test() {
	this.assertThat(new HashMap<String, String>()).isEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_list_Test() {
	this.assertThat(new ArrayList<String>()).isEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_long_Test() {
	this.assertThat(111L).isEqualTo(111L);
	this.assertThat(Long.valueOf(111L)).isEqualTo(Long.valueOf(111L));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_long_array_Test() {
	long[] testArray = new long[10];
	this.assertThat(testArray).hasSize(10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_string_Test() {
	this.assertThat("Hello world").startsWith("Hello");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_date_Test() {
	this.assertThat(new Date()).isAfter("2000-01-01");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_throwable_Test() {
	this.assertThat(new RuntimeException("test")).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_big_decimal_Test() {
	this.assertThat(new BigDecimal(100.22)).isGreaterThan(new BigDecimal(-100000));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_short_Test() {
	this.assertThat((short) 1).isLessThan((short) 2);
	this.assertThat(Short.valueOf("1")).isLessThan(Short.valueOf("2"));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_short_array_Test() {
	short[] testArray = new short[10];
	this.assertThat(testArray).hasSize(10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_char_sequence_Test() {
	this.assertThat((CharSequence) "Hello world").startsWith("Hello");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_char_Test() {
	this.assertThat('a').isLowerCase();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_char_array_Test() {
	char[] chars = { 'a', 'b' };
	this.assertThat(chars).containsOnlyOnce('a');
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_character_Test() {
	this.assertThat(Character.valueOf('a')).isLowerCase();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_class_Test() {
	this.assertThat(WithAssertions.class).isInterface();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_comparable_Test() {
	this.assertThat(new Comparable<String>() {
	  @Override
	  public int compareTo(final String o) {
		return 0;
	  }
	}).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_iterable_Test() {
	this.assertThat((Iterable<TestItem>) Arrays.asList(ITEMS)).contains(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_iterator_Test() {
	this.assertThat(Arrays.asList(ITEMS).iterator()).contains(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_boolean_Test() {
	this.assertThat(true).isTrue();
	this.assertThat(Boolean.TRUE).isTrue();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_boolean_array_Test() {
	this.assertThat(new boolean[10]).hasSize(10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_byte_Test() {
	this.assertThat((byte) 1).isGreaterThan((byte) 0);
	this.assertThat(Byte.valueOf((byte) 1)).isGreaterThan(Byte.valueOf((byte) 0));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_byte_array_Test() {
	this.assertThat("Hello".getBytes()).isNotEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_int_array_Test() {
	this.assertThat(new int[5]).hasSize(5);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_int_Test() {
	this.assertThat(10).isGreaterThan(-10);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_float_Test() {
	this.assertThat(10f).isGreaterThan(0.1f);
	this.assertThat(Float.valueOf(10f)).isGreaterThan(Float.valueOf(0.1f));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_integer_Test() {
	this.assertThat(Integer.valueOf(10)).isGreaterThan(Integer.valueOf(0));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_input_stream_Test() {
	this.assertThat(new BufferedInputStream(System.in)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_float_array_Test() {
	this.assertThat(new float[5]).isNotEmpty();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_double_Test() {
	this.assertThat(111.11).isGreaterThan(-111.11);
	this.assertThat(Double.valueOf(111.11)).isGreaterThan(Double.valueOf(-111.11));
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_file_Test() {
	this.assertThat(new File(".")).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_double_array_Test() {
	this.assertThat(new double[3]).hasSize(3);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_extractProperty_string_Test() {
	this.assertThat(this.extractProperty("name").from(ITEMS).contains("n1")).isTrue();
	this.assertThat(this.extractProperty("name", String.class).from(ITEMS).contains("n1")).isTrue();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_tuple_Test() {
	this.assertThat(this.tuple(ITEMS[0]).toArray()[0]).isEqualTo(ITEMS[0]);
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_atIndex_Test() {
	this.assertThat(this.atIndex(0)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_within_double_Test() {
	this.assertThat(this.within(Double.valueOf(111))).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_within_float_Test() {
	this.assertThat(this.within(Float.valueOf(111))).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_within_big_decimal_Test() {
	this.assertThat(this.within(BigDecimal.valueOf(111))).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_anyOf_iterable_Test() {
	this.assertThat(this.anyOf(new ArrayList<AnyOf<String>>())).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void WithAssertions_anyOf_condition_array_Test() {
	this.assertThat(this.anyOf(JEDI)).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_doesNotHave_condition_Test() {
	this.assertThat(this.doesNotHave(JEDI).matches("Solo")).isTrue();
  }



  /**
   * Test that the delegate method is called.
   */
  @Test(expected = FilesException.class)
  public void WithAssertions_contentOf_Test() {
	this.contentOf(new File("/non-existent file")).contains("a");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test(expected = FilesException.class)
  public void WithAssertions_contentOf_with_charset_Test() {
	this.contentOf(new File("/non-existent file", "UTF-8")).contains("a");
  }


  /**
   * Test that the delegate method is called.
   */
  @Test(expected = FilesException.class)
  public void WithAssertions_linesOf_Test() {
	this.linesOf(new File("/non-existent file")).contains("a");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test(expected = FilesException.class)
  public void WithAssertions_linesOf_with_charsetTest() {
	this.linesOf(new File("/non-existent file", "UTF-8")).contains("a");
  }

  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_allOf_iterable_Test() {
	this.assertThat(this.allOf(new ArrayList<AnyOf<String>>())).isNotNull();
  }

  /**
   * Test that the delegate method is called.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void WithAssertions_allOf_condition_array_Test() {
	this.assertThat(this.allOf(JEDI)).isNotNull();
  }


  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_setRemoveAssertJRelatedElementsFromStackTrace_Test() {
	this.setRemoveAssertJRelatedElementsFromStackTrace(true);
  }



  /**
   * Test that the delegate method is called.
   */
  @Test(expected = AssertionError.class)
  public void WithAssertions_failBecauseExceptionWasNotThrown_Test() {
	this.failBecauseExceptionWasNotThrown(Throwable.class);
  }


  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_setAllowExtractingPrivateFields_Test() {
	this.setAllowExtractingPrivateFields(false);
  }



  /**
   * Test that the delegate method is called without error.
   */
  @Test
  public void WithAssertions_registerCustomDateFormat_Test() {
	this.registerCustomDateFormat("YYYY-MMMM-dddd");
	this.registerCustomDateFormat(DateFormat.getInstance());
  }



  /**
   * Test that the delegate method is called without error.
   */
  @Test
  public void WithAssertions_useDefaultDateFormatsOnly_Test() {
	this.useDefaultDateFormatsOnly();
  }

  // TODO - uncomment after all classes have been moved to the branch
  //  /**
  //   * Test that the delegate method is called.
  //   */
  //  @Test
  //  public void WithAssertions_assertThat_zoned_date_time_Test() {
  //	this.assertThat(ZonedDateTime.now()).isAfter("2000-12-03T10:15:30+01:00");
  //  }
  //
  //
  //
  //  /**
  //   * Test that the delegate method is called.
  //   */
  //  @Test
  //  public void WithAssertions_assertThat_optional_Test() {
  //	this.assertThat(Optional.of("Not empty")).isPresent();
  //  }



  /**
   * Test that the delegate method is called.
   */
  @Test
  public void WithAssertions_assertThat_local_date_time_Test() {
	this.assertThat(LocalDateTime.now()).isNotNull();
  }


}
