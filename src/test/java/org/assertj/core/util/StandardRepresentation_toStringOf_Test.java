/*
 * Created on Sep 22, 2006
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2006-2012 the original author or authors.
 */
package org.assertj.core.util;

import static junit.framework.Assert.assertFalse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    assertNull(new StandardRepresentation().toStringOf(null));
  }

  @Test
  public void should_quote_String() {
    assertEquals("\"Hello\"", new StandardRepresentation().toStringOf("Hello"));
  }

  @Test
  public void should_quote_empty_String() {
    assertEquals("\"\"", new StandardRepresentation().toStringOf(""));
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
    assertEquals(path, new StandardRepresentation().toStringOf(o));
  }

  @Test
  public void should_return_toString_of_Class_with_its_name() {
    assertEquals("java.lang.Object", new StandardRepresentation().toStringOf(Object.class));
  }

  @Test
  public void should_return_toString_of_Collection_of_String() {
    Collection<String> collection = newArrayList("s1", "s2");
    assertEquals("[\"s1\", \"s2\"]", new StandardRepresentation().toStringOf(collection));
  }

  @Test
  public void should_return_toString_of_Collection_of_arrays() {
    List<Boolean[]> collection = newArrayList(array(true, false), array(true, false, true));
    assertEquals("[[true, false], [true, false, true]]", new StandardRepresentation().toStringOf(collection));
  }

  @Test
  public void should_return_toString_of_Collection_of_Collections() {
    Collection<List<String>> collection = new ArrayList<List<String>>();
    collection.add(newArrayList("s1", "s2"));
    collection.add(newArrayList("s3", "s4", "s5"));
    assertEquals("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]", new StandardRepresentation().toStringOf(collection));
  }

  @Test
  public void should_return_toString_of_Map() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertEquals("{\"key1\"=\"value1\", \"key2\"=\"value2\"}", new StandardRepresentation().toStringOf(map));
  }

  @Test
  public void should_return_toString_of_array() {
    assertEquals("[\"s1\", \"s2\"]", new StandardRepresentation().toStringOf(array("s1", "s2")));
  }

  @Test
  public void should_return_toString_of_array_of_arrays() {
    String[][] array = array(array("s1", "s2"), array("s3", "s4", "s5"));
    assertEquals("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]", new StandardRepresentation().toStringOf(array));
  }

  @Test
  public void should_return_toString_of_array_of_Class() {
    Class<?>[] array = { String.class, File.class };
    assertEquals("[java.lang.String, java.io.File]", new StandardRepresentation().toStringOf(array));
  }

  @Test
  public void should_return_toString_of_calendar() {
    GregorianCalendar calendar = new GregorianCalendar(2011, Calendar.JANUARY, 18, 23, 53, 17);
    assertEquals("2011-01-18T23:53:17", new StandardRepresentation().toStringOf(calendar));
  }

  @Test
  public void should_return_toString_of_date() {
    Date date = new GregorianCalendar(2011, Calendar.JUNE, 18, 23, 53, 17).getTime();
    assertEquals("2011-06-18T23:53:17", new StandardRepresentation().toStringOf(date));
  }

  @Test
  public void toString_with_anonymous_comparator() {
    Comparator<String> anonymousComparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    };
    assertEquals("'Anonymous Comparator class'", new StandardRepresentation().toStringOf(anonymousComparator));
  }

  @Test
  public void should_format_longs_and_integers() {
    assertFalse(new StandardRepresentation().toStringOf(20L).equals(toStringOf(20)));
    assertEquals("20", toStringOf(20));
    assertEquals("20L", toStringOf(20L));
  }

  @Test
  public void should_format_bytes_as_hex() {
    assertFalse(toStringOf((byte) 20).equals(toStringOf((char) 20)));
    assertEquals(toStringOf((byte) 20), (toStringOf((short) 20)));
    assertEquals("32", toStringOf((byte) 32));
  }

  @Test
  public void should_format_doubles_and_floats() {
    assertFalse(toStringOf(20.0f).equals(toStringOf(20.0)));
    assertEquals("20.0", toStringOf(20.0));
    assertEquals("20.0f", toStringOf(20.0f));
  }

  @Test
  public void should_format_tuples() {
    assertThat(toStringOf(tuple(1, 2, 3))).isEqualTo("(1, 2, 3)");
  }

  private String toStringOf(Object o) {
    return new StandardRepresentation().toStringOf(o);
  }
}
