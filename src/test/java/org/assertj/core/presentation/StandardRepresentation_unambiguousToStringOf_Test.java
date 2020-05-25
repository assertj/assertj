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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.io.File;
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
import org.assertj.core.util.OtherStringTestComparator;
import org.assertj.core.util.OtherStringTestComparatorWithAt;
import org.assertj.core.util.StringTestComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StandardRepresentation#unambiguousToStringOf(Object)}.
 *
 * @author Alexandre Dutra
 */
public class StandardRepresentation_unambiguousToStringOf_Test extends AbstractBaseRepresentationTest {

  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  @Test
  public void should_return_null_if_object_is_null() {
    assertThat(unambiguousToStringOf(null)).isNull();
  }

  @Test
  public void should_quote_String() {
    String obj = "Hello";
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("\"Hello\" (String@%s)",
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void should_quote_empty_String() {
    String obj = "";
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("\"\" (String@%s)",
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void should_return_toString_of_File() {
    File obj = new MyTestFile("/someFile.txt");
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("/someFile.txt (MyTestFile@%s)",
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void should_return_toString_of_anonymous_class() {
    Object obj = new Object() {
      @Override
      public String toString() {
        return "my object";
      }
    };
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("my object (%s@%s)",
                                                            obj.getClass().getName(),
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void should_return_toString_of_Class_with_its_name() {
    assertThat(unambiguousToStringOf(Object.class)).isEqualTo(format("java.lang.Object (Class@%s)",
                                                                     toHexString(System.identityHashCode(Object.class))));
  }

  @Test
  public void should_return_toString_of_Collection_of_String() {
    Collection<String> collection = list("s1", "s2");
    assertThat(unambiguousToStringOf(collection)).isEqualTo(format("[\"s1\", \"s2\"] (ArrayList@%s)",
                                                                   toHexString(System.identityHashCode(collection))));
  }

  @Test
  public void should_return_toString_of_Collection_of_arrays() {
    List<Boolean[]> collection = list(array(true, false),
                                      array(true, false, true));
    assertThat(unambiguousToStringOf(collection)).isEqualTo(format("[[true, false], [true, false, true]] (ArrayList@%s)",
                                                                   toHexString(System.identityHashCode(collection))));
  }

  @Test
  public void should_return_toString_of_Collection_of_arrays_up_to_the_maximum_allowed_elements() {
    List<Boolean[]> collection = list(array(true, false),
                                      array(true),
                                      array(true, false),
                                      array(true, false, true, false, true),
                                      array(true, true));
    StandardRepresentation.setMaxElementsForPrinting(4);
    assertThat(unambiguousToStringOf(collection)).isEqualTo(format("[[true, false], [true], ... [true, false, ... false, true], [true, true]] (ArrayList@%s)",
                                                                   toHexString(System.identityHashCode(collection))));
  }

  @Test
  public void should_return_toString_of_Collection_of_Collections() {
    Collection<List<String>> collection = list(
                                               list("s1", "s2"),
                                               list("s3", "s4", "s5"));
    assertThat(unambiguousToStringOf(collection)).isEqualTo(format("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]] (ArrayList@%s)",
                                                                   toHexString(System.identityHashCode(collection))));
  }

  @Test
  public void should_return_toString_of_Collection_of_Collections_up_to_the_maximum_allowed_elements() {
    Collection<List<String>> collection = list(list("s1", "s2"),
                                               list("s3", "s4", "s5", "s6", "s7"),
                                               list("s8", "s9"),
                                               list("s10", "s11"),
                                               list("s12"));
    StandardRepresentation.setMaxElementsForPrinting(2);
    assertThat(unambiguousToStringOf(collection)).isEqualTo(format("[[\"s1\", \"s2\"], ... [\"s12\"]] (ArrayList@%s)",
                                                                   toHexString(System.identityHashCode(collection))));
  }

  @Test
  public void should_return_toString_of_Map() {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertThat(unambiguousToStringOf(map)).isEqualTo(format("{\"key1\"=\"value1\", \"key2\"=\"value2\"} (LinkedHashMap@%s)",
                                                            toHexString(System.identityHashCode(map))));
  }

  @Test
  public void should_return_toString_of_array() {
    String[] array = array("s1", "s2");
    assertThat(unambiguousToStringOf(array)).isEqualTo(format("[\"s1\", \"s2\"] (String[]@%s)",
                                                              toHexString(System.identityHashCode(array))));
  }

  @Test
  public void should_return_toString_of_array_of_arrays() {
    String[][] array = array(array("s1", "s2"),
                             array("s3", "s4", "s5"));
    assertThat(unambiguousToStringOf(array)).isEqualTo(format("[[\"s1\", \"s2\"], [\"s3\", \"s4\", \"s5\"]] (String[][]@%s)",
                                                              toHexString(System.identityHashCode(array))));
  }

  @Test
  public void should_return_toString_of_array_of_arrays_up_to_the_maximum_allowed_elements() {
    String[][] array = array(array("s1", "s2"),
                             array("s3", "s4", "s5", "s6", "s7"),
                             array("s8", "s9"),
                             array("s10", "s11"),
                             array("s12"));
    StandardRepresentation.setMaxElementsForPrinting(4);
    assertThat(unambiguousToStringOf(array)).isEqualTo(format("[[\"s1\", \"s2\"], [\"s3\", \"s4\", ... \"s6\", \"s7\"], ... [\"s10\", \"s11\"], [\"s12\"]] (String[][]@%s)",
                                                              toHexString(System.identityHashCode(array))));
  }

  @Test
  public void should_return_toString_of_array_of_Class() {
    Class<?>[] array = { String.class, File.class };
    assertThat(unambiguousToStringOf(array)).isEqualTo(format("[java.lang.String, java.io.File] (Class[]@%s)",
                                                              toHexString(System.identityHashCode(array))));
  }

  @Test
  public void should_return_toString_of_calendar() {
    GregorianCalendar calendar = new GregorianCalendar(2011, Calendar.JANUARY, 18, 23, 53, 17);
    assertThat(unambiguousToStringOf(calendar)).isEqualTo("2011-01-18T23:53:17 (java.util.GregorianCalendar)");
  }

  @Test
  public void should_return_toString_of_date() {
    Date date = new GregorianCalendar(2011, Calendar.JUNE, 18, 23, 53, 17).getTime();
    assertThat(unambiguousToStringOf(date)).isEqualTo("2011-06-18T23:53:17.000 (java.util.Date)");
  }

  @Test
  public void should_return_toString_of_AtomicReference() {
    AtomicReference<String> atomicReference = new AtomicReference<>("actual");
    assertThat(unambiguousToStringOf(atomicReference)).isEqualTo(format("AtomicReference[\"actual\"] (AtomicReference@%s)",
                                                                        toHexString(System.identityHashCode(atomicReference))));
  }

  @Test
  public void should_return_toString_of_AtomicMarkableReference() {
    AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("actual", true);
    assertThat(unambiguousToStringOf(atomicMarkableReference)).isEqualTo(format("AtomicMarkableReference[marked=true, reference=\"actual\"] (AtomicMarkableReference@%s)",
                                                                                toHexString(System.identityHashCode(atomicMarkableReference))));
  }

  @Test
  public void should_return_toString_of_AtomicStampedReference() {
    AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("actual", 123);
    assertThat(unambiguousToStringOf(atomicStampedReference)).isEqualTo(format("AtomicStampedReference[stamp=123, reference=\"actual\"] (AtomicStampedReference@%s)",
                                                                               toHexString(System.identityHashCode(atomicStampedReference))));
  }

  @Test
  public void should_return_toString_of_AtomicIntegerFieldUpdater() {
    AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
    assertThat(unambiguousToStringOf(updater)).isEqualTo(format("AtomicIntegerFieldUpdater (%s@%s)",
                                                                updater.getClass().getSimpleName(),
                                                                toHexString(System.identityHashCode(updater))));
  }

  @Test
  public void should_return_toString_of_AtomicLongFieldUpdater() {
    AtomicLongFieldUpdater<Person> updater = AtomicLongFieldUpdater.newUpdater(Person.class, "account");
    assertThat(unambiguousToStringOf(updater)).isEqualTo(format("AtomicLongFieldUpdater (%s@%s)",
                                                                updater.getClass().getSimpleName(),
                                                                toHexString(System.identityHashCode(updater))));
  }

  @Test
  public void should_return_toString_of_AtomicReferenceFieldUpdater() {
    AtomicReferenceFieldUpdater<Person, String> updater = newUpdater(Person.class, String.class, "name");
    assertThat(unambiguousToStringOf(updater)).isEqualTo(format("AtomicReferenceFieldUpdater (%s@%s)",
                                                                updater.getClass().getSimpleName(),
                                                                toHexString(System.identityHashCode(updater))));
  }

  @Test
  public void toString_with_anonymous_comparator() {
    @SuppressWarnings("unused")
    Comparator<String> anonymousComparator = new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    };
    assertThat(unambiguousToStringOf(anonymousComparator)).isEqualTo(format("'anonymous comparator class' (%s@%s)",
                                                                            anonymousComparator.getClass().getName(),
                                                                            toHexString(System.identityHashCode(anonymousComparator))));
  }

  @Test
  public void toString_with_lambda_comparator() {
    Comparator<String> lambda = (s1, s2) -> s1.length() - s2.length();
    assertThat(unambiguousToStringOf(lambda)).isEqualTo(format("%s (%s@%s)",
                                                               lambda.getClass().getSimpleName(),
                                                               lambda.getClass().getSimpleName(),
                                                               toHexString(System.identityHashCode(lambda))));
  }

  @Test
  public void toString_with_builtin_comparator() {
    Comparator<String> comparator = Comparator.comparingInt(String::length);
    assertThat(unambiguousToStringOf(comparator)).isEqualTo(format("%s (%s@%s)",
                                                                   comparator.getClass().getSimpleName(),
                                                                   comparator.getClass().getSimpleName(),
                                                                   toHexString(System.identityHashCode(comparator))));
  }

  @Test
  public void toString_with_anonymous_comparator_overriding_toString() {
    @SuppressWarnings("unused")
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
    assertThat(unambiguousToStringOf(anonymousComparator)).isEqualTo(format("foo (%s@%s)",
                                                                            anonymousComparator.getClass().getName(),
                                                                            toHexString(System.identityHashCode(anonymousComparator))));
  }

  @Test
  public void toString_with_comparator_not_overriding_toString() {
    StringTestComparator obj = new StringTestComparator();
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("StringTestComparator (StringTestComparator@%s)",
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void toString_with_comparator_overriding_toString() {
    OtherStringTestComparator obj = new OtherStringTestComparator();
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("other String comparator (OtherStringTestComparator@%s)",
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void toString_with_comparator_overriding_toString_and_having_at() {
    OtherStringTestComparatorWithAt obj = new OtherStringTestComparatorWithAt();
    assertThat(unambiguousToStringOf(obj)).isEqualTo(format("other String comparator with @ (OtherStringTestComparatorWithAt@%s)",
                                                            toHexString(System.identityHashCode(obj))));
  }

  @Test
  public void should_format_longs_and_integers() {
    Long l = 20L;
    Integer i = 20;
    assertThat(unambiguousToStringOf(l)).isNotEqualTo(unambiguousToStringOf(i));
    assertThat(unambiguousToStringOf(i)).isEqualTo(format("20 (Integer@%s)", toHexString(System.identityHashCode(i))));
    assertThat(unambiguousToStringOf(l)).isEqualTo(format("20L (Long@%s)", toHexString(System.identityHashCode(l))));
  }

  @Test
  public void should_format_bytes_chars_and_shorts() {
    Byte b = (byte) 20;
    Character c = (char) 20;
    Short s = (short) 20;
    assertThat(unambiguousToStringOf(b)).isNotEqualTo(unambiguousToStringOf(c));
    assertThat(unambiguousToStringOf(b)).isNotEqualTo(unambiguousToStringOf(s));
    assertThat(unambiguousToStringOf(c)).isNotEqualTo(unambiguousToStringOf(s));
    assertThat(unambiguousToStringOf(b)).isEqualTo(format("20 (Byte@%s)", toHexString(System.identityHashCode(b))));
    assertThat(unambiguousToStringOf(c)).isEqualTo(format("'\u0014' (Character@%s)",
                                                          toHexString(System.identityHashCode(c))));
    assertThat(unambiguousToStringOf(s)).isEqualTo(format("20 (Short@%s)", toHexString(System.identityHashCode(s))));
  }

  @Test
  public void should_format_doubles_and_floats() {
    Float f = 20.0f;
    Double d = 20.0d;
    assertThat(unambiguousToStringOf(f)).isNotEqualTo(unambiguousToStringOf(d));
    assertThat(unambiguousToStringOf(d)).isEqualTo(format("20.0 (Double@%s)", toHexString(System.identityHashCode(d))));
    assertThat(unambiguousToStringOf(f)).isEqualTo(format("20.0f (Float@%s)", toHexString(System.identityHashCode(f))));
  }

  @Test
  public void should_format_tuples() {
    Tuple tuple = tuple(1, 2, 3);
    assertThat(unambiguousToStringOf(tuple)).isEqualTo(format("(1, 2, 3) (Tuple@%s)",
                                                              toHexString(System.identityHashCode(tuple))));
  }

  @Test
  public void should_format_tuples_up_to_the_maximum_allowed_elements() {
    StandardRepresentation.setMaxElementsForPrinting(2);
    Tuple tuple = tuple(1, 2, 3, 4, 5);
    assertThat(unambiguousToStringOf(tuple)).isEqualTo(format("(1, ... 5) (Tuple@%s)",
                                                              toHexString(System.identityHashCode(tuple))));
  }

  @Test
  public void should_format_simple_date_format() {
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    assertThat(unambiguousToStringOf(sdf)).isEqualTo(format("ddMMyyyy (SimpleDateFormat@%s)",
                                                            toHexString(System.identityHashCode(sdf))));
  }

  @Test
  public void should_format_assertj_map_entry() {
    MapEntry<String, Integer> entry = entry("A", 1);
    assertThat(unambiguousToStringOf(entry)).isEqualTo(format("MapEntry[key=\"A\", value=1] (MapEntry@%s)",
                                                              toHexString(System.identityHashCode(entry))));
  }

  @Test
  public void should_return_unambiguousToStringOf_method() throws NoSuchMethodException {
    Method method = GenericClass.class.getDeclaredMethod("someGenericMethod", Person.class, List.class, Object.class);
    assertThat(unambiguousToStringOf(method)).isEqualTo(format("%s (Method@%s)",
                                                               method.toGenericString(),
                                                               toHexString(System.identityHashCode(method))));
  }

  @Test
  public void should_disambiguate_non_equal_objects_with_same_hash_code_and_toString_representations() {
    assertThat(unambiguousToStringOf(new Ambiguous(0, 1))).isNotEqualTo(unambiguousToStringOf(new Ambiguous(0, 2)));
  }

  @Test
  public void isEqualTo_should_show_disambiguated_objects_with_same_hash_code_and_toString_representations() {
    // GIVEN
    Ambiguous ambiguous1 = new Ambiguous(0, 1);
    Ambiguous ambiguous2 = new Ambiguous(0, 2);
    // WHEN
    AssertionError error = catchThrowableOfType(() -> assertThat(ambiguous1).isEqualTo(ambiguous2), AssertionError.class);
    // THEN
    assertThat(error).hasMessageContaining(unambiguousToStringOf(ambiguous1))
                     .hasMessageContaining(unambiguousToStringOf(ambiguous2));
  }

  private static String unambiguousToStringOf(Object o) {
    return STANDARD_REPRESENTATION.unambiguousToStringOf(o);
  }

  private static class MyTestFile extends File {
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
