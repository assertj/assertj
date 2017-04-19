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
package org.assertj.core.internal;

import static java.lang.Math.E;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.ObjectsBaseTest.defaultTypeComparators;
import static org.assertj.core.internal.ObjectsBaseTest.noFieldComparators;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.Test;

/**
 * Based on the deep equals tests of https://github.com/jdereg/java-util
 * 
 * @author John DeRegnaucourt
 * @author sapradhan8
 * @author Pascal Schumacher
 */
public class DeepDifference_Test {

  @Test
  public void testSameObject() {
    Date date1 = new Date();
    Date date2 = date1;
    assertHaveNoDifferences(date1, date2);
  }

  @Test
  public void testEqualsWithNull() {
    Date date1 = new Date();
    assertHaveDifferences(null, date1);
    assertHaveDifferences(date1, null);
  }

  @Test
  public void testBigDecimals() {
    BigDecimal bigDecimal1 = new BigDecimal("42.5");
    BigDecimal bigDecimal2 = new BigDecimal("42.5");
    assertHaveNoDifferences(bigDecimal1, bigDecimal2);
  }

  @Test
  public void testWithDifferentFields() {
    assertHaveDifferences("one", 1);
    assertHaveDifferences(new Wrapper(new Wrapper("one")), new Wrapper("one"));
  }

  @Test
  public void testPOJOequals() {
    Class1 x = new Class1(true, tan(PI / 4), 1);
    Class1 y = new Class1(true, 1.0, 1);
    assertHaveNoDifferences(x, y);
    assertHaveDifferences(x, new Class1());

    Class2 a = new Class2((float) atan(1.0), "hello", (short) 2, new Class1(false, sin(0.75), 5));
    Class2 b = new Class2((float) PI / 4, "hello", (short) 2, new Class1(false, 2 * cos(0.75 / 2) * sin(0.75 / 2), 5));

    assertHaveNoDifferences(a, b);
    assertHaveDifferences(a, new Class2((float) atan(2.0), "hello", (short) 2, new Class1(false, sin(0.75), 5)));
  }

  @Test
  public void testPrimitiveArrays() {
    int array1[] = { 2, 4, 5, 6, 3, 1, 3, 3, 5, 22 };
    int array2[] = { 2, 4, 5, 6, 3, 1, 3, 3, 5, 22 };

    assertHaveNoDifferences(array1, array2);

    int array3[] = { 3, 4, 7 };

    assertHaveDifferences(array1, array3);

    float array4[] = { 3.4f, 5.5f };
    assertHaveDifferences(array1, array4);
  }

  @Test
  public void testOrderedCollection() {
    List<String> a = newArrayList("one", "two", "three", "four", "five");
    List<String> b = new LinkedList<>();
    b.addAll(a);
    assertHaveNoDifferences(a, b);

    List<Integer> c = newArrayList(1, 2, 3, 4, 5);
    assertHaveDifferences(a, c);

    List<Integer> d = newArrayList(4, 6);
    assertHaveDifferences(c, d);

    List<Class1> x1 = newArrayList(new Class1(true, log(pow(E, 2)), 6), new Class1(true, tan(PI / 4), 1));
    List<Class1> x2 = newArrayList(new Class1(true, 2, 6), new Class1(true, 1, 1));
    assertHaveNoDifferences(x1, x2);
  }

  @Test
  public void testUnorderedCollection() {
    Set<String> a = newLinkedHashSet("one", "two", "three", "four", "five");
    Set<String> b = newLinkedHashSet("three", "five", "one", "four", "two");
    assertHaveNoDifferences(a, b);
    assertHaveNoDifferences(a, b, noFieldComparators(), new TypeComparators());

    Set<Integer> c = newLinkedHashSet(1, 2, 3, 4, 5);
    assertHaveDifferences(a, c);
    assertHaveDifferences(a, c, noFieldComparators(), null);

    Set<Integer> d = newLinkedHashSet(4, 2, 6);
    assertHaveDifferences(c, d);
    assertHaveDifferences(c, d, noFieldComparators(), null);

    Set<Class1> x1 = newLinkedHashSet(new Class1(true, log(pow(E, 2)), 6), new Class1(true, tan(PI / 4), 1));
    Set<Class1> x2 = newLinkedHashSet(new Class1(true, 1, 1), new Class1(true, 2, 6));
    assertHaveNoDifferences(x1, x2);
  }

  @Test
  public void testUnorderedCollectionWithCustomComparatorsByType() {
    TypeComparators comparatorsWithBigDecimalComparator = new TypeComparators();
    comparatorsWithBigDecimalComparator.put(BigDecimal.class, BIG_DECIMAL_COMPARATOR);

    Set<BigDecimal> a = newLinkedHashSet(new BigDecimal("1.0"), new BigDecimal("3"), new BigDecimal("2"), new BigDecimal("4"));
    Set<BigDecimal> b = newLinkedHashSet(new BigDecimal("4"), new BigDecimal("1"), new BigDecimal("2.0"), new BigDecimal("3"));

    assertHaveNoDifferences(a, b, noFieldComparators(), comparatorsWithBigDecimalComparator);

    Set<BigDecimal> c = newLinkedHashSet(new BigDecimal("4"), new BigDecimal("1"), new BigDecimal("2.2"), new BigDecimal("3"));
    assertHaveDifferences(a, c, noFieldComparators(), comparatorsWithBigDecimalComparator);
  }

  @Test
  public void testUnorderedCollectionWithCustomComparatorsByFieldName() {
    SetWrapper a = new SetWrapper(newLinkedHashSet(new Wrapper("one"), new Wrapper("two")));
    SetWrapper b = new SetWrapper(newLinkedHashSet(new Wrapper("1"), new Wrapper("2")));

    Map<String, Comparator<?>> fieldComparators = new HashMap<>();
    fieldComparators.put("set.o", ALWAY_EQUALS_STRING);
    assertHaveNoDifferences(a, b, fieldComparators, defaultTypeComparators());
  }

  @Test
  public void testEquivalentMaps() {
    Map<String, Integer> map1 = new LinkedHashMap<>();
    fillMap(map1);
    Map<String, Integer> map2 = new HashMap<>();
    fillMap(map2);
    assertHaveNoDifferences(map1, map2);
    assertThat(DeepDifference.deepHashCode(map1)).isEqualTo(DeepDifference.deepHashCode(map2));

    map1 = new TreeMap<>();
    fillMap(map1);
    map2 = new TreeMap<>();
    map2 = Collections.synchronizedSortedMap((SortedMap<String, Integer>) map2);
    fillMap(map2);
    assertHaveNoDifferences(map1, map2);
    assertThat(DeepDifference.deepHashCode(map1)).isEqualTo(DeepDifference.deepHashCode(map2));
  }

  @Test
  public void testInequivalentMaps() {
    Map<String, Integer> map1 = new TreeMap<>();
    fillMap(map1);
    Map<String, Integer> map2 = new HashMap<>();
    fillMap(map2);
    // Sorted versus non-sorted Map
    assertHaveDifferences(map1, map2);

    // Hashcodes are equals because the Maps have same elements
    assertThat(DeepDifference.deepHashCode(map1)).isEqualTo(DeepDifference.deepHashCode(map2));

    map2 = new TreeMap<>();
    fillMap(map2);
    map2.remove("kilo");
    assertHaveDifferences(map1, map2);

    // Hashcodes are different because contents of maps are different
    assertThat(DeepDifference.deepHashCode(map1)).isNotEqualTo(DeepDifference.deepHashCode(map2));

    // Inequality because ConcurrentSkipListMap is a SortedMap
    map1 = new HashMap<>();
    fillMap(map1);
    map2 = new ConcurrentSkipListMap<>();
    fillMap(map2);
    assertHaveDifferences(map1, map2);

    map1 = new TreeMap<>();
    fillMap(map1);
    map2 = new ConcurrentSkipListMap<>();
    fillMap(map2);
    assertHaveNoDifferences(map1, map2);
    map2.remove("papa");
    assertHaveDifferences(map1, map2);
  }

  @Test
  public void testEquivalentCollections() {
    // ordered Collection
    Collection<String> col1 = new ArrayList<>();
    fillCollection(col1);
    Collection<String> col2 = new LinkedList<>();
    fillCollection(col2);
    assertHaveNoDifferences(col1, col2);
    assertThat(DeepDifference.deepHashCode(col1)).isEqualTo(DeepDifference.deepHashCode(col2));

    // unordered Collections (Set)
    col1 = new LinkedHashSet<>();
    fillCollection(col1);
    col2 = new HashSet<>();
    fillCollection(col2);
    assertHaveNoDifferences(col1, col2);
    assertThat(DeepDifference.deepHashCode(col1)).isEqualTo(DeepDifference.deepHashCode(col2));

    col1 = new TreeSet<>();
    fillCollection(col1);
    col2 = new TreeSet<>();
    Collections.synchronizedSortedSet((SortedSet<String>) col2);
    fillCollection(col2);
    assertHaveNoDifferences(col1, col2);
    assertThat(DeepDifference.deepHashCode(col1)).isEqualTo(DeepDifference.deepHashCode(col2));
  }

  @Test
  public void testInequivalentCollections() {
    Collection<String> col1 = new TreeSet<>();
    fillCollection(col1);
    Collection<String> col2 = new HashSet<>();
    fillCollection(col2);
    assertHaveDifferences(col1, col2);
    assertThat(DeepDifference.deepHashCode(col1)).isEqualTo(DeepDifference.deepHashCode(col2));

    col2 = new TreeSet<>();
    fillCollection(col2);
    col2.remove("lima");
    assertHaveDifferences(col1, col2);
    assertThat(DeepDifference.deepHashCode(col1)).isNotEqualTo(DeepDifference.deepHashCode(col2));

    assertHaveDifferences(new HashMap<>(), new ArrayList<>());
    assertHaveDifferences(new ArrayList<>(), new HashMap<>());
  }

  @Test
  public void testArray() {
    Object[] a1 = new Object[] { "alpha", "bravo", "charlie", "delta" };
    Object[] a2 = new Object[] { "alpha", "bravo", "charlie", "delta" };

    assertHaveNoDifferences(a1, a2);
    assertThat(DeepDifference.deepHashCode(a1)).isEqualTo(DeepDifference.deepHashCode(a2));
    a2[3] = "echo";
    assertHaveDifferences(a1, a2);
    assertThat(DeepDifference.deepHashCode(a1)).isNotEqualTo(DeepDifference.deepHashCode(a2));
  }

  @Test
  public void testHasCustomMethod() {
    assertThat(DeepDifference.hasCustomEquals(EmptyClass.class)).isFalse();
    assertThat(DeepDifference.hasCustomHashCode(Class1.class)).isFalse();

    assertThat(DeepDifference.hasCustomEquals(EmptyClassWithEquals.class)).isTrue();
    assertThat(DeepDifference.hasCustomHashCode(EmptyClassWithEquals.class)).isTrue();
  }

  @Test
  public void shouldBeAbleToUseCustomComparatorForHashMap() {
    class ObjectWithMapField {
      Map<Integer, Boolean> map;
    }
    ObjectWithMapField a = new ObjectWithMapField(), b = new ObjectWithMapField();
    a.map = new HashMap<>();
    a.map.put(1, true);

    b.map = new HashMap<>();
    b.map.put(2, true);

    @SuppressWarnings("rawtypes")
    class AlwaysEqualMapComparator implements Comparator<Map> {
      @Override
      public int compare(Map o1, Map o2) {
        return 0;
      }
    }

    TypeComparators typeComparators = new TypeComparators();
    typeComparators.put(Map.class, new AlwaysEqualMapComparator());
    assertThat(DeepDifference.determineDifferences(a, b, noFieldComparators(), typeComparators)).isEmpty();
  }

  private void assertHaveNoDifferences(Object x, Object y) {
    assertHaveNoDifferences(x, y, noFieldComparators(), defaultTypeComparators());
  }

  private void assertHaveNoDifferences(Object x, Object y, Map<String, Comparator<?>> fieldComparators, TypeComparators typeComparators) {
    assertThat(DeepDifference.determineDifferences(x, y, fieldComparators, typeComparators)).isEmpty();
  }

  private void assertHaveDifferences(Object x, Object y) {
    assertHaveDifferences(x, y, noFieldComparators(), defaultTypeComparators());
  }

  private void assertHaveDifferences(Object x, Object y, Map<String, Comparator<?>> fieldComparators, TypeComparators typeComparators) {
    assertThat(DeepDifference.determineDifferences(x, y, fieldComparators, typeComparators)).isNotEmpty();
  }

  private static class EmptyClass {
  }

  private static class EmptyClassWithEquals {

    @Override
    public boolean equals(Object obj) {
      return obj instanceof EmptyClassWithEquals;
    }

    @Override
    public int hashCode() {
      return 0;
    }
  }

  private static class Class1 {

    @SuppressWarnings("unused")
    private boolean b;
    @SuppressWarnings("unused")
    private double d;
    @SuppressWarnings("unused")
    int i;

    public Class1() {
    }

    public Class1(boolean b, double d, int i) {
      this.b = b;
      this.d = d;
      this.i = i;
    }
  }

  private static class Class2 {

    @SuppressWarnings("unused")
    private Float f;
    @SuppressWarnings("unused")
    String s;
    @SuppressWarnings("unused")
    short ss;
    @SuppressWarnings("unused")
    Class1 c;

    public Class2(float f, String s, short ss, Class1 c) {
      this.f = f;
      this.s = s;
      this.ss = ss;
      this.c = c;
    }

  }

  private static class Wrapper {

    @SuppressWarnings("unused")
    private Object o;

    private Wrapper(Object o) {
      this.o = o;
    }
  }

  private static class SetWrapper {

    @SuppressWarnings("unused")
    private Set<?> set;

    private SetWrapper(Set<?> set) {
      this.set = set;
    }
  }

  private void fillMap(Map<String, Integer> map) {
    map.put("zulu", 26);
    map.put("alpha", 1);
    map.put("bravo", 2);
    map.put("charlie", 3);
    map.put("delta", 4);
    map.put("echo", 5);
    map.put("foxtrot", 6);
    map.put("golf", 7);
    map.put("hotel", 8);
    map.put("india", 9);
    map.put("juliet", 10);
    map.put("kilo", 11);
    map.put("lima", 12);
    map.put("mike", 13);
    map.put("november", 14);
    map.put("oscar", 15);
    map.put("papa", 16);
    map.put("quebec", 17);
    map.put("romeo", 18);
    map.put("sierra", 19);
    map.put("tango", 20);
    map.put("uniform", 21);
    map.put("victor", 22);
    map.put("whiskey", 23);
    map.put("xray", 24);
    map.put("yankee", 25);
  }

  private void fillCollection(Collection<String> col) {
    col.add("zulu");
    col.add("alpha");
    col.add("bravo");
    col.add("charlie");
    col.add("delta");
    col.add("echo");
    col.add("foxtrot");
    col.add("golf");
    col.add("hotel");
    col.add("india");
    col.add("juliet");
    col.add("kilo");
    col.add("lima");
    col.add("mike");
    col.add("november");
    col.add("oscar");
    col.add("papa");
    col.add("quebec");
    col.add("romeo");
    col.add("sierra");
    col.add("tango");
    col.add("uniform");
    col.add("victor");
    col.add("whiskey");
    col.add("xray");
    col.add("yankee");
  }
}
