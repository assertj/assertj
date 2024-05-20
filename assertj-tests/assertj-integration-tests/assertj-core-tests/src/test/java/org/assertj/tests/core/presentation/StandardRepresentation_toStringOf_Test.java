/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.presentation;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.io.Serial;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
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
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.tests.core.testkit.OtherStringTestComparator;
import org.assertj.tests.core.testkit.OtherStringTestComparatorWithAt;
import org.assertj.tests.core.testkit.StringTestComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StandardRepresentation_toStringOf_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  void should_return_null_if_object_is_null() {
    // GIVEN
    Object object = null;
    // WHEN
    String nullStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(object);
    // THEN
    then(nullStandardRepresentation).isNull();
  }

  @Test
  void should_quote_String() {
    // GIVEN
    String string = "Hello";
    // WHEN
    String emptyStringStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(string);
    // THEN
    then(emptyStringStandardRepresentation).isEqualTo("\"Hello\"");
  }

  @Test
  void should_quote_empty_String() {
    // GIVEN
    String emptyString = "";
    // WHEN
    String emptyStringStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(emptyString);
    // THEN
    then(emptyStringStandardRepresentation).isEqualTo("\"\"");
  }

  @Test
  void should_return_toString_of_File() {
    // GIVEN
    final String path = "/someFile.txt";
    File file = new File(path) {
      @Override
      public String getAbsolutePath() {
        return path;
      }
    };
    // WHEN
    String fileStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(file);
    // THEN
    then(fileStandardRepresentation).isEqualTo(path);
  }

  @Test
  void should_return_toString_of_Path() {
    // GIVEN
    final Path path = Paths.get("someFile.txt");
    // WHEN
    String pathStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(path);
    // THEN
    then(pathStandardRepresentation).isEqualTo("someFile.txt");
  }

  @Test
  void should_return_toString_of_Class_with_its_name() {
    // GIVEN
    final Class<?> aClass = Object.class;
    // WHEN
    String classStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(aClass);
    // THEN
    then(classStandardRepresentation).isEqualTo("java.lang.Object");
  }

  @Test
  void should_return_toString_of_anonymous_Class_with_generic_description() {
    // GIVEN
    Object anonymous = new Object() {};
    // WHEN
    String anonymousClassStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(anonymous.getClass());
    // THEN
    then(anonymousClassStandardRepresentation).isEqualTo("anonymous class");
  }

  @Test
  void should_return_toString_of_anonymous_Class_array_with_generic_description() {
    // GIVEN
    Object anonymous = new Object() {};
    Object anonymousArray = Array.newInstance(anonymous.getClass(), 1);
    // WHEN
    String anonymousClassArrayStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(anonymousArray.getClass());
    // THEN
    then(anonymousClassArrayStandardRepresentation).isEqualTo("anonymous class array");
  }

  @Test
  void should_return_toString_of_local_Class_with_its_simple_name() {
    // GIVEN
    class LocalClass {
    }
    // WHEN
    String localClassStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(LocalClass.class);
    // THEN
    then(localClassStandardRepresentation).isEqualTo("local class LocalClass");
  }

  @Test
  void should_return_toString_of_local_Class_array_with_its_simple_name() {
    // GIVEN
    class LocalClass {
    }
    // WHEN
    String localClassArrayStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(LocalClass[].class);
    // THEN
    then(localClassArrayStandardRepresentation).isEqualTo("local class LocalClass[]");
  }

  @Test
  void should_return_toString_of_Collection_of_String() {
    // GIVEN
    Collection<String> collection = list("s1", "s2");
    // WHEN
    String stringCollectionStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(collection);
    // THEN
    then(stringCollectionStandardRepresentation).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  void should_return_toString_of_Collection_of_arrays() {
    // GIVEN
    List<Boolean[]> collection = list(array(true, false), array(true, false, true));
    // WHEN
    String arrayCollectionStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(collection);
    // THEN
    then(arrayCollectionStandardRepresentation).isEqualTo("[[true, false], [true, false, true]]");
  }

  @Test
  void should_return_toString_of_Collection_of_arrays_up_to_the_maximum_allowed_elements() {
    // GIVEN
    List<Boolean[]> collection = list(array(true),
                                      array(true, false, true, false, true),
                                      array(true, true),
                                      array(true),
                                      array(true));
    StandardRepresentation.setMaxElementsForPrinting(4);
    // WHEN
    String collectionStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(collection);
    // THEN
    then(collectionStandardRepresentation).isEqualTo("[[true], [true, false, ... false, true], ... [true], [true]]");
  }

  @Test
  void should_return_toString_of_Collection_of_Collections() {
    // GIVEN
    Collection<List<String>> collection = list(list("s1", "s2"), list("s3", "s4", "s5"));
    // WHEN
    String collectionOfCollectionStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(collection);
    // THEN
    then(collectionOfCollectionStandardRepresentation).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  void should_return_toString_of_Collection_of_Collections_up_to_the_maximum_allowed_elements() {
    // GIVEN
    Collection<List<String>> collection = list(list("s1"),
                                               list("s2", "s3", "s4", "s5", "s6"),
                                               list("s7", "s8"),
                                               list("s9"),
                                               list("s10"));
    StandardRepresentation.setMaxElementsForPrinting(4);
    // WHEN
    String collectionOfCollectionStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(collection);
    // THEN
    then(collectionOfCollectionStandardRepresentation).isEqualTo("[[\"s1\"], [\"s2\", \"s3\", ... \"s5\", \"s6\"], ... [\"s9\"], [\"s10\"]]");
  }

  @Test
  void should_return_toString_of_Map() {
    // GIVEN
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    // WHEN
    String mapStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(map);
    // THEN
    then(mapStandardRepresentation).isEqualTo("{\"key1\"=\"value1\", \"key2\"=\"value2\"}");
  }

  @Test
  void should_return_toString_of_array() {
    // GIVEN
    String[] array = array("s1", "s2");
    // WHEN
    String arrayStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(array);
    // THEN
    then(arrayStandardRepresentation).isEqualTo("[\"s1\", \"s2\"]");
  }

  @Test
  void should_return_toString_of_array_of_arrays() {
    // GIVEN
    String[][] array = array(array("s1", "s2"), array("s3", "s4", "s5"));
    // WHEN
    String arrayStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(array);
    // THEN
    then(arrayStandardRepresentation).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]]");
  }

  @Test
  void should_return_toString_of_array_of_arrays_up_to_the_maximum_allowed_elements() {
    // GIVEN
    String[][] array = array(array("s1", "s2"),
                             array("s3", "s4", "s5", "s6", "s7"),
                             array("s8"),
                             array("s9"),
                             array("s10"));
    StandardRepresentation.setMaxElementsForPrinting(4);
    // WHEN
    String arrayStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(array);
    // THEN
    then(arrayStandardRepresentation).isEqualTo("[[\"s1\", \"s2\"], [\"s3\", \"s4\", ... \"s6\", \"s7\"], ... [\"s9\"], [\"s10\"]]");
  }

  @Test
  void should_return_toString_of_array_of_Class() {
    // GIVEN
    Class<?>[] array = { String.class, File.class };
    // WHEN
    String arrayStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(array);
    // THEN
    then(arrayStandardRepresentation).isEqualTo("[java.lang.String, java.io.File]");
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
  void should_return_unambiguous_toString_of_YearMonth() {
    // GIVEN use Object to call toStringOf(Object) and not toStringOf(YearMonth)
    Object yearMonth = YearMonth.of(2011, 6);
    // WHEN
    String localDateRepresentation = STANDARD_REPRESENTATION.toStringOf(yearMonth);
    // THEN
    then(localDateRepresentation).isEqualTo("2011-06 (java.time.YearMonth)");
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
    // GIVEN
    AtomicReference<String> atomicReference = new AtomicReference<>("actual");
    // WHEN
    String atomicReferenceRepresentation = STANDARD_REPRESENTATION.toStringOf(atomicReference);
    // THEN
    then(atomicReferenceRepresentation).isEqualTo("AtomicReference[\"actual\"]");
  }

  @Test
  void should_return_toString_of_AtomicMarkableReference() {
    // GIVEN
    AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("actual", true);
    // WHEN
    String atomicMarkableReferenceRepresentation = STANDARD_REPRESENTATION.toStringOf(atomicMarkableReference);
    // THEN
    then(atomicMarkableReferenceRepresentation).isEqualTo("AtomicMarkableReference[marked=true, reference=\"actual\"]");
  }

  @Test
  void should_return_toString_of_AtomicStampedReference() {
    // GIVEN
    AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("actual", 123);
    // WHEN
    String atomicStampedReferenceRepresentation = STANDARD_REPRESENTATION.toStringOf(atomicStampedReference);
    // THEN
    then(atomicStampedReferenceRepresentation).isEqualTo("AtomicStampedReference[stamp=123, reference=\"actual\"]");
  }

  @Test
  void should_return_toString_of_AtomicIntegerFieldUpdater() {
    // GIVEN
    AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
    // WHEN
    String atomicIntegerFieldUpdaterRepresentation = STANDARD_REPRESENTATION.toStringOf(updater);
    // THEN
    then(atomicIntegerFieldUpdaterRepresentation).isEqualTo("AtomicIntegerFieldUpdater");
  }

  @Test
  void should_return_toString_of_AtomicLongFieldUpdater() {
    // GIVEN
    AtomicLongFieldUpdater<Person> updater = AtomicLongFieldUpdater.newUpdater(Person.class, "account");
    // WHEN
    String atomicLongFieldUpdaterRepresentation = STANDARD_REPRESENTATION.toStringOf(updater);
    // THEN
    then(atomicLongFieldUpdaterRepresentation).isEqualTo("AtomicLongFieldUpdater");
  }

  @Test
  void should_return_toString_of_AtomicReferenceFieldUpdater() {
    AtomicReferenceFieldUpdater<Person, String> updater = newUpdater(Person.class, String.class, "name");
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(updater)).isEqualTo("AtomicReferenceFieldUpdater");
  }

  @Test
  void toString_with_anonymous_comparator() {
    // GIVEN
    Comparator<String> anonymousComparator = new Comparator<>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    };
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(anonymousComparator);
    // THEN
    then(representation).isEqualTo("'anonymous comparator class'");
  }

  @Test
  void toString_with_anonymous_comparator_overriding_toString() {
    // GIVEN
    Comparator<String> anonymousComparator = new Comparator<>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }

      @Override
      public String toString() {
        return "foo";
      }
    };
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(anonymousComparator);
    // THEN
    then(representation).isEqualTo("foo");
  }

  @Test
  void toString_with_comparator_not_overriding_toString() {
    // GIVEN
    StringTestComparator comparator = new StringTestComparator();
    // WHEN
    String comparatorStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(comparator);
    // THEN
    then(comparatorStandardRepresentation).isEqualTo("StringTestComparator");
  }

  @Test
  void toString_with_comparator_overriding_toString() {
    // GIVEN
    OtherStringTestComparator comparator = new OtherStringTestComparator();
    // WHEN
    String comparatorStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(comparator);
    // THEN
    then(comparatorStandardRepresentation).isEqualTo("other String comparator");
  }

  @Test
  void toString_with_comparator_overriding_toString_and_having_at() {
    // GIVEN
    OtherStringTestComparatorWithAt comparator = new OtherStringTestComparatorWithAt();
    // WHEN
    String comparatorStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(comparator);
    // THEN
    then(comparatorStandardRepresentation).isEqualTo("other String comparator with @");
  }

  @Test
  void should_format_byte() {
    // GIVEN
    byte b = 20;
    // WHEN
    String byteStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(b);
    // THEN
    then(byteStandardRepresentation).isEqualTo("20");
  }

  @Test
  void should_format_char() {
    // GIVEN
    char a = 'a';
    // WHEN
    String charStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(a);
    // THEN
    then(charStandardRepresentation).isEqualTo("'a'");
  }

  @Test
  void should_format_short() {
    // GIVEN
    short s = 20;
    // WHEN
    String shortStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(s);
    // THEN
    then(shortStandardRepresentation).isEqualTo("20");
  }

  @Test
  void should_format_int() {
    // GIVEN
    int i = 20;
    // WHEN
    String intStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(i);
    // THEN
    then(intStandardRepresentation).isEqualTo("20");
  }

  @Test
  void should_format_long() {
    // GIVEN
    long l = 20;
    // WHEN
    String longStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(l);
    // THEN
    then(longStandardRepresentation).isEqualTo("20L");
  }

  @Test
  void should_format_float() {
    // GIVEN
    float d = 20.0f;
    // WHEN
    String floatStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(d);
    // THEN
    then(floatStandardRepresentation).isEqualTo("20.0f");
  }

  @Test
  void should_format_double() {
    // GIVEN
    double d = 20.0;
    // WHEN
    String doubleStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(d);
    // THEN
    then(doubleStandardRepresentation).isEqualTo("20.0");
  }

  @Test
  void should_format_tuple() {
    // GIVEN
    Tuple tuple = tuple(1, 2, 3);
    // WHEN
    String tupleStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(tuple);
    // THEN
    then(tupleStandardRepresentation).isEqualTo("(1, 2, 3)");
  }

  @Test
  void should_format_tuples_up_to_the_maximum_allowed_elements() {
    // GIVEN
    StandardRepresentation.setMaxElementsForPrinting(4);
    // WHEN
    String tupleRepresentation = STANDARD_REPRESENTATION.toStringOf(tuple(1, 2, 3, 4, 5));
    // THEN
    then(tupleRepresentation).isEqualTo("(1, 2, ... 4, 5)");
  }

  @Test
  void should_format_simple_date_format() {
    // GIVEN
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    // WHEN
    String sdfRepresentation = STANDARD_REPRESENTATION.toStringOf(sdf);
    // THEN
    then(sdfRepresentation).isEqualTo("ddMMyyyy");
  }

  @Test
  void should_format_assertj_map_entry() {
    // GIVEN
    MapEntry<String, Integer> entry = entry("A", 1);
    // WHEN
    String entryRepresentation = STANDARD_REPRESENTATION.toStringOf(entry);
    // THEN
    then(entryRepresentation).isEqualTo("\"A\"=1");
  }

  @Test
  void should_format_java_map_entry() {
    // GIVEN
    Entry<String, Integer> entry = newHashMap("key", 123).entrySet().iterator().next();
    // WHEN
    String javaEntryRepresentation = STANDARD_REPRESENTATION.toStringOf(entry);
    // THEN
    then(javaEntryRepresentation).isEqualTo("\"key\"=123");
  }

  @Test
  void should_return_toStringOf_method() {
    // GIVEN
    Method method = Arrays.stream(GenericClass.class.getMethods()).filter(m -> m.getName().equals("someGenericMethod"))
                          .findAny()
                          .get();
    // WHEN
    String methodRepresentation = STANDARD_REPRESENTATION.toStringOf(method);
    // THEN
    then(methodRepresentation).isEqualTo(method.toGenericString());
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
    String toString = STANDARD_REPRESENTATION.toStringOf(list);
    // THEN
    then(toString).isEqualTo("[\"abc\", \"def\"]");
  }

  static class VolatileSizeArrayList<T> extends AtomicInteger implements List<T> {

    @Serial
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

  //@format:off FIXME the formatter profile shouldn't touch this method
  private static Stream<Arguments> durations() {
    return Stream.of(
      arguments(Duration.of(1L, MILLIS), "0.001S"),
      arguments(Duration.of(1234L, MILLIS), "1.234S"),
      arguments(Duration.of(3_661_001L, MILLIS), "1H1M1.001S"));
  }
  //@format:on

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
