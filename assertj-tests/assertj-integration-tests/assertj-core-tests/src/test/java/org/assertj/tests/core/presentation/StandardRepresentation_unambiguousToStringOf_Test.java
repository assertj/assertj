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

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.lang.System.identityHashCode;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.Serial;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.tests.core.testkit.OtherStringTestComparator;
import org.assertj.tests.core.testkit.OtherStringTestComparatorWithAt;
import org.assertj.tests.core.testkit.StringTestComparator;
import org.junit.jupiter.api.Test;

class StandardRepresentation_unambiguousToStringOf_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  void should_return_null_if_object_is_null() {
    // GIVEN
    Object object = null;
    // WHEN
    String unambiguousToString = unambiguousToStringOf(object);
    // THEN
    then(unambiguousToString).isNull();
  }

  @Test
  void should_quote_String() {
    // GIVEN
    String string = "Hello";
    // WHEN
    String unambiguousToString = unambiguousToStringOf(string);
    // THEN
    then(unambiguousToString).isEqualTo(format("\"Hello\" (String@%s)", toHexString(identityHashCode(string))));
  }

  @Test
  void should_quote_empty_String() {
    // GIVEN
    String emptyString = "";
    // WHEN
    String unambiguousToString = unambiguousToStringOf(emptyString);
    // THEN
    then(unambiguousToString).isEqualTo(format("\"\" (String@%s)", toHexString(identityHashCode(emptyString))));
  }

  @Test
  void should_return_toString_of_File() {
    // GIVEN
    File file = new MyTestFile("/someFile.txt");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(file);
    // THEN
    then(unambiguousToString).isEqualTo(format("/someFile.txt (MyTestFile@%s)", toHexString(identityHashCode(file))));
  }

  @Test
  void should_return_toString_of_anonymous_class() {
    // GIVEN
    Object obj = new Object() {
      @Override
      public String toString() {
        return "my object";
      }
    };
    // WHEN
    String unambiguousToString = unambiguousToStringOf(obj);
    // THEN
    then(unambiguousToString).isEqualTo(format("my object (%s@%s)",
                                               obj.getClass().getName(),
                                               toHexString(identityHashCode(obj))));
  }

  @Test
  void should_return_toString_of_Class_with_its_name() {
    // GIVEN
    final Class<?> aClass = Object.class;
    // WHEN
    String unambiguousToString = unambiguousToStringOf(aClass);
    // THEN
    then(unambiguousToString).isEqualTo(format("java.lang.Object (Class@%s)", toHexString(identityHashCode(aClass))));
  }

  @Test
  void should_return_toString_of_Collection_of_String() {
    // GIVEN
    Collection<String> collection = list("s1", "s2");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(collection);
    // THEN
    then(unambiguousToString).isEqualTo(format("[\"s1\", \"s2\"] (ArrayList@%s)",
                                               toHexString(identityHashCode(collection))));
  }

  @Test
  void should_return_toString_of_Collection_of_arrays() {
    // GIVEN
    List<Boolean[]> collection = list(array(true, false), array(true, false, true));
    // WHEN
    String unambiguousToString = unambiguousToStringOf(collection);
    // THEN
    then(unambiguousToString).isEqualTo(format("[[true, false], [true, false, true]] (ArrayList@%s)",
                                               toHexString(identityHashCode(collection))));
  }

  @Test
  void should_return_toString_of_Collection_of_arrays_up_to_the_maximum_allowed_elements() {
    // GIVEN
    List<Boolean[]> collection = list(array(true, false),
                                      array(true),
                                      array(true, false),
                                      array(true, false, true, false, true),
                                      array(true, true));
    StandardRepresentation.setMaxElementsForPrinting(4);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(collection);
    // THEN
    then(unambiguousToString).isEqualTo(format("[[true, false], [true], ... [true, false, ... false, true], [true, true]] (ArrayList@%s)",
                                               toHexString(identityHashCode(collection))));
  }

  @Test
  void should_return_toString_of_Collection_of_Collections() {
    // GIVEN
    Collection<List<String>> collection = list(list("s1", "s2"), list("s3", "s4", "s5"));
    // WHEN
    String unambiguousToString = unambiguousToStringOf(collection);
    // THEN
    then(unambiguousToString).isEqualTo(format("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]] (ArrayList@%s)",
                                               toHexString(identityHashCode(collection))));
  }

  @Test
  void should_return_toString_of_Collection_of_Collections_up_to_the_maximum_allowed_elements() {
    // GIVEN
    Collection<List<String>> collection = list(list("s1", "s2"),
                                               list("s3", "s4", "s5", "s6", "s7"),
                                               list("s8", "s9"),
                                               list("s10", "s11"),
                                               list("s12"));
    StandardRepresentation.setMaxElementsForPrinting(2);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(collection);
    // THEN
    then(unambiguousToString).isEqualTo(format("[[\"s1\", \"s2\"], ... [\"s12\"]] (ArrayList@%s)",
                                               toHexString(identityHashCode(collection))));
  }

  @Test
  void should_return_toString_of_Map() {
    // GIVEN
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(map);
    // THEN
    then(unambiguousToString).isEqualTo(format("{\"key1\"=\"value1\", \"key2\"=\"value2\"} (LinkedHashMap@%s)",
                                               toHexString(identityHashCode(map))));
  }

  @Test
  void should_return_toString_of_array() {
    // GIVEN
    String[] array = array("s1", "s2");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(array);
    // THEN
    then(unambiguousToString).isEqualTo(format("[\"s1\", \"s2\"] (String[]@%s)", toHexString(identityHashCode(array))));
  }

  @Test
  void should_return_toString_of_array_of_arrays() {
    // GIVEN
    String[][] array = array(array("s1", "s2"),
                             array("s3", "s4", "s5"));
    // WHEN
    String unambiguousToString = unambiguousToStringOf(array);
    // THEN
    then(unambiguousToString).isEqualTo(format("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]] (String[][]@%s)",
                                               toHexString(identityHashCode(array))));
  }

  @Test
  void should_return_toString_of_array_of_arrays_up_to_the_maximum_allowed_elements() {
    // GIVEN
    String[][] array = array(array("s1", "s2"),
                             array("s3", "s4", "s5", "s6", "s7"),
                             array("s8", "s9"),
                             array("s10", "s11"),
                             array("s12"));
    StandardRepresentation.setMaxElementsForPrinting(4);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(array);
    // THEN
    then(unambiguousToString).isEqualTo(format("[[\"s1\", \"s2\"], [\"s3\", \"s4\", ... \"s6\", \"s7\"], ... [\"s10\", \"s11\"], [\"s12\"]] (String[][]@%s)",
                                               toHexString(identityHashCode(array))));
  }

  @Test
  void should_return_toString_of_array_of_Class() {
    // GIVEN
    Class<?>[] array = { String.class, File.class };
    // WHEN
    String unambiguousToString = unambiguousToStringOf(array);
    // THEN
    then(unambiguousToString).isEqualTo(format("[java.lang.String, java.io.File] (Class[]@%s)",
                                               toHexString(identityHashCode(array))));
  }

  @Test
  void should_return_toString_of_calendar() {
    // GIVEN
    GregorianCalendar calendar = new GregorianCalendar(2011, Calendar.JANUARY, 18, 23, 53, 17);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(calendar);
    // THEN
    then(unambiguousToString).isEqualTo("2011-01-18T23:53:17 (java.util.GregorianCalendar)");
  }

  @Test
  void should_return_toString_of_date() {
    // GIVEN
    Date date = new GregorianCalendar(2011, Calendar.JUNE, 18, 23, 53, 17).getTime();
    // WHEN
    String unambiguousToString = unambiguousToStringOf(date);
    // THEN
    then(unambiguousToString).isEqualTo("2011-06-18T23:53:17.000 (java.util.Date)");
  }

  @Test
  void should_return_toString_of_AtomicReference() {
    // GIVEN
    AtomicReference<String> atomicReference = new AtomicReference<>("actual");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(atomicReference);
    // THEN
    then(unambiguousToString).isEqualTo(format("AtomicReference[\"actual\"] (AtomicReference@%s)",
                                               toHexString(identityHashCode(atomicReference))));
  }

  @Test
  void should_return_toString_of_AtomicMarkableReference() {
    // GIVEN
    AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("actual", true);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(atomicMarkableReference);
    // THEN
    then(unambiguousToString).isEqualTo(format("AtomicMarkableReference[marked=true, reference=\"actual\"] (AtomicMarkableReference@%s)",
                                               toHexString(identityHashCode(atomicMarkableReference))));
  }

  @Test
  void should_return_toString_of_AtomicStampedReference() {
    // GIVEN
    AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("actual", 123);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(atomicStampedReference);
    // THEN
    then(unambiguousToString).isEqualTo(format("AtomicStampedReference[stamp=123, reference=\"actual\"] (AtomicStampedReference@%s)",
                                               toHexString(identityHashCode(atomicStampedReference))));
  }

  @Test
  void should_return_toString_of_AtomicIntegerFieldUpdater() {
    // GIVEN
    AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(updater);
    // THEN
    then(unambiguousToString).isEqualTo(format("AtomicIntegerFieldUpdater (%s@%s)",
                                               updater.getClass().getSimpleName(),
                                               toHexString(identityHashCode(updater))));
  }

  @Test
  void should_return_toString_of_AtomicLongFieldUpdater() {
    // GIVEN
    AtomicLongFieldUpdater<Person> updater = AtomicLongFieldUpdater.newUpdater(Person.class, "account");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(updater);
    // THEN
    then(unambiguousToString).isEqualTo(format("AtomicLongFieldUpdater (%s@%s)",
                                               updater.getClass().getSimpleName(),
                                               toHexString(identityHashCode(updater))));
  }

  @Test
  void should_return_toString_of_AtomicReferenceFieldUpdater() {
    // GIVEN
    AtomicReferenceFieldUpdater<Person, String> updater = newUpdater(Person.class, String.class, "name");
    // WHEN
    String unambiguousToString = unambiguousToStringOf(updater);
    // THEN
    then(unambiguousToString).isEqualTo(format("AtomicReferenceFieldUpdater (%s@%s)",
                                               updater.getClass().getSimpleName(),
                                               toHexString(identityHashCode(updater))));
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
    String unambiguousToString = unambiguousToStringOf(anonymousComparator);
    // THEN
    then(unambiguousToString).isEqualTo(format("'anonymous comparator class' (%s@%s)",
                                               anonymousComparator.getClass().getName(),
                                               toHexString(identityHashCode(anonymousComparator))));
  }

  @Test
  void toString_with_lambda_comparator() {
    // GIVEN
    Comparator<String> lambda = (s1, s2) -> s1.length() - s2.length();
    // WHEN
    String unambiguousToString = unambiguousToStringOf(lambda);
    // THEN
    then(unambiguousToString).isEqualTo(format("%s (%s@%s)",
                                               lambda.getClass().getSimpleName(),
                                               lambda.getClass().getSimpleName(),
                                               toHexString(identityHashCode(lambda))));
  }

  @Test
  void toString_with_builtin_comparator() {
    // GIVEN
    Comparator<String> comparator = Comparator.comparingInt(String::length);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(comparator);
    // THEN
    then(unambiguousToString).isEqualTo(format("%s (%s@%s)",
                                               comparator.getClass().getSimpleName(),
                                               comparator.getClass().getSimpleName(),
                                               toHexString(identityHashCode(comparator))));
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
    String unambiguousToString = unambiguousToStringOf(anonymousComparator);
    // THEN
    then(unambiguousToString).isEqualTo(format("foo (%s@%s)",
                                               anonymousComparator.getClass().getName(),
                                               toHexString(identityHashCode(anonymousComparator))));
  }

  @Test
  void toString_with_comparator_not_overriding_toString() {
    // GIVEN
    StringTestComparator stringTestComparator = new StringTestComparator();
    // WHEN
    String unambiguousToString = unambiguousToStringOf(stringTestComparator);
    // THEN
    then(unambiguousToString).isEqualTo(format("StringTestComparator (StringTestComparator@%s)",
                                               toHexString(identityHashCode(stringTestComparator))));
  }

  @Test
  void toString_with_comparator_overriding_toString() {
    // GIVEN
    OtherStringTestComparator otherStringTestComparator = new OtherStringTestComparator();
    // WHEN
    String unambiguousToString = unambiguousToStringOf(otherStringTestComparator);
    // THEN
    then(unambiguousToString).isEqualTo(format("other String comparator (OtherStringTestComparator@%s)",
                                               toHexString(identityHashCode(otherStringTestComparator))));
  }

  @Test
  void toString_with_comparator_overriding_toString_and_having_at() {
    // GIVEN
    OtherStringTestComparatorWithAt otherStringTestComparatorWithAt = new OtherStringTestComparatorWithAt();
    // WHEN
    String unambiguousToString = unambiguousToStringOf(otherStringTestComparatorWithAt);
    // THEN
    then(unambiguousToString).isEqualTo(format("other String comparator with @ (OtherStringTestComparatorWithAt@%s)",
                                               toHexString(identityHashCode(otherStringTestComparatorWithAt))));
  }

  @Test
  void should_format_longs_and_integers() {
    // GIVEN
    Long l = 20L;
    Integer i = 20;
    // WHEN/THEN
    then(unambiguousToStringOf(l)).isNotEqualTo(unambiguousToStringOf(i));
    then(unambiguousToStringOf(i)).isEqualTo(format("20 (Integer@%s)", toHexString(identityHashCode(i))));
    then(unambiguousToStringOf(l)).isEqualTo(format("20L (Long@%s)", toHexString(identityHashCode(l))));
  }

  @Test
  void should_format_bytes_chars_and_shorts() {
    // GIVEN
    Byte b = (byte) 20;
    Character c = (char) 20;
    Short s = (short) 20;
    // WHEN/THEN
    then(unambiguousToStringOf(b)).isNotEqualTo(unambiguousToStringOf(c));
    then(unambiguousToStringOf(b)).isNotEqualTo(unambiguousToStringOf(s));
    then(unambiguousToStringOf(c)).isNotEqualTo(unambiguousToStringOf(s));
    then(unambiguousToStringOf(b)).isEqualTo(format("20 (Byte@%s)", toHexString(identityHashCode(b))));
    then(unambiguousToStringOf(c)).isEqualTo(format("'\u0014' (Character@%s)", toHexString(identityHashCode(c))));
    then(unambiguousToStringOf(s)).isEqualTo(format("20 (Short@%s)", toHexString(identityHashCode(s))));
  }

  @Test
  void should_format_doubles_and_floats() {
    // GIVEN
    Float f = 20.0f;
    Double d = 20.0d;
    // WHEN/THEN
    then(unambiguousToStringOf(f)).isNotEqualTo(unambiguousToStringOf(d));
    then(unambiguousToStringOf(d)).isEqualTo(format("20.0 (Double@%s)", toHexString(identityHashCode(d))));
    then(unambiguousToStringOf(f)).isEqualTo(format("20.0f (Float@%s)", toHexString(identityHashCode(f))));
  }

  @Test
  void should_format_tuples() {
    // GIVEN
    Tuple tuple = tuple(1, 2, 3);
    // WHEN/THEN
    then(unambiguousToStringOf(tuple)).isEqualTo(format("(1, 2, 3) (Tuple@%s)", toHexString(identityHashCode(tuple))));
  }

  @Test
  void should_format_tuples_up_to_the_maximum_allowed_elements() {
    // GIVEN
    StandardRepresentation.setMaxElementsForPrinting(2);
    Tuple tuple = tuple(1, 2, 3, 4, 5);
    // WHEN/THEN
    then(unambiguousToStringOf(tuple)).isEqualTo(format("(1, ... 5) (Tuple@%s)", toHexString(identityHashCode(tuple))));
  }

  @Test
  void should_format_simple_date_format() {
    // GIVEN
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    // WHEN/THEN
    then(unambiguousToStringOf(sdf)).isEqualTo(format("ddMMyyyy (SimpleDateFormat@%s)", toHexString(identityHashCode(sdf))));
  }

  @Test
  void should_format_assertj_map_entry() {
    // GIVEN
    MapEntry<String, Integer> entry = entry("A", 1);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(entry);
    // THEN
    then(unambiguousToString).isEqualTo(format("\"A\"=1 (MapEntry@%s)", toHexString(identityHashCode(entry))));
  }

  @Test
  void should_return_unambiguousToStringOf_method() throws NoSuchMethodException {
    // GIVEN
    Method method = GenericClass.class.getDeclaredMethod("someGenericMethod", Person.class, List.class, Object.class);
    // WHEN
    String unambiguousToString = unambiguousToStringOf(method);
    // THEN
    then(unambiguousToString).isEqualTo(format("%s (Method@%s)",
                                               method.toGenericString(),
                                               toHexString(identityHashCode(method))));
  }

  @Test
  void should_disambiguate_non_equal_objects_with_same_hash_code_and_toString_representations() {
    // GIVEN
    String unambiguousToString1 = unambiguousToStringOf(new Ambiguous(0, 1));
    String unambiguousToString2 = unambiguousToStringOf(new Ambiguous(0, 2));
    // WHEN/THEN
    then(unambiguousToString1).isNotEqualTo(unambiguousToString2);
  }

  @Test
  void isEqualTo_should_show_disambiguated_objects_with_same_hash_code_and_toString_representations() {
    // GIVEN
    Ambiguous ambiguous1 = new Ambiguous(0, 1);
    Ambiguous ambiguous2 = new Ambiguous(0, 2);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(ambiguous1).isEqualTo(ambiguous2));
    // THEN
    then(error).hasMessageContaining(unambiguousToStringOf(ambiguous1))
               .hasMessageContaining(unambiguousToStringOf(ambiguous2));
  }

  private static String unambiguousToStringOf(Object o) {
    return STANDARD_REPRESENTATION.unambiguousToStringOf(o);
  }

  private static class MyTestFile extends File {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String path;

    MyTestFile(String path) {
      super(path);
      this.path = path;
    }

    @Override
    public String getAbsolutePath() {
      return path;
    }
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

  private static class Ambiguous {

    int x;
    int y;

    Ambiguous(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Ambiguous that = (Ambiguous) o;
      return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
      return x;
    }

    @Override
    public String toString() {
      return String.format("Ambiguous(%d)", x);
    }
  }

}
