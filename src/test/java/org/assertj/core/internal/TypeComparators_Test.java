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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
public class TypeComparators_Test {

  private TypeComparators typeComparators;

  @BeforeEach
  public void setUp() {
    typeComparators = new TypeComparators();
  }

  @Test
  public void should_return_exact_match() {
    Comparator<Foo> fooComparator = newComparator();
    Comparator<Bar> barComparator = newComparator();
    typeComparators.put(Foo.class, fooComparator);
    typeComparators.put(Bar.class, barComparator);

    Comparator<?> foo = typeComparators.get(Foo.class);
    Comparator<?> bar = typeComparators.get(Bar.class);
    assertThat(foo).isEqualTo(fooComparator);
    assertThat(bar).isEqualTo(barComparator);
  }

  @Test
  public void should_return_parent_comparator() {
    Comparator<Bar> barComparator = newComparator();
    typeComparators.put(Bar.class, barComparator);

    Comparator<?> foo = typeComparators.get(Foo.class);
    Comparator<?> bar = typeComparators.get(Bar.class);
    assertThat(foo).isEqualTo(bar);
    assertThat(bar).isEqualTo(barComparator);
  }

  @Test
  public void should_return_most_relevant() {

    Comparator<I3> i3Comparator = newComparator();
    Comparator<I4> i4Comparator = newComparator();
    Comparator<I1> i1Comparator = newComparator();
    Comparator<I2> i2Comparator = newComparator();
    Comparator<Foo> fooComparator = newComparator();
    typeComparators.put(I3.class, i3Comparator);
    typeComparators.put(I4.class, i4Comparator);
    typeComparators.put(I1.class, i1Comparator);
    typeComparators.put(I2.class, i2Comparator);
    typeComparators.put(Foo.class, fooComparator);

    Comparator<?> foo = typeComparators.get(Foo.class);
    Comparator<?> bar = typeComparators.get(Bar.class);
    Comparator<?> i3 = typeComparators.get(I3.class);
    Comparator<?> i4 = typeComparators.get(I4.class);
    Comparator<?> i5 = typeComparators.get(I5.class);
    assertThat(foo).isEqualTo(fooComparator);
    assertThat(bar).isEqualTo(i3Comparator);
    assertThat(i3).isEqualTo(i3Comparator);
    assertThat(i4).isEqualTo(i4Comparator);
    assertThat(i5).isEqualTo(i1Comparator);
  }

  @Test
  public void should_find_no_comparator() {

    Comparator<I3> i3Comparator = newComparator();
    Comparator<I4> i4Comparator = newComparator();
    Comparator<Foo> fooComparator = newComparator();
    typeComparators.put(I3.class, i3Comparator);
    typeComparators.put(I4.class, i4Comparator);
    typeComparators.put(Foo.class, fooComparator);

    Comparator<?> i5 = typeComparators.get(I5.class);
    assertThat(i5).isNull();
  }

  @Test
  public void should_be_empty() {
    typeComparators.typeComparators.clear();
    assertThat(typeComparators.isEmpty()).isTrue();
  }

  private static <T> Comparator<T> newComparator() {
    return (T o1, T o2) -> 0;
  }

  private interface I1 {
  }

  private interface I2 {
  }

  private interface I3 {
  }

  private interface I4 {
  }

  private interface I5 extends I1, I2 {
  }

  private class Bar implements I3 {

  }

  private class Foo extends Bar implements I4, I5 {

  }

}
