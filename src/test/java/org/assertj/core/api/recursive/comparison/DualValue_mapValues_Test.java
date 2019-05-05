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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.Maps.treeMapOf;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

public class DualValue_mapValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest
  @MethodSource("maps")
  public void isActualFieldAMap_should_return_true_when_actual_is_not_an_iterable(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean haveMapValues = dualValue.isActualFieldAMap();
    // THEN
    assertThat(haveMapValues).isTrue();
  }

  @ParameterizedTest
  @MethodSource("maps")
  public void isExpectedFieldAMap_should_return_true_when_expected_is_not_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean haveMapValues = dualValue.isExpectedFieldAMap();
    // THEN
    assertThat(haveMapValues).isTrue();
  }

  static Stream<Object> maps() {
    return Stream.of(singletonMap("a", "b"),
                     mapOf(entry(1, 2), entry(3, 4)),
                     treeMapOf(entry("a", "b")),
                     ImmutableMap.of("a", "b"),
                     ImmutableSortedMap.of("a", "b"));
  }

  @ParameterizedTest
  @MethodSource("nonMaps")
  public void isActualFieldAMap_should_return_false_when_actual_is_not_an_iterable(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, singletonMap("a", "b"));
    // WHEN
    boolean haveMapValues = dualValue.isActualFieldAMap();
    // THEN
    assertThat(haveMapValues).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonMaps")
  public void isExpectedFieldAMap_should_return_false_when_expected_is_not_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, singletonMap("a", "b"), expected);
    // WHEN
    boolean haveMapValues = dualValue.isExpectedFieldAMap();
    // THEN
    assertThat(haveMapValues).isFalse();
  }

  static Stream<Object> nonMaps() {
    return Stream.of(list("a", "b"), "abc", 123, null);
  }

}
