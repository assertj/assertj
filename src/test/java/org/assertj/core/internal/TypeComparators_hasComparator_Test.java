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

public class TypeComparators_hasComparator_Test {

  private TypeComparators typeComparators;

  @BeforeEach
  public void setUp() {
    typeComparators = new TypeComparators();
  }

  @Test
  public void should_find_comparator() {
    typeComparators.put(Foo.class, newComparator());
    // WHEN
    boolean comparatorFound = typeComparators.hasComparatorForType(Foo.class);
    // THEN
    assertThat(comparatorFound).isTrue();
  }

  @Test
  public void should_find_parent_comparator() {
    typeComparators.put(Bar.class, newComparator());
    // WHEN
    boolean comparatorFound = typeComparators.hasComparatorForType(Foo.class);
    // THEN
    assertThat(comparatorFound).isTrue();
  }

  @Test
  public void should_not_find_any_comparator() {
    // GIVEN
    Comparator<I3> i3Comparator = newComparator();
    Comparator<I4> i4Comparator = newComparator();
    Comparator<Foo> fooComparator = newComparator();
    typeComparators.put(I3.class, i3Comparator);
    typeComparators.put(I4.class, i4Comparator);
    typeComparators.put(Foo.class, fooComparator);
    // WHEN
    boolean comparatorFound = typeComparators.hasComparatorForType(I5.class);
    // THEN
    assertThat(comparatorFound).isFalse();
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
