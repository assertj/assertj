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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.assertj.core.test.Jedi;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ExtendedByTypesComparator_compareTo_Test {

  private static final TypeComparators COMPARATORS_BY_TYPE = new TypeComparators();
  private static final ExtendedByTypesComparator EXTENDED_STANDARD_COMPARATOR = new ExtendedByTypesComparator(COMPARATORS_BY_TYPE);
  private static final ExtendedByTypesComparator EXTENDED_FIELD_BY_FIELD_COMPARATOR = new ExtendedByTypesComparator(new FieldByFieldComparator(),
                                                                                                                    COMPARATORS_BY_TYPE);

  public static Iterable<ExtendedByTypesComparator> data() {
    return asList(EXTENDED_STANDARD_COMPARATOR, EXTENDED_FIELD_BY_FIELD_COMPARATOR);
  }

  @BeforeAll
  public static void beforeClass() {
    COMPARATORS_BY_TYPE.put(BigDecimal.class, new BigDecimalComparator());
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_return_equal_if_both_objects_are_null(ExtendedByTypesComparator extendedComparator) {
    assertThat(extendedComparator.compare(null, null)).isZero();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_return_are_not_equal_if_first_object_is_null_and_second_is_not(ExtendedByTypesComparator extendedComparator) {
    assertThat(extendedComparator.compare(null, "some")).isNotZero();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_return_are_not_equal_if_second_object_is_null_and_first_is_not(ExtendedByTypesComparator extendedComparator) {
    assertThat(extendedComparator.compare("some", null)).isNotZero();
  }

  @Test
  public void should_return_equal_if_objects_are_equal_by_default_comparator() {
    assertThat(EXTENDED_STANDARD_COMPARATOR.compare(new Jedi("Yoda", "Green"), new Jedi("Yoda", "Green"))).isZero();
    assertThat(EXTENDED_FIELD_BY_FIELD_COMPARATOR.compare(new Jedi("Yoda", "Green"),
                                                          new Jedi("Yoda", "Green"))).isZero();
  }

  @Test
  public void should_return_are_not_equal_if_objects_are_not_equal_by_default_comparator() {
    assertThat(EXTENDED_STANDARD_COMPARATOR.compare(new Jedi("Yoda", "Green"), new Jedi("Luke", "Blue"))).isNotZero();
    assertThat(EXTENDED_FIELD_BY_FIELD_COMPARATOR.compare(new Jedi("Yoda", "Green"),
                                                          new Jedi("Yoda", "Any"))).isNotZero();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_return_equal_if_objects_are_equal_by_type_comparator(ExtendedByTypesComparator extendedComparator) {
    assertThat(extendedComparator.compare(new BigDecimal("4.2"), new BigDecimal("4.20"))).isZero();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_return_are_not_equal_if_objects_are_not_equal_by_type_comparator(ExtendedByTypesComparator extendedComparator) {
    assertThat(extendedComparator.compare(new BigDecimal(42), new BigDecimal("42.5"))).isNotZero();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void should_return_are_not_equal_if_objects_are_different_by_type_for_type_comparator(ExtendedByTypesComparator extendedComparator) {
    assertThat(extendedComparator.compare(new BigDecimal(42), "some")).isNotZero();
  }
}
