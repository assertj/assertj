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
package org.assertj.core.presentation;

import static java.lang.String.format;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;

import org.assertj.core.data.MapEntry;
import org.assertj.core.util.OtherStringTestComparator;
import org.assertj.core.util.OtherStringTestComparatorWithAt;
import org.assertj.core.util.StringTestComparator;
import org.junit.Test;

/**
 * Tests for {@link org.assertj.core.presentation.StandardRepresentation#toStringOf(Object)}.
 *
 * @author Joel Costigliola
 */
public class StandardRepresentation_toStringOf_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  public void should_return_null_if_object_is_null() {
    assertThat(STANDARD_REPRESENTATION.toStringOf((Object) null)).isNull();
  }

  @Test
  public void should_quote_String() {
    assertThat(STANDARD_REPRESENTATION.toStringOf("Hello")).isEqualTo("\"Hello\"");
  }

  @Test
  public void should_quote_empty_String() {
    assertThat(STANDARD_REPRESENTATION.toStringOf("")).isEqualTo("\"\"");
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
    assertThat(STANDARD_REPRESENTATION.toStringOf(o)).isEqualTo(path);
  }

  @Test
  public void should_return_toString_of_Class_with_its_name() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(Object.class)).isEqualTo("java.lang.Object");
  }

  @Test
  public void should_return_toString_of_Collection_of_String() {
    Collection<String> collection = newArrayList("s1", "s2");
    // assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo(format("[\"s1\",%n" +
    // " \"s2\"]"));
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo(format("[\"s1\", \"s2\"]"));
  }

  @Test
  public void should_return_toString_of_Collection_of_arrays() {
    List<Boolean[]> collection = newArrayList(array(true, false), array(true, false, true));
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[true, false], [true, false, true]]");
  }

  @Test
  public void should_return_toString_of_Collection_of_arrays_up_to_the_maximum_allowed_elements() {
    List<Boolean[]> collection = newArrayList(array(true, false), array(true, false, true), array(true, true));
    StandardRepresentation.setMaxElementsForPrinting(2);
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[true, false], [true, false, ...], ...]");
  }

  @Test
  public void should_return_toString_of_Collection_of_Collections() {
    Collection<List<String>> collection = new ArrayList<>();
    collection.add(newArrayList("s1", "s2"));
    collection.add(newArrayList("s3", "s4", "s5"));
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  public void should_return_toString_of_Collection_of_Collections_up_to_the_maximum_allowed_elements() {
    Collection<List<String>> collection = new ArrayList<>();
    collection.add(newArrayList("s1", "s2"));
    collection.add(newArrayList("s3", "s4", "s5"));
    collection.add(newArrayList("s6", "s7"));
    StandardRepresentation.setMaxElementsForPrinting(2);
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection))
                                                              .isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", ...], ...]");
  }

  @Test
  public void should_return_toString_of_Map() {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{\"key1\"=\"value1\", \"key2\"=\"value2\"}");
  }

  @Test
  public void should_return_toString_of_array() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(array("s1", "s2"))).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  public void should_return_toString_of_array_of_arrays() {
    String[][] array = array(array("s1", "s2"), array("s3", "s4", "s5"));
    assertThat(STANDARD_REPRESENTATION.toStringOf(array)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  public void should_return_toString_of_array_of_arrays_up_to_the_maximum_allowed_elements() {
    String[][] array = array(array("s1", "s2"), array("s3", "s4", "s5"), array("s6", "s7"));
    StandardRepresentation.setMaxElementsForPrinting(2);
    assertThat(STANDARD_REPRESENTATION.toStringOf(array)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", ...], ...]");
  }

  @Test
  public void should_return_toString_of_array_of_Class() {
    Class<?>[] array = { String.class, File.class };
    assertThat(STANDARD_REPRESENTATION.toStringOf(array)).isEqualTo("[java.lang.String, java.io.File]");
  }

  @Test
  public void should_return_toString_of_calendar() {
    GregorianCalendar calendar = new GregorianCalendar(2011, Calendar.JANUARY, 18, 23, 53, 17);
    assertThat(STANDARD_REPRESENTATION.toStringOf(calendar)).isEqualTo("2011-01-18T23:53:17");
  }

  @Test
  public void should_return_toString_of_date() {
    Date date = new GregorianCalendar(2011, Calendar.JUNE, 18, 23, 53, 17).getTime();
    assertThat(STANDARD_REPRESENTATION.toStringOf(date)).isEqualTo("2011-06-18T23:53:17.000");
  }

  @Test
  public void should_return_toString_of_AtomicReference() {
    AtomicReference<String> atomicReference = new AtomicReference<>("actual");
    assertThat(STANDARD_REPRESENTATION.toStringOf(atomicReference)).isEqualTo("AtomicReference[\"actual\"]");
  }

  @Test
  public void should_return_toString_of_AtomicMarkableReference() {
    AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("actual", true);
    assertThat(STANDARD_REPRESENTATION.toStringOf(atomicMarkableReference)).isEqualTo("AtomicMarkableReference[marked=true, reference=\"actual\"]");
  }

  @Test
  public void should_return_toString_of_AtomicStampedReference() {
    AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("actual", 123);
    assertThat(STANDARD_REPRESENTATION.toStringOf(atomicStampedReference)).isEqualTo("AtomicStampedReference[stamp=123, reference=\"actual\"]");
  }

  @Test
  public void should_return_toString_of_AtomicIntegerFieldUpdater() {
    AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
    assertThat(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicIntegerFieldUpdater");
  }

  @Test
  public void should_return_toString_of_AtomicLongFieldUpdater() {
    AtomicLongFieldUpdater<Person> updater = AtomicLongFieldUpdater.newUpdater(Person.class, "account");
    assertThat(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicLongFieldUpdater");
  }

  @Test
  public void should_return_toString_of_AtomicReferenceFieldUpdater() {
    AtomicReferenceFieldUpdater<Person, String> updater = newUpdater(Person.class, String.class, "name");
    assertThat(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicReferenceFieldUpdater");
  }

  @Test
  public void toString_with_anonymous_comparator() {
    Comparator<String> anonymousComparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    };
    assertThat(STANDARD_REPRESENTATION.toStringOf(anonymousComparator)).isEqualTo("'anonymous comparator class'");
  }

  @Test
  public void toString_with_anonymous_comparator_overriding_toString() {
    Comparator<String> anonymousComparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }

      @Override
      public String toString() {
        return "foo";
      }
    };
    assertThat(STANDARD_REPRESENTATION.toStringOf(anonymousComparator)).isEqualTo("'foo'");
  }

  @Test
  public void toString_with_comparator_not_overriding_toString() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new StringTestComparator())).isEqualTo("'StringTestComparator'");
  }

  @Test
  public void toString_with_comparator_overriding_toString() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new OtherStringTestComparator())).isEqualTo("'other String comparator'");
  }

  @Test
  public void toString_with_comparator_overriding_toString_and_having_at() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new OtherStringTestComparatorWithAt())).isEqualTo("'other String comparator with @'");
  }

  @Test
  public void should_format_longs_and_integers() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(20L).equals(toStringOf(20))).isFalse();
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

  @Test
  public void should_format_tuples_up_to_the_maximum_allowed_elements() {
    StandardRepresentation.setMaxElementsForPrinting(2);
    assertThat(toStringOf(tuple(1, 2, 3))).isEqualTo("(1, 2, ...)");
  }

  @Test
  public void should_format_simple_date_format() {
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    assertThat(toStringOf(sdf)).isEqualTo("ddMMyyyy");
  }

  @Test
  public void should_format_assertj_map_entry() {
    MapEntry<String, Integer> entry = entry("A", 1);
    assertThat(toStringOf(entry)).isEqualTo("MapEntry[key=\"A\", value=1]");
  }

  @Test
  public void should_return_toStringOf_method() {
    Method method = null;
    for (Method m : GenericClass.class.getMethods()) {
      if (m.getName().equals("someGenericMethod")) {
        method = m;
        break;
      }
    }

    assertThat(method).as("someGenericMethod in GenericClass").isNotNull();
    assertThat(STANDARD_REPRESENTATION.toStringOf(method)).isEqualTo(method.toGenericString());
  }

  private String toStringOf(Object o) {
    return STANDARD_REPRESENTATION.toStringOf(o);
  }

  private static class Person {
    volatile String name;
    volatile int age;
    volatile long account;

    @Override
    public String toString() {
      return format("Person [name=%s, age=%s, account=%s]", name, age, account);
    }
  }

  private static class GenericClass<T> {

    @SuppressWarnings("unused")
    public <R extends Person> T someGenericMethod(R input, List<? extends R> list, T input2) {
      return input2;
    }
  }

}
