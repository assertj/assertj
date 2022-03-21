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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.stream.Stream;

import org.assertj.core.data.MapEntry;
import org.assertj.core.util.OtherStringTestComparator;
import org.assertj.core.util.OtherStringTestComparatorWithAt;
import org.assertj.core.util.StringTestComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Joel Costigliola
 */
class StandardRepresentation_toStringOf_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  void should_return_null_if_object_is_null() {
    assertThat(STANDARD_REPRESENTATION.toStringOf((Object) null)).isNull();
  }

  @Test
  void should_quote_String() {
    assertThat(STANDARD_REPRESENTATION.toStringOf("Hello")).isEqualTo("\"Hello\"");
  }

  @Test
  void should_quote_empty_String() {
    assertThat(STANDARD_REPRESENTATION.toStringOf("")).isEqualTo("\"\"");
  }

  @Test
  void should_return_toString_of_File() {
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
  void should_return_toString_of_Path() {
    final Path path = Paths.get("someFile.txt");
    assertThat(STANDARD_REPRESENTATION.toStringOf(path)).isEqualTo("someFile.txt");
  }

  @Test
  void should_return_toString_of_Class_with_its_name() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(Object.class)).isEqualTo("java.lang.Object");
  }

  @Test
  void should_return_toString_of_anonymous_Class_with_generic_description() {
    Object anonymous = new Object() {};
    assertThat(STANDARD_REPRESENTATION.toStringOf(anonymous.getClass())).isEqualTo("anonymous class");
  }

  @Test
  void should_return_toString_of_anonymous_Class_array_with_generic_description() {
    Object anonymous = new Object() {};
    Object anonymousArray = Array.newInstance(anonymous.getClass(), 1);
    assertThat(STANDARD_REPRESENTATION.toStringOf(anonymousArray.getClass())).isEqualTo("anonymous class array");
  }

  @Test
  void should_return_toString_of_local_Class_with_its_simple_name() {
    class LocalClass {
    }
    assertThat(STANDARD_REPRESENTATION.toStringOf(LocalClass.class)).isEqualTo("local class LocalClass");
  }

  @Test
  void should_return_toString_of_local_Class_array_with_its_simple_name() {
    class LocalClass {
    }
    assertThat(STANDARD_REPRESENTATION.toStringOf(LocalClass[].class)).isEqualTo("local class LocalClass[]");
  }

  @Test
  void should_return_toString_of_Collection_of_String() {
    Collection<String> collection = list("s1", "s2");
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  void should_return_toString_of_Collection_of_arrays() {
    List<Boolean[]> collection = list(array(true, false), array(true, false, true));
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[true, false], [true, false, true]]");
  }

  @Test
  void should_return_toString_of_Collection_of_arrays_up_to_the_maximum_allowed_elements() {
    List<Boolean[]> collection = list(array(true),
                                      array(true, false, true, false, true),
                                      array(true, true),
                                      array(true),
                                      array(true));
    StandardRepresentation.setMaxElementsForPrinting(4);
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[true], [true, false, ... false, true], ... [true], [true]]");
  }

  @Test
  void should_return_toString_of_Collection_of_Collections() {
    Collection<List<String>> collection = list(list("s1", "s2"), list("s3", "s4", "s5"));
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  void should_return_toString_of_Collection_of_Collections_up_to_the_maximum_allowed_elements() {
    Collection<List<String>> collection = list(list("s1"),
                                               list("s2", "s3", "s4", "s5", "s6"),
                                               list("s7", "s8"),
                                               list("s9"),
                                               list("s10"));
    StandardRepresentation.setMaxElementsForPrinting(4);
    assertThat(STANDARD_REPRESENTATION.toStringOf(collection)).isEqualTo("[[\"s1\"], [\"s2\", \"s3\", ... \"s5\", \"s6\"], ... [\"s9\"], [\"s10\"]]");
  }

  @Test
  void should_return_toString_of_Map() {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertThat(STANDARD_REPRESENTATION.toStringOf(map)).isEqualTo("{\"key1\"=\"value1\", \"key2\"=\"value2\"}");
  }

  @Test
  void should_return_toString_of_array() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(array("s1", "s2"))).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  void should_return_toString_of_array_of_arrays() {
    String[][] array = array(array("s1", "s2"), array("s3", "s4", "s5"));
    assertThat(STANDARD_REPRESENTATION.toStringOf(array)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  void should_return_toString_of_array_of_arrays_up_to_the_maximum_allowed_elements() {
    String[][] array = array(array("s1", "s2"),
                             array("s3", "s4", "s5", "s6", "s7"),
                             array("s8"),
                             array("s9"),
                             array("s10"));
    StandardRepresentation.setMaxElementsForPrinting(4);
    assertThat(STANDARD_REPRESENTATION.toStringOf(array)).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", ... \"s6\", \"s7\"], ... [\"s9\"], [\"s10\"]]");
  }

  @Test
  void should_return_toString_of_array_of_Class() {
    Class<?>[] array = { String.class, File.class };
    assertThat(STANDARD_REPRESENTATION.toStringOf(array)).isEqualTo("[java.lang.String, java.io.File]");
  }

  @Test
  void should_return_unambiguous_toString_of_calendar() {
    // GIVEN
    GregorianCalendar calendar = new GregorianCalendar(2011, Calendar.JANUARY, 18, 23, 53, 17);
    // WHEN
    String stringOfCalendar = STANDARD_REPRESENTATION.toStringOf(calendar);
    // THEN
    then(stringOfCalendar).isEqualTo("2011-01-18T23:53:17 (java.util.GregorianCalendar)");
  }

  @Test
  void should_return_unambiguous_toString_of_date() {
    // GIVEN
    Date date = new GregorianCalendar(2011, Calendar.JUNE, 18, 23, 53, 17).getTime();
    // WHEN
    String dateRepresentation = STANDARD_REPRESENTATION.toStringOf(date);
    // THEN
    then(dateRepresentation).isEqualTo("2011-06-18T23:53:17.000 (java.util.Date)");
  }

  @Test
  void should_return_unambiguous_toString_of_LocalDate() {
    // GIVEN use Object to call toStringOf(Object) and not toStringOf(LocalDateTime)
    Object localDate = LocalDate.of(2011, 6, 18);
    // WHEN
    String localDateRepresentation = STANDARD_REPRESENTATION.toStringOf(localDate);
    // THEN
    then(localDateRepresentation).isEqualTo("2011-06-18 (java.time.LocalDate)");
  }

  @Test
  void should_return_unambiguous_toString_of_LocalDateTime() {
    // GIVEN use Object to call toStringOf(Object) and not toStringOf(LocalDateTime)
    Object localDateTime = LocalDateTime.of(2011, 6, 18, 23, 53, 17);
    // WHEN
    String localDateTimeRepresentation = STANDARD_REPRESENTATION.toStringOf(localDateTime);
    // THEN
    then(localDateTimeRepresentation).isEqualTo("2011-06-18T23:53:17 (java.time.LocalDateTime)");
  }

  @Test
  void should_return_unambiguous_toString_of_OffsetDateTime() {
    // GIVEN use Object to call toStringOf(Object) and not toStringOf(LocalDateTime)
    LocalDateTime localDateTime = LocalDateTime.of(2011, 6, 18, 23, 53, 17);
    Object offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
    // WHEN
    String offsetDateTimeRepresentation = STANDARD_REPRESENTATION.toStringOf(offsetDateTime);
    // THEN
    then(offsetDateTimeRepresentation).isEqualTo("2011-06-18T23:53:17Z (java.time.OffsetDateTime)");
  }

  @Test
  void should_return_unambiguous_toString_of_ZonedDateTime() {
    // GIVEN use Object to call toStringOf(Object) and not toStringOf(LocalDateTime)
    LocalDateTime localDateTime = LocalDateTime.of(2011, 6, 18, 23, 53, 17);
    Object offsetDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
    // WHEN
    String offsetDateTimeRepresentation = STANDARD_REPRESENTATION.toStringOf(offsetDateTime);
    // THEN
    then(offsetDateTimeRepresentation).isEqualTo("2011-06-18T23:53:17Z (java.time.ZonedDateTime)");
  }

  @Test
  void should_return_toString_of_AtomicReference() {
    AtomicReference<String> atomicReference = new AtomicReference<>("actual");
    assertThat(STANDARD_REPRESENTATION.toStringOf(atomicReference)).isEqualTo("AtomicReference[\"actual\"]");
  }

  @Test
  void should_return_toString_of_AtomicMarkableReference() {
    AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("actual", true);
    assertThat(STANDARD_REPRESENTATION.toStringOf(atomicMarkableReference)).isEqualTo("AtomicMarkableReference[marked=true, reference=\"actual\"]");
  }

  @Test
  void should_return_toString_of_AtomicStampedReference() {
    AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("actual", 123);
    assertThat(STANDARD_REPRESENTATION.toStringOf(atomicStampedReference)).isEqualTo("AtomicStampedReference[stamp=123, reference=\"actual\"]");
  }

  @Test
  void should_return_toString_of_AtomicIntegerFieldUpdater() {
    AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
    assertThat(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicIntegerFieldUpdater");
  }

  @Test
  void should_return_toString_of_AtomicLongFieldUpdater() {
    AtomicLongFieldUpdater<Person> updater = AtomicLongFieldUpdater.newUpdater(Person.class, "account");
    assertThat(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicLongFieldUpdater");
  }

  @Test
  void should_return_toString_of_AtomicReferenceFieldUpdater() {
    AtomicReferenceFieldUpdater<Person, String> updater = newUpdater(Person.class, String.class, "name");
    assertThat(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicReferenceFieldUpdater");
  }

  @Test
  void toString_with_anonymous_comparator() {
    Comparator<String> anonymousComparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    };
    assertThat(STANDARD_REPRESENTATION.toStringOf(anonymousComparator)).isEqualTo("'anonymous comparator class'");
  }

  @Test
  void toString_with_anonymous_comparator_overriding_toString() {
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
    assertThat(STANDARD_REPRESENTATION.toStringOf(anonymousComparator)).isEqualTo("foo");
  }

  @Test
  void toString_with_comparator_not_overriding_toString() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new StringTestComparator())).isEqualTo("StringTestComparator");
  }

  @Test
  void toString_with_comparator_overriding_toString() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new OtherStringTestComparator())).isEqualTo("other String comparator");
  }

  @Test
  void toString_with_comparator_overriding_toString_and_having_at() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new OtherStringTestComparatorWithAt())).isEqualTo("other String comparator with @");
  }

  @Test
  void should_format_longs_and_integers() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(20L).equals(toStringOf(20))).isFalse();
    assertThat(toStringOf(20)).isEqualTo("20");
    assertThat(toStringOf(20L)).isEqualTo("20L");
  }

  @Test
  void should_format_bytes_as_hex() {
    assertThat(toStringOf((byte) 20).equals(toStringOf((char) 20))).isFalse();
    assertThat((toStringOf((short) 20))).isEqualTo(toStringOf((byte) 20));
    assertThat(toStringOf((byte) 32)).isEqualTo("32");
  }

  @Test
  void should_format_doubles_and_floats() {
    assertThat(toStringOf(20.0f).equals(toStringOf(20.0))).isFalse();
    assertThat(toStringOf(20.0)).isEqualTo("20.0");
    assertThat(toStringOf(20.0f)).isEqualTo("20.0f");
  }

  @Test
  void should_format_tuples() {
    assertThat(toStringOf(tuple(1, 2, 3))).isEqualTo("(1, 2, 3)");
  }

  @Test
  void should_format_tuples_up_to_the_maximum_allowed_elements() {
    StandardRepresentation.setMaxElementsForPrinting(4);
    assertThat(toStringOf(tuple(1, 2, 3, 4, 5))).isEqualTo("(1, 2, ... 4, 5)");
  }

  @Test
  void should_format_simple_date_format() {
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    assertThat(toStringOf(sdf)).isEqualTo("ddMMyyyy");
  }

  @Test
  void should_format_assertj_map_entry() {
    MapEntry<String, Integer> entry = entry("A", 1);
    assertThat(toStringOf(entry)).isEqualTo("\"A\"=1");
  }

  @Test
  void should_format_java_map_entry() {
    Entry<String, Integer> entry = newHashMap("key", 123).entrySet().iterator().next();
    assertThat(toStringOf(entry)).isEqualTo("\"key\"=123");
  }

  @Test
  void should_return_toStringOf_method() {
    Method method = Arrays.stream(GenericClass.class.getMethods()).filter(m -> m.getName().equals("someGenericMethod"))
                          .findAny().get();

    assertThat(STANDARD_REPRESENTATION.toStringOf(method)).isEqualTo(method.toGenericString());
  }

  @ParameterizedTest
  @MethodSource("durations")
  void should_return_toString_of_duration(Duration duration, String expectedDurationRepresentation) {
    // WHEN
    String durationRepresentation = STANDARD_REPRESENTATION.toStringOf(duration);
    // THEN
    then(durationRepresentation).isEqualTo(expectedDurationRepresentation);
  }

  @Test
  void should_fix_1483() {
    // GIVEN
    VolatileSizeArrayList<String> list = new VolatileSizeArrayList<>();
    list.add("abc");
    list.add("def");
    // WHEN
    String toString = toStringOf(list);
    // THEN
    assertThat(toString).isEqualTo("[\"abc\", \"def\"]");
  }

  class VolatileSizeArrayList<T> extends AtomicInteger implements List<T> {

    private static final long serialVersionUID = 1L;
    private final List<T> list = new ArrayList<>();

    @Override
    public int size() {
      return list.size();
    }

    @Override
    public boolean isEmpty() {
      return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
      return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
      return list.iterator();
    }

    @Override
    public Object[] toArray() {
      return list.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
      return list.toArray(a);
    }

    @Override
    public boolean add(T e) {
      return list.add(e);
    }

    @Override
    public boolean remove(Object o) {
      return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
      return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
      return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      return list.retainAll(c);
    }

    @Override
    public void clear() {
      list.clear();
    }

    @Override
    public T get(int index) {
      return list.get(index);
    }

    @Override
    public T set(int index, T element) {
      return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
      list.add(index, element);
    }

    @Override
    public T remove(int index) {
      return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
      return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
      return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
      return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
      return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
      return list.subList(fromIndex, toIndex);
    }

  }

  private static Stream<Arguments> durations() {
    return Stream.of(arguments(Duration.of(1L, MILLIS), "0.001S"),
                     arguments(Duration.of(1234L, MILLIS), "1.234S"),
                     arguments(Duration.of(3_661_001L, MILLIS), "1H1M1.001S"));
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
