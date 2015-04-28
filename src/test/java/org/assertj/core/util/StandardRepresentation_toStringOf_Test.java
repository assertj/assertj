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

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for {@link org.assertj.core.presentation.StandardRepresentation#toStringOf(Object)}.
 *
 * @author Joel Costigliola
 */
public class StandardRepresentation_toStringOf_Test {

  @Test
  public void should_return_null_if_object_is_null() {
    assertThat(new StandardRepresentation().toStringOf(null)).isNull();
  }

  @Test
  public void should_quote_String() {
    assertThat(new StandardRepresentation().toStringOf("Hello")).isEqualTo("\"Hello\"");
  }

  @Test
  public void should_quote_empty_String() {
    assertThat(new StandardRepresentation().toStringOf("")).isEqualTo("\"\"");
  }

  @Test
  public void should_return_toString_of_File() {
    final String path = "/someFile.txt";
    @SuppressWarnings("serial")
    File o = new File(path) {
      @Override
      public String getAbsolutePath() {
        return path;
      }
    };
    assertThat(new StandardRepresentation().toStringOf(o)).isEqualTo(path);
  }

  @Test
  public void should_return_toString_of_Class_with_its_name() {
    assertThat(new StandardRepresentation().toStringOf(Object.class)).isEqualTo("java.lang.Object");
  }

  @Test
  public void should_return_toString_of_Collection_of_String() {
    Collection<String> collection = newArrayList("s1", "s2");
    assertThat(new StandardRepresentation().toStringOf(collection)).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  public void should_return_toString_of_Collection_of_arrays() {
    List<Boolean[]> collection = newArrayList(array(true, false), array(true, false, true));
    assertThat(new StandardRepresentation().toStringOf(collection)).isEqualTo("[[true, false], [true, false, true]]");
  }

  @Test
  public void should_return_toString_of_Collection_of_Collections() {
    Collection<List<String>> collection = new ArrayList<>();
    collection.add(newArrayList("s1", "s2"));
    collection.add(newArrayList("s3", "s4", "s5"));
    assertThat(new StandardRepresentation().toStringOf(collection)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  public void should_return_toString_of_Map() {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertThat(new StandardRepresentation().toStringOf(map)).isEqualTo("{\"key1\"=\"value1\", \"key2\"=\"value2\"}");
  }

  @Test
  public void should_return_toString_of_array() {
    assertThat( new StandardRepresentation().toStringOf(array("s1", "s2"))).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  public void should_return_toString_of_array_of_arrays() {
    String[][] array = array(array("s1", "s2"), array("s3", "s4", "s5"));
    assertThat(new StandardRepresentation().toStringOf(array)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  public void should_return_toString_of_array_of_Class() {
    Class<?>[] array = { String.class, File.class };
    assertThat(new StandardRepresentation().toStringOf(array)).isEqualTo("[java.lang.String, java.io.File]");
  }

  @Test
  public void should_return_toString_of_calendar() {
    GregorianCalendar calendar = new GregorianCalendar(2011, Calendar.JANUARY, 18, 23, 53, 17);
    assertThat(new StandardRepresentation().toStringOf(calendar)).isEqualTo("2011-01-18T23:53:17");
  }

  @Test
  public void should_return_toString_of_date() {
    Date date = new GregorianCalendar(2011, Calendar.JUNE, 18, 23, 53, 17).getTime();
    assertThat(new StandardRepresentation().toStringOf(date)).isEqualTo("2011-06-18T23:53:17");
  }

  @Test
  public void toString_with_anonymous_comparator() {
    Comparator<String> anonymousComparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    };
    assertThat(new StandardRepresentation().toStringOf(anonymousComparator)).isEqualTo("'anonymous comparator class'");
  }

  @Test
  public void should_format_longs_and_integers() {
    assertThat(new StandardRepresentation().toStringOf(20L).equals(toStringOf(20))).isFalse();
    assertThat(toStringOf(20)).isEqualTo("20");
    assertThat(toStringOf(20L)).isEqualTo("20L");
  }

  @Test
  public void should_format_bytes_as_hex() {
    assertThat(toStringOf((byte) 20).equals(toStringOf((char) 20))).isFalse();
    assertThat((toStringOf((short) 20))).isEqualTo(toStringOf((byte) 20));
    assertThat(toStringOf((byte) 32)).isEqualTo("32");
  }

  @Test
  public void should_format_doubles_and_floats() {
    assertThat(toStringOf(20.0f).equals(toStringOf(20.0))).isFalse();
    assertThat(toStringOf(20.0)).isEqualTo("20.0");
    assertThat(toStringOf(20.0f)).isEqualTo("20.0f");
  }

  @Test
  public void should_format_tuples() {
    assertThat(toStringOf(tuple(1, 2, 3))).isEqualTo("(1, 2, 3)");
  }

  private String toStringOf(Object o) {
    return new StandardRepresentation().toStringOf(o);
  }
}
